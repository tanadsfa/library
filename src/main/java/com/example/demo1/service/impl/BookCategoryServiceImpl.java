package com.example.demo1.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo1.domain.po.BookCategory;
import com.example.demo1.mapper.BookCategoryMapper;
import com.example.demo1.service.BookCategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookCategoryServiceImpl extends ServiceImpl<BookCategoryMapper, BookCategory> implements BookCategoryService {

    @Override
    public BookCategory getCategoryByName(String name) {
        QueryWrapper<BookCategory> wrapper = new QueryWrapper<>();
        wrapper.eq("name", name);
        return this.getOne(wrapper);
    }

    @Override
    public BookCategory saveCategory(BookCategory category) {
        BookCategory existingCategory = this.getCategoryByName(category.getName());
        if (existingCategory == null) {
            this.save(category);
        } else {
            Long id = existingCategory.getId();
            category.setId(id);
            this.updateById(category);
        }
        return category;
    }

    @Override
    public boolean updateCategory(BookCategory category) {
        return this.updateById(category);
    }

    @Override
    public int deleteCategory(BookCategory category) {
        return this.getBaseMapper().deleteById(category.getId());
    }

    @Override
    public List<BookCategory> getCategories() {
        return this.list();
    }
}
