package com.antisaga.order.entity;

import com.antisaga.common.enums.OrderStatus;
import com.antisaga.common.enums.PaymentStatus;
import com.antisaga.common.enums.InventoryStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "purchase_orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private Integer userId;
    
    @OneToMany(cascade = CascadeType.ALL)
    private List<OrderItem> items;
    
    private Double totalAmount;
    
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;
    @Enumerated(EnumType.STRING)
    private InventoryStatus inventoryStatus;
}
