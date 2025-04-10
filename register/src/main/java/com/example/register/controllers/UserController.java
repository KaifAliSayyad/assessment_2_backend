package com.example.register.controllers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.register.models.User;
import com.example.register.services.UserService;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
// @RequestMapping("/register")
// @CrossOrigin(origins= "http://localhost:3000")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }
    
    @GetMapping("/users/{id}")
    public Optional<User> getUserById(@PathVariable Integer id) {
        return userService.getUserById(id);
    }
    
    
    
    @PostMapping("/signup")
    public ResponseEntity<User> signup(@RequestBody Map<String, String> request) {
        try {
            User user = new User();
            user.setName(request.get("name"));
            user.setUsername(request.get("username"));
            user.setPassword(request.get("password"));
            
            // Parse date from "dd-MM-yyyy" format
            DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate dob = null;
            try{
                dob = LocalDate.parse(request.get("dob"), formatter1);
                dob = LocalDate.parse(request.get("dob"), formatter2);
            }catch(Exception e){
                System.out.println("Error parsing birth date");
                e.printStackTrace();
            }
            user.setDob(dob);
            
            // Parse balance from string to double
            double balance = Double.parseDouble(request.get("balance"));
            user.setBalance(balance);

            User newUser = userService.register(user);
            if (newUser == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(newUser, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");
        
        User user = userService.login(username, password);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping("/changePassword/{id}")
    public ResponseEntity<User> changePassword(
            @PathVariable Integer id,
            @RequestBody Map<String, String> request) {
        String oldPassword = request.get("old_password");
        String newPassword = request.get("new_password");
        
        User user = userService.changePassword(oldPassword, newPassword, id);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping("/forgotPassword/{username}")
    public ResponseEntity<User> forgotPassword(
            @PathVariable String username,
            @RequestBody Map<String, String> request) {
        try {
            // Parse date from "dd-MM-yyyy" format
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate dob = LocalDate.parse(request.get("dob"), formatter);
            String newPassword = request.get("new_password");
            
            User user = userService.forgotPassword(username, dob, newPassword);
            if (user == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/balance/{id}")
    public ResponseEntity<String> updateUsersBalance(@PathVariable Integer id, @RequestBody Double new_balance) {
        Double balance = userService.updateUsersBalance(id, new_balance);
        if (balance == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(balance.toString(), HttpStatus.OK);
    }

    @GetMapping("/balance/{user_id}")
    public Double getBalance(@PathVariable Integer user_id) {
        System.out.println("123 : "+user_id);
        return userService.getUserById(user_id).get().getBalance();
    }
    
}
