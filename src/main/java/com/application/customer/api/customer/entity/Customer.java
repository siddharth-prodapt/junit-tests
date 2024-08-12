package com.application.customer.api.customer.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "customer_table")  // Replace with your actual table name
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customerid")
    private Long customerId;

    @Column(name = "orderid")
    private Long orderId;

    @Column(name = "processid")
    private Long processId;

    @Column(name = "orderprice")
    private Double orderPrice;

    @Column(name = "createdts")
    private LocalDateTime createdTimestamp;

    @Column(name = "lastmodifiedts")
    private LocalDateTime lastModifiedTimestamp;

    // Default constructor
    public Customer() {
        // No-arg constructor required by JPA
    }

    // Constructor with parameters (optional)
    public Customer(Long orderId, Long processId, Double orderPrice, LocalDateTime createdTimestamp, LocalDateTime lastModifiedTimestamp) {
        this.orderId = orderId;
        this.processId = processId;
        this.orderPrice = orderPrice;
        this.createdTimestamp = createdTimestamp;
        this.lastModifiedTimestamp = lastModifiedTimestamp;
    }
    @Override
    public String toString() {
        return "Customer{" +
                "customerId=" + customerId +
                ", orderId=" + orderId +
                ", processId=" + processId +
                ", orderPrice=" + orderPrice +
                ", createdTimestamp=" + createdTimestamp +
                ", lastModifiedTimestamp=" + lastModifiedTimestamp +
                '}';
    }
}
