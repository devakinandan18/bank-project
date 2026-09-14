package com.ex.project1.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ex.project1.entity.User;
import com.ex.project1.repo.Userrepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;
import com.ex.project1.entity.Transaction;
import com.ex.project1.repo.Transactionrepo;

import com.ex.project1.security.JwtService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/user")
public class Usercontroller {

    @Autowired
JwtService jwtService;

   @Autowired
    Userrepo userrepo;

    @Autowired
Transactionrepo transactionrepo;
    
    public Usercontroller(Userrepo userrepo) {
    this.userrepo = userrepo;
}
    
    @PostMapping
    public boolean addUser(@RequestBody User user) {
        User u=userrepo.findBymobilenumber(user.getMobilenumber());
       
        if(u==null)
        {
          userrepo.save(user);
         user.setAccountnumber(user.getId());
         user.setStatus("activate");
         userrepo.save(user);
          return true;
        }
        return false;
    }
    @GetMapping("/getuser")
    public User getuser(@RequestParam String accountnumber)
    {
        User u=userrepo.findByaccountnumber(accountnumber);
        //u.setBalance(0);
        if(u!=null)
        {
             u.setPin(0);
             u.setPassword(null);
            return u;
        }
        return null;
    }

    @GetMapping("/loginaccountnumber")
    public String loginaccountnumber(@RequestParam String accountnumber,@RequestParam String password)
    {
        User u=userrepo.findByaccountnumber(accountnumber);
        if(u==null)
        {
            return "false";
        }
        if(!(password.equals(u.getPassword())))
        {
            return "false";
        }
        return jwtService.generateToken(accountnumber);
    }

    @GetMapping("/loginmobilenumber")
    public String loginmobilenumber(@RequestParam String mobilenumber,@RequestParam String password)
    {
        User u=userrepo.findBymobilenumber(mobilenumber);
        if(u==null)
        {
            return "false";
        }
        if(!(password.equals(u.getPassword())))
        {
            return "false";
        }
         return jwtService.generateToken(u.getAccountnumber());
    }

    @GetMapping("/transactions")
public List<Transaction> transactions(@RequestParam String accountnumber)
{
    return transactionrepo.findByAccountnumberOrderByDate(accountnumber);
}
@GetMapping("/totaltransactions")
public int totaltransactions(@RequestParam String accountnumber)
{
    List<Transaction> list =transactionrepo.findByAccountnumberOrderByDate(accountnumber);

    int size = list.size();

    return size;
}
    @GetMapping("/balance")
    public double getbalance(@RequestParam String accountnumber)
    {
        User u=userrepo.findByaccountnumber(accountnumber);
        return u.getBalance();
    }

    @GetMapping("/totalbalance")
public double totalbalance()
{
    Double balance=userrepo.totalbalance();

    if(balance==null)
    {
        return 0;
    }

    return balance;
}

    @GetMapping("/totalusers")
public long totalusers()
{
    return userrepo.count();
}
@GetMapping("/totalaccounts")
public long totalaccounts()
{
    return userrepo.count();
}

    @PatchMapping("/update")
    public User updateuser(@RequestParam String newdetail,@RequestParam String accountnumber,@RequestParam int choice)
    {
        User u=userrepo.findByaccountnumber(accountnumber);
        
        switch(choice)
        {
            case 1:u.setName(newdetail);
                     break;
            case 2:u.setMobilenumber(newdetail);
                    break;
            // case 3:u.setPassword(newdetail);
            //         break;
            case 4:u.setEmail(newdetail);
        }
        return userrepo.save(u);
    }

    @PatchMapping("/changepassword")
    public boolean changepassword(@RequestParam String accountnumber,@RequestParam String currentpassword,@RequestParam String newpassword)
    {
        User u=userrepo.findByaccountnumber(accountnumber);
        if(u==null)
        {
            return false;
        }
        if(!currentpassword.equals(u.getPassword()))
        {
            return false;
        }
        u.setPassword(newpassword);
        userrepo.save(u);
        return true;
    }

