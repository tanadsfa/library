package com.example.demo1.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo1.domain.po.Book;
import com.example.demo1.mapper.BookMapper;
import com.example.demo1.service.BookService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl extends ServiceImpl<BookMapper, Book> implements BookService {

    @Override
    public Book getBookById(Long bookId) { 
        QueryWrapper<Book> wrapper = new QueryWrapper<>();
        wrapper.eq("id", bookId);
        return this.getOne(wrapper);
    }

    @Override
    public Book saveBook(Book book) {
        Book existingBook = this.getBookById(book.getId());
        if (existingBook == null) {
            this.save(book);
        } else {
            Long id = existingBook.getId();
            book.setId(id);
            this.updateById(book);
        }
        return book;
    }

    @Override
    public boolean updateBook(Book book) {
        return this.updateById(book);
    }

    @Override
    public int deleteBook(Book book) {
        return this.getBaseMapper().deleteById(book.getId());
    }

    @Override
    public List<Book> getBooks() {
        return this.list();
    }

    @Override
    public List<Book> searchBooks(String keyword) {
        return this.getBaseMapper().searchBooks(keyword);
    }

    @Override
    public List<Book> getAvailableBooks() { // 实现获取在库书籍的方法
        QueryWrapper<Book> wrapper = new QueryWrapper<>();
        wrapper.eq("in_stock", true);
        return this.list(wrapper);
    }
}
