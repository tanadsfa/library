package com.example.demo1.controller;

import com.example.demo1.domain.po.Book;
import com.example.demo1.service.BookService;
import com.example.demo1.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/book")
public class BookController {

    @Autowired
    private BookService bookService;

    @PostMapping("/getbooks")
    public R getBooks() {
        List<Book> books = bookService.getBooks();
        return R.success(books);
    }

    @PostMapping("/add")
    public R addBook(@RequestBody Book book) {
        bookService.saveBook(book);
        return R.success("add success");
    }

    @PostMapping("/update")
    public R updateBook(@RequestBody Book book) {
        boolean isSuccess = bookService.updateBook(book);
        String message = isSuccess ? "update success" : "update error";
        return R.success(message);
    }

    @PostMapping("/delete")
    public R deleteBook(@RequestBody Book book) {
        int result = bookService.deleteBook(book);
        String message = result > 0 ? "delete success" : "delete error";
        return R.success(message);
    }

    @PostMapping("/search")
    public R searchBooks(@RequestBody Map<String, String> body) {
        String keyword = body.get("keyword");
        List<Book> books = bookService.searchBooks(keyword);
        return R.success(books);
    }

}
