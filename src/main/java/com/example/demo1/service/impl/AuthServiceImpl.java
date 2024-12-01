package com.example.demo1.service.impl;


import com.alibaba.fastjson.JSON;
import com.example.demo1.constant.RedisKey;
import com.example.demo1.domain.dto.SecurityUser;
import com.example.demo1.domain.po.UserEntity;
import com.example.demo1.service.AuthService;
import com.example.demo1.service.UserService;
import com.example.demo1.utils.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;

@Service
public class AuthServiceImpl implements AuthService {
    Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserService userService;

    @SuppressWarnings("null")
    @Override
    public String login(String username, String password, String usertype) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        if (authenticate == null) {
            log.error("{username: {}, password: {}} 认证失败！", username, password);
            return null;
        }
        SecurityUser user = (SecurityUser) authenticate.getPrincipal();
        // userEntity
        UserEntity userEntity = user.getUserEntity();
        // 验证用户类型
        if (!usertype.equals(userEntity.getUsertype())) {
            log.error("用户类型不匹配: {username: {}, usertype: {}}", username, usertype);
            return null;
        }
        // 以用户表的行政区划代码作为盐值（这里主要是为了程序更好扩展实际开发中盐值可以是一些特殊或唯一的标识）
        String token = JwtUtil.createToken(userEntity.getRegionCode(), username, null);
        redisTemplate.opsForValue().set(String.format(RedisKey.AUTH_TOKEN_KEY, username),
                JSON.toJSONString(user), JwtUtil.JWT_EXPIRE_TIME, TimeUnit.MILLISECONDS);
        return token;
    }

    @Override
    public UserEntity addUser(UserEntity userEntity) {
        return userService.saveUser(userEntity);
    }
}
