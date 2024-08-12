package com.application.customer.api.customer.dao;

import com.application.customer.api.customer.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    // Method to fetch customers sorted by orderPrice
    Page<Customer> findAllByOrderByOrderPriceAsc(Pageable pageable);

    Page<Customer> findAllByOrderByOrderPriceDesc(Pageable pageable);
}
