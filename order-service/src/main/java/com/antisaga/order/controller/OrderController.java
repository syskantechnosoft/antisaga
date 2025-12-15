package com.antisaga.order.controller;

import com.antisaga.common.dto.OrderRequestDTO;
import com.antisaga.order.entity.PurchaseOrder;
import com.antisaga.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    public PurchaseOrder createOrder(@RequestBody OrderRequestDTO orderRequest) {
        return orderService.createOrder(orderRequest);
    }
    
    @GetMapping("/all")
    public List<PurchaseOrder> getAllOrders() {
        return orderService.getAllOrders();
    }
}
