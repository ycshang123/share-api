package com.ycshang.contentservice.handler;

import com.ycshang.contentservice.common.ResultCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionErrorHandler {
    @ExceptionHandler(SecurityException.class)
    public ResponseEntity error(SecurityException e) {
        return new ResponseEntity<>(
                ErrorBody.builder()
                        .body(e.getMessage())
                        .status(HttpStatus.UNAUTHORIZED.value())
                        .build(),
                HttpStatus.UNAUTHORIZED

        );
    }

    @ExceptionHandler(NoSuchElementException.class)
    public  ResponseEntity handlerNoSuchElementException(NoSuchElementException e){
        return  new ResponseEntity<>(
            ErrorBody.builder().body(ResultCode.DATA_IS_UPDATE.message())
                    .status(ResultCode.DATA_IS_UPDATE.code())
                    .build(),
                HttpStatus.valueOf(ResultCode.DATA_IS_UPDATE.code())
        );
    }
}


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
class ErrorBody {
    private String body;
    private int status;
}
