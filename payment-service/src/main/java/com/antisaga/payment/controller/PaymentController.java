package com.antisaga.payment.controller;

import com.antisaga.payment.entity.UserBalance;
import com.antisaga.payment.repository.UserBalanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private UserBalanceRepository userBalanceRepository;

    @GetMapping("/users/{userId}/balance")
    public UserBalance getUserBalance(@PathVariable Integer userId) {
        return userBalanceRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User balance not found"));
    }
}
