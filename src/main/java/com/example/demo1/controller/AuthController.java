package com.example.demo1.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.demo1.domain.po.UserEntity;
import com.example.demo1.service.AuthService;
import com.example.demo1.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;
    @PostMapping("/login")
    public R login(@RequestBody JSONObject params) {
        String username = params.getString("username");
        String password = params.getString("password");
        String usertype = params.getString("usertype");
        if (!StringUtils.hasText(username) || !StringUtils.hasText(password) || !StringUtils.hasText(usertype)) {
            return R.error(500, "用户名或密码或用户类型为空！");
        }
        String token = authService.login(username, password, usertype);
        return R.success(token);
    }
    
    @PostMapping("/add/user")
    public R addUser(@RequestBody UserEntity userEntity) {
        UserEntity entity = authService.addUser(userEntity);
        return R.success(entity);
    }

}
