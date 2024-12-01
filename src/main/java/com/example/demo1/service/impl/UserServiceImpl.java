package com.example.demo1.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo1.domain.po.UserEntity;
import com.example.demo1.mapper.UserMapper;
import com.example.demo1.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements UserService {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserEntity getUserByName(String username) {
        QueryWrapper<UserEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username);
        return this.getOne(wrapper);
    }
   
    @Override
    public UserEntity saveUser(UserEntity entity) {
        if (null == entity.getCreateTime()) {
            entity.setCreateTime(new Date());
        }
        entity.setPassword(passwordEncoder.encode(entity.getPassword()));
        UserEntity user = this.getUserByName(entity.getUsername());
        if (null == user) {
            this.save(entity);
            user = entity;
        } else {
            Long id = user.getId();
            BeanUtils.copyProperties(entity, user);
            user.setId(id);
            this.updateById(user);
        }
        return user;
    }
    public List<UserEntity>getUsers(){
        List<UserEntity> uList = this.list();
        return uList;
    }   

    public boolean updateUser(UserEntity userEntity) {
        return this.updateById(userEntity);
    }

    public int deleteUser(UserEntity userEntity) { 
        return this.getBaseMapper().deleteById(userEntity);
    }

    @Override
    public Long getUserIdByUsername(String username) {
        UserEntity user = this.getUserByName(username);
        return user != null ? user.getId() : null;
    }
}
