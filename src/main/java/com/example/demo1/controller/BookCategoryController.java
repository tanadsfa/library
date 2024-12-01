package com.example.demo1.controller;

import com.example.demo1.domain.po.BookCategory;
import com.example.demo1.service.BookCategoryService;
import com.example.demo1.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/category")
public class BookCategoryController {

    @Autowired
    private BookCategoryService bookCategoryService;

    @PostMapping("/addgetcategories")
    public R addCategory(@RequestBody BookCategory category) {
        bookCategoryService.saveCategory(category);
        return R.success("add success");
    }

    @PostMapping("/getcategories")
    public R getCategories() {
        List<BookCategory> categories = bookCategoryService.getCategories();
        return R.success(categories);
    }

    @PostMapping("/updatecategory")
    public R updateCategory(@RequestBody BookCategory category) {
        boolean isSuccess = bookCategoryService.updateCategory(category);
        String message = isSuccess ? "update success" : "update error";
        return R.success(message);
    }

    @PostMapping("/deletecategory")
    public R deleteCategory(@RequestBody BookCategory category) {
        int result = bookCategoryService.deleteCategory(category);
        String message = result > 0 ? "delete success" : "delete error";
        return R.success(message);
    }
}
