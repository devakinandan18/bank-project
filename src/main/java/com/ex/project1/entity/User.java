package com.ex.project1.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class User {

      @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
      public long id;
      @Column 
      public String name;
      @Column
      public String email;
      @Column
      public String accountnumber;
      @Column
      public String password;
      @Column
      public int pin;
      @Column
      public  String mobilenumber;
      @Column
      public String ifsccode;
      @Column
      public double balance;
      @Column
      public String status;
 
        public String getStatus() {
        return status;
    }
      public void setStatus(String status) {
          this.status = status;
      }
       
      public long getId() {
        return id;
    }
      public void setId(long id) {
          this.id = id;
      }
      public double getBalance() {
        return balance;
    }
      public void setBalance(double balance) {
          this.balance = balance;
      }
      public String getName() {
          return name;
      }
      public void setName(String name) {
          this.name = name;
      }
      public String getPassword() {
        return password;
    }
      public void setPassword(String password) {
          this.password = password;
      }
      public int getPin() {
          return pin;
      }
      public void setPin(int pin) {
          this.pin = pin;
      }
      public String getEmail() {
          return email;
      }
      public void setEmail(String email) {
          this.email = email;
      }
      public String getAccountnumber() {
          return accountnumber;
      }
      public void setAccountnumber(long id) {
          this.accountnumber = "10000"+id;
      }
      public String getMobilenumber() {
          return mobilenumber;
      }
      public void setMobilenumber(String mobilenumber) {
          this.mobilenumber = mobilenumber;
      }
      public String getIfsccode() {
          return ifsccode;
      }
      public void setIfsccode(String ifsccode) {
          this.ifsccode = ifsccode;
      }
}
