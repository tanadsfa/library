package com.example.demo1.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo1.domain.po.UserEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<UserEntity> {
}
