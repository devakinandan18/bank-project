package com.ex.project1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ex.project1.entity.Transaction;
import com.ex.project1.entity.Admin;
import com.ex.project1.entity.User;
import com.ex.project1.repo.Adminrepo;
import com.ex.project1.repo.Transactionrepo;
import com.ex.project1.repo.Userrepo;
import com.ex.project1.security.JwtService;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/admin")
public class Admincontroller {

     @Autowired
JwtService jwtService;

 @Autowired
Transactionrepo transactionrepo;

    
    @Autowired
    Adminrepo adminrepo;
     @Autowired
    Userrepo userrepo;
    
    public Admincontroller(Adminrepo adminrepo) {
        this.adminrepo = adminrepo;
    }
    
    @PostMapping
    public boolean addadmin(@RequestBody Admin admin)
    {
        Admin a=adminrepo.findBymobilenumber(admin.getMobilenumber());
        if(a==null)
        {
          adminrepo.save(admin);
          return true;
        }
        return false;
    }
    
    @GetMapping("/getadmin")
    public Admin getadmin(@RequestParam String mobilenumber)
    {
        Admin a=adminrepo.findBymobilenumber(mobilenumber);
        if(a!=null)
        {
            return a;
        }
        return null;
    }

    @GetMapping("/loginmobilenumber")
    public String loginmobilenumber(@RequestParam String mobilenumber,@RequestParam String password)
    {
        Admin a=adminrepo.findBymobilenumber(mobilenumber);
        if(a==null)
        {
            return "false";
        }
        if(!(password.equals(a.getPassword())))
        {
            return "false";
        }
        return jwtService.generateToken(a.getMobilenumber());
    }

    @PatchMapping("/deposit")
public boolean deposit(@RequestParam String accountnumber, @RequestParam double amount)
{
    User u = userrepo.findByaccountnumber(accountnumber);

    if(u == null)
    {
        return false;
    }

    if(amount <= 0)
    {
        return false;
    }

    double bal = u.getBalance();

    bal = bal + amount;

    u.setBalance(bal);

    userrepo.save(u);
    Transaction t=new Transaction();

    t.setAccountnumber(accountnumber);
    t.setType("credit");
    t.setAmount(amount);
    t.setBalance(bal);
    t.setDescription("Money deposit");
    t.setDate(java.time.LocalDate.now().toString());

    transactionrepo.save(t);

    return true;
}

    @PatchMapping("/updateadmin")
    public Admin updateadmin(@RequestParam String newdetail,@RequestParam String mobilenumber,@RequestParam int choice)
    {
        Admin u=adminrepo.findBymobilenumber(mobilenumber);
        
        switch(choice)
        {
            case 1:u.setAdminname(newdetail);
                     break;
            case 2:u.setMobilenumber(newdetail);
                    break;
            case 3:u.setPassword(newdetail);
                    break;
            case 4:u.setEmail(newdetail);
        }
        return adminrepo.save(u);
    }

    @DeleteMapping("/deleteadmin")
    public void deleteuser(@RequestParam String mobilenumber)
    {
        Admin a=adminrepo.findBymobilenumber(mobilenumber);
        if(a!=null)
        {
            adminrepo.delete(a);
        }
    }

    @PatchMapping("/accountstatus")
    public boolean accountstatus(@RequestParam String mobilenumber,@RequestParam String accountnumber,@RequestParam String status)
    {
        Admin a=adminrepo.findBymobilenumber(mobilenumber);
        User u=userrepo.findByaccountnumber(accountnumber);
        if((a.getMobilenumber().equals(mobilenumber)) && (u.getAccountnumber().equals(accountnumber)))
        {
            u.setStatus(status);
            userrepo.save(u);
            return true;
        }
        else
        {
            return false;
        }
    }
   
    @GetMapping("/checkstatus")
    public String checkstatus(@RequestParam String accountnumber)
    {
        User u=userrepo.findByaccountnumber(accountnumber);
        return u.getStatus();
    }

}
