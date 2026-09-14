package com.ex.project1.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import com.ex.project1.entity.Transaction;

public interface Transactionrepo extends JpaRepository<Transaction, Long> {

     List<Transaction> findByAccountnumberOrderByDate(String accountnumber);
     
}