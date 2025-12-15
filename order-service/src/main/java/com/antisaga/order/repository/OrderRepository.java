package com.antisaga.order.repository;

import com.antisaga.order.entity.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<PurchaseOrder, UUID> {
}
