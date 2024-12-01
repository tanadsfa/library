package com.example.demo1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.demo1.domain.po.UserEntity;
import com.example.demo1.service.UserService;
import com.example.demo1.utils.R;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/getusers")
    public R getUsers() {
        List<UserEntity> entities = userService.getUsers();
        return R.success(entities);
    }

    @PostMapping("/updateuser")
    public R updateUser(@RequestBody UserEntity userEntity) {
        boolean isSuccess = userService.updateUser(userEntity);
        String message = isSuccess ? "update success" : "update error";
        return R.success(message);
    }

    @PostMapping("/deleteuser")
    public R deleteUser(@RequestBody UserEntity userEntity) {
        int result = userService.deleteUser(userEntity);
        String message = result > 0 ? "delete success" : "delete error";
        return R.success(message);
    }
}