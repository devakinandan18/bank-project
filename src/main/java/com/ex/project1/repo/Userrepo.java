package com.ex.project1.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ex.project1.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.Query;

public interface Userrepo extends JpaRepository<User, Long> {

    User findBymobilenumber(String mobilenumber);
    User findByaccountnumber(String accountnumber);
    User findBypassword(String password);
    @Query("SELECT SUM(u.balance) FROM User u")
Double totalbalance();
}