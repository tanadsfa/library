package com.example.demo1.controller;


import com.example.demo1.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class HelloController {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @GetMapping("/hello")
    public R hello() {
        redisTemplate.opsForValue().set("name", "alex");
        return R.success(redisTemplate.opsForValue().get("name"));
    }
}
