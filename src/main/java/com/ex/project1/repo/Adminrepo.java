package com.ex.project1.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ex.project1.entity.Admin;

public interface Adminrepo extends JpaRepository<Admin,Long> {
  Admin findBymobilenumber(String mobilenumber); 
   Admin findBypassword(String password);
    
}