    @PatchMapping("/admindeposit")
public boolean admindeposit(@RequestParam String accountnumber,@RequestParam double amount)
{
    User u=userrepo.findByaccountnumber(accountnumber);

    if(u==null)
    {
        return false;
    }

    if(amount<=0)
    {
        return false;
    }

    double bal=u.getBalance();
    bal=bal+amount;

    u.setBalance(bal);
    userrepo.save(u);

    return true;
}

   @PatchMapping("/withdrawal")
public int withdrawal(@RequestParam String accountnumber,@RequestParam double balance,@RequestParam int pin){
    User u=userrepo.findByaccountnumber(accountnumber);

    if(u==null) return 1;

    double bal=u.getBalance();

    if(pin!=u.getPin()) return 0;

    if(balance>bal) return 1;

    if(balance<=0) return 1;

    bal=bal-balance;

    u.setBalance(bal);
    userrepo.save(u);

    Transaction t=new Transaction();

    t.setAccountnumber(accountnumber);
    t.setType("debit");
    t.setAmount(balance);
    t.setBalance(bal);
    t.setDescription("Money Withdrawal");
    t.setDate(java.time.LocalDate.now().toString());

    transactionrepo.save(t);

    return 2;
}
    @PatchMapping("/deposit")
public boolean deposit(@RequestParam String accountnumber,@RequestParam double balance,@RequestParam int pin){
    User u=userrepo.findByaccountnumber(accountnumber);

    if(u==null) return false;

    if(pin!=u.getPin()) return false;

    if(balance<=0) return false;

    double bal=u.getBalance();
    bal=bal+balance;

    u.setBalance(bal);
    userrepo.save(u);

    Transaction t=new Transaction();

    t.setAccountnumber(accountnumber);
    t.setType("credit");
    t.setAmount(balance);
    t.setBalance(bal);
    t.setDescription("Money Deposit");
    t.setDate(java.time.LocalDate.now().toString());

    transactionrepo.save(t);

    return true;
}

    @PatchMapping("/transfermoney")
public boolean transfermoney(@RequestParam String accountnumbersender,@RequestParam String accountnumberreceiver,@RequestParam int pin,@RequestParam double amount){
    User u=userrepo.findByaccountnumber(accountnumbersender);
    User receiver=userrepo.findByaccountnumber(accountnumberreceiver);

    if(u==null || receiver==null) return false;

    if(pin!=u.getPin()) return false;

    if(amount<=0) return false;

    if(amount>u.getBalance()) return false;

    if(accountnumbersender.equals(accountnumberreceiver)) return false;

    double remainingbalance=u.getBalance()-amount;

    u.setBalance(remainingbalance);
    userrepo.save(u);

    double amountadd=receiver.getBalance()+amount;

    receiver.setBalance(amountadd);
    userrepo.save(receiver);


    Transaction t1=new Transaction();

    t1.setAccountnumber(accountnumbersender);
    t1.setType("debit");
    t1.setAmount(amount);
    t1.setBalance(remainingbalance);
    t1.setDescription("Money Transfer");
    t1.setDate(java.time.LocalDate.now().toString());

    transactionrepo.save(t1);


    Transaction t2=new Transaction();

    t2.setAccountnumber(accountnumberreceiver);
    t2.setType("credit");
    t2.setAmount(amount);
    t2.setBalance(amountadd);
    t2.setDescription("Money Received");
    t2.setDate(java.time.LocalDate.now().toString());

    transactionrepo.save(t2);

    return true;
}

    @PatchMapping("/updatepin")
    public boolean updatepin(@RequestParam String accountnumber,@RequestParam int pin,@RequestParam int newpin)
    {
        User u=userrepo.findByaccountnumber(accountnumber);
        if(pin!=u.getPin())
        {
            return false;
        }
        u.setPin(newpin);
        userrepo.save(u);
        return true;
    }
}
