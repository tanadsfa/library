package com.example.demo1.service.impl;

import com.example.demo1.domain.dto.SecurityUser;
import com.example.demo1.domain.po.UserEntity;
import com.example.demo1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userService.getUserByName(username);
        if (null == user) {
            throw new RuntimeException(String.format("not found [%s]", username));
        }
        return new SecurityUser(user);
    }
}
