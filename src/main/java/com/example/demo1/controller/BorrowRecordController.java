package com.example.demo1.controller;

import com.example.demo1.domain.po.Book;
import com.example.demo1.domain.po.BorrowRecord;
import com.example.demo1.service.BookService;
import com.example.demo1.service.BorrowRecordService;
import com.example.demo1.service.UserService;
import com.example.demo1.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/borrow")
public class BorrowRecordController {

    @Autowired
    private BorrowRecordService borrowRecordService;

    @Autowired
    private BookService bookService;

    @Autowired
    private UserService userService;

    @PostMapping("/getrecords")
    public R getBorrowRecords(@RequestBody Map<String, Object> body) {
        String username = body.get("username").toString();
        String usertype = body.get("usertype").toString();
        
        List<BorrowRecord> records;
        if ("manager".equals(usertype)) {
            records = borrowRecordService.getBorrowRecords();
        } else {
            Long userId = userService.getUserIdByUsername(username);
            records = borrowRecordService.getRecordsByUserId(userId);
        }
        return R.success(records);
    }

    @PostMapping("/add")
    public R addBorrowRecord(@RequestBody BorrowRecord borrowRecord) {
        borrowRecord.setBorrowDate(new Date()); // 使用当前日期
        borrowRecordService.saveBorrowRecord(borrowRecord);
        return R.success("add success");
    }

    @PostMapping("/update")
    public R updateBorrowRecord(@RequestBody BorrowRecord borrowRecord) {
        boolean isSuccess = borrowRecordService.updateBorrowRecord(borrowRecord);
        String message = isSuccess ? "update success" : "update error";
        return R.success(message);
    }

    @PostMapping("/delete")
    public R deleteBorrowRecord(@RequestBody BorrowRecord borrowRecord) {
        int result = borrowRecordService.deleteBorrowRecord(borrowRecord);
        String message = result > 0 ? "delete success" : "delete error";
        return R.success(message);
    }

    @PostMapping("/return")
    public R returnBook(@RequestBody Map<String, Long> body) {
        Long borrowRecordId = body.get("borrowRecordId");
        boolean isSuccess = borrowRecordService.returnBook(borrowRecordId);
        String message = isSuccess ? "return success" : "return error";
        return R.success(message);
    }

    @PostMapping("/availablebooks")
    public R getAvailableBooks() {
        List<Book> books = bookService.getAvailableBooks();
        return R.success(books);
    }

    @PostMapping("/recordsbyuser")
    public R getRecordsByUserId(@RequestBody Map<String, Long> body) {
        Long userId = body.get("userId");
        List<BorrowRecord> records = borrowRecordService.getRecordsByUserId(userId);
        return R.success(records);
    }

    @PostMapping("/recordsbybook")
    public R getRecordsByBookId(@RequestBody Map<String, Long> body) {
        Long bookId = body.get("bookId");
        List<BorrowRecord> records = borrowRecordService.getRecordsByBookId(bookId);
        return R.success(records);
    }
}
