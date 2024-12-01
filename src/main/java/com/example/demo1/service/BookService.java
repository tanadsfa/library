package com.example.demo1.service;

import com.example.demo1.domain.po.Book;

import java.util.List;

public interface BookService {
    Book getBookById(Long bookId); // 修改方法参数类型
    Book saveBook(Book book);
    boolean updateBook(Book book);
    int deleteBook(Book book);
    List<Book> getBooks();
    List<Book> searchBooks(String keyword); // 新增的搜索方法
    List<Book> getAvailableBooks(); // 新增的方法
}
