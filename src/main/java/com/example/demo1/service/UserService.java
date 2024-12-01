package com.example.demo1.service;


import java.util.List;

import com.example.demo1.domain.po.UserEntity;


public interface UserService {

    UserEntity getUserByName(String username);

    UserEntity saveUser(UserEntity userEntity);
    boolean updateUser(UserEntity userEntity);
    int deleteUser(UserEntity userEntity);
    List<UserEntity> getUsers();
    Long getUserIdByUsername(String username); // 新增的方法
}
