package com.ycshang.fileservice.utils;

import io.minio.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.InputStream;


@Component
@Configuration
public class MinIoTemplate {

    @Value("${minio.endPoint}")
    private String endPoint;

    @Value("${minio.accessKey}")
    private String accessKey;

    @Value("${minio.secretKey}")
    private String secretKey;

    private MinioClient instance;

    @PostConstruct
    public void init() {
        instance = MinioClient.builder()
                .endpoint(endPoint)
                .credentials(accessKey, secretKey)
                .build();
    }

    /**
     * 判断 bucket是否存在
     */
    public boolean bucketExists(String bucketName) throws Exception {
        return instance.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
    }

    /**
     * 创建 bucket
     */
    public void makeBucket(String bucketName) throws Exception {
        boolean isExist = this.bucketExists(bucketName);
        if (!isExist) {
            instance.makeBucket(
                    MakeBucketArgs.builder()
                            .bucket(bucketName)
                            .build());

            String policyJson = "{\n" +
                    "\t\"Version\": \"2012-10-17\",\n" +
                    "\t\"Statement\": [{\n" +
                    "\t\t\"Effect\": \"Allow\",\n" +
                    "\t\t\"Principal\": {\n" +
                    "\t\t\t\"AWS\": [\"*\"]\n" +
                    "\t\t},\n" +
                    "\t\t\"Action\": [\"s3:GetBucketLocation\", \"s3:ListBucket\", \"s3:ListBucketMultipartUploads\"],\n" +
                    "\t\t\"Resource\": [\"arn:aws:s3:::" + bucketName + "\"]\n" +
                    "\t}, {\n" +
                    "\t\t\"Effect\": \"Allow\",\n" +
                    "\t\t\"Principal\": {\n" +
                    "\t\t\t\"AWS\": [\"*\"]\n" +
                    "\t\t},\n" +
                    "\t\t\"Action\": [\"s3:AbortMultipartUpload\", \"s3:DeleteObject\", \"s3:GetObject\", \"s3:ListMultipartUploadParts\", \"s3:PutObject\"],\n" +
                    "\t\t\"Resource\": [\"arn:aws:s3:::" + bucketName + "/*\"]\n" +
                    "\t}]\n" +
                    "}\n";
            instance.setBucketPolicy(SetBucketPolicyArgs.builder().bucket(bucketName).config(policyJson).build());
        }
    }

    /**
     * 文件上传
     *
     * @param bucketName bucket名称
     * @param objectName 对象名称，文件名称
     * @param filepath   文件路径
     */
    public ObjectWriteResponse putObject(String bucketName, String objectName, String filepath) throws Exception {
        return instance.uploadObject(
                UploadObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .filename(filepath).build());
    }

    /**
     * 文件上传
     *
     * @param bucketName  bucket名称
     * @param objectName  对象名称，文件名称
     * @param inputStream 文件输入流
     */
    public ObjectWriteResponse putObject(String bucketName, String objectName, InputStream inputStream) throws Exception {
        return instance.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName).stream(inputStream, -1, 10485760)
                        .build());
    }

    /**
     * 删除文件
     *
     * @param bucketName bucket名称
     * @param objectName 对象名称
     */
    public void removeObject(String bucketName, String objectName) throws Exception {
        instance.removeObject(
                RemoveObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .build());
    }
}
