package com.example.demo1.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo1.domain.po.Book;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface BookMapper extends BaseMapper<Book> {
    @Select("SELECT b.* FROM book b " +
            "JOIN book_category c ON b.category_id = c.id " +
            "WHERE b.author LIKE CONCAT('%', #{keyword}, '%') OR c.name LIKE CONCAT('%', #{keyword}, '%')")
    List<Book> searchBooks(@Param("keyword") String keyword);
}
