package com.example.demo1.service;

import com.example.demo1.domain.po.UserEntity;

public interface AuthService {
    String login(String username, String password, String usertype);
    UserEntity addUser(UserEntity userEntity);
}
