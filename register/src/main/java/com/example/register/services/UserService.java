package com.example.register.services;

import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.time.LocalDate;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import com.example.register.models.User;
import com.example.register.repos.UserRepo;

@Service
public class UserService {

    @Autowired
    private UserRepo repo;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${portfolio.service.url}")
    private String portfolioBaseUrl;

    @Value("${stock.service.url}")
    private String stockExchangeBaseUrl;

    @Value("${trading.service.url}")
    private String tradingBaseUrl;

    public List<User> getAllUsers() {
        return repo.findAll();
    }

    public Optional<User> getUserById(int id) {
        return repo.findById(id);
    }

    @CircuitBreaker(name = "portfolioService", fallbackMethod = "registerFallback")
    public User register(User user) {
        // Check if username already exists
        if (repo.existsByUsername(user.getUsername())) {
            System.out.println("Username already exists");
            return null;
        }

        if(user.getBalance() > 100000) {
            System.out.println("Cannot register user with balance more than 100000");
            return null;
        }

        User new_user = repo.save(user);
        System.out.println("New user created with username " + user.getUsername());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("userId", new_user.getId());

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
        String response = "abc";
        String createPortfolioUrl = portfolioBaseUrl + "/create";
        System.out.println(createPortfolioUrl);
        try{
            response = restTemplate.postForObject(
                createPortfolioUrl, 
                request, 
                String.class
            );
        }catch(Exception e){
            System.out.println("Portfolio creation response: " + e.getMessage());
        }
        System.out.println(response);
        return new_user;
    }

    public User registerFallback(User user, Throwable t) {
        System.out.println("Portfolio service is down. Saving user without portfolio creation.");
        return repo.save(user);
    }

    public User login(String username, String password) {
        Optional<User> userOpt = repo.findByUsername(username);
        if (userOpt.isEmpty()) {
            System.out.println("User not found");
            return null;
        }
        
        User user = userOpt.get();
        if (user.getPassword().equals(password)) {
            return user;
        } else {
            System.out.println("Password is incorrect");
            return null;
        }
    }

    public User changePassword(String old_password, String new_password, Integer id) {
        User user = repo.findById(id).get();
        if(user.getPassword().equals(old_password)) {
            user.setPassword(new_password);
            return repo.save(user);
        }
        else {
            System.out.println("Old password is incorrect");
            return null;
        }
    }

    public User forgotPassword(String username, LocalDate dob, String new_password){
        User user = repo.findByUsername(username).get();
        if(user.getDob().equals(dob)){
            user.setPassword(new_password);
            System.out.println("password changed for user "+user.getUsername());
            return repo.save(user);
        }
        else{
            System.out.println("Date of birth is incorrect");
            return null;
        }
    }

    public Double updateUsersBalance(Integer userId, Double new_balance) {
        try {
            Optional<User> userOpt = repo.findById(userId);
            
            if (userOpt.isEmpty()) {
                System.out.println("User not found with id: " + userId);
                return null;
            }
            
            User user = userOpt.get();
            user.setBalance(new_balance);
            User updatedUser = repo.save(user);
            
            return updatedUser.getBalance();
        } catch (NumberFormatException e) {
            System.out.println("Invalid user id format: " + userId);
            return null;
        }
    }

    
}
