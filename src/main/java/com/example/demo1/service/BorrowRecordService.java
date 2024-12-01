package com.example.demo1.service;

import com.example.demo1.domain.po.BorrowRecord;

import java.util.List;

public interface BorrowRecordService {
    BorrowRecord saveBorrowRecord(BorrowRecord borrowRecord);
    boolean updateBorrowRecord(BorrowRecord borrowRecord);
    int deleteBorrowRecord(BorrowRecord borrowRecord);
    List<BorrowRecord> getBorrowRecords();
    boolean returnBook(Long borrowRecordId);
    List<BorrowRecord> getRecordsByUserId(Long userId); // 新增的方法
    List<BorrowRecord> getRecordsByBookId(Long bookId); // 新增的方法
    List<BorrowRecord> getRecordsByUsername(String username); // 新增的方法
}
