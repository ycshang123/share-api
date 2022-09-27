package com.ycshang.userservice.repository;


import com.ycshang.userservice.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {


    /**
     * 根据账号密码查询用户
     * @param mobile
     * @param password
     * @return
     */
    User findByMobileAndPassword(String mobile,String password);
}
