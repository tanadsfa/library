package com.example.demo1.service;

import com.example.demo1.domain.po.BookCategory;

import java.util.List;

public interface BookCategoryService {
    BookCategory getCategoryByName(String name);
    BookCategory saveCategory(BookCategory category);
    boolean updateCategory(BookCategory category);
    int deleteCategory(BookCategory category);
    List<BookCategory> getCategories();
}
