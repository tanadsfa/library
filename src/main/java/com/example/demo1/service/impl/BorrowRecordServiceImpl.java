package com.example.demo1.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo1.domain.po.BorrowRecord;
import com.example.demo1.domain.po.Book;
import com.example.demo1.mapper.BorrowRecordMapper;
import com.example.demo1.service.BorrowRecordService;
import com.example.demo1.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BorrowRecordServiceImpl extends ServiceImpl<BorrowRecordMapper, BorrowRecord> implements BorrowRecordService {

    @Autowired
    private BookService bookService;

    @Override
    public BorrowRecord saveBorrowRecord(BorrowRecord borrowRecord) {
        this.save(borrowRecord);
        // 更新书本在库状态
        Book book = bookService.getBookById(borrowRecord.getBookId());
        book.setInStock(false);
        bookService.updateBook(book);
        return borrowRecord;
    }

    @Override
    public boolean updateBorrowRecord(BorrowRecord borrowRecord) {
        return this.updateById(borrowRecord);
    }

    @Override
    public int deleteBorrowRecord(BorrowRecord borrowRecord) {
        return this.getBaseMapper().deleteById(borrowRecord.getId());
    }

    @Override
    public List<BorrowRecord> getBorrowRecords() {
        return this.list();
    }

    @Override
    public boolean returnBook(Long borrowRecordId) {
        BorrowRecord borrowRecord = this.getById(borrowRecordId);
        if (borrowRecord != null && !borrowRecord.getIsReturned()) {
            borrowRecord.setIsReturned(true);
            this.updateById(borrowRecord);

            Book book = bookService.getBookById(borrowRecord.getBookId());
            book.setInStock(true);
            bookService.updateBook(book);

            return true;
        }
        return false;
    }

    @Override
    public List<BorrowRecord> getRecordsByUserId(Long userId) {
        QueryWrapper<BorrowRecord> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        return this.list(wrapper);
    }

    @Override
    public List<BorrowRecord> getRecordsByBookId(Long bookId) {
        QueryWrapper<BorrowRecord> wrapper = new QueryWrapper<>();
        wrapper.eq("book_id", bookId);
        return this.list(wrapper);
    }

    @Override
    public List<BorrowRecord> getRecordsByUsername(String username) {
        QueryWrapper<BorrowRecord> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username);
        return this.list(wrapper);
    }
}
