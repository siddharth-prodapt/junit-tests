package com.application.customer.api.customer.services;

import com.application.customer.api.customer.entity.Customer;

import com.application.customer.api.customer.entity.CustomerDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomerService {
    public List<Customer> getCustomerDetails();

    Customer createCustomer(Customer customer);

    Page getCustomersSortedByOrderPrice(Pageable pageable, String sort);

    CustomerDetails addCustomerData(CustomerDetails customerData);

    CustomerDetails getCustomerDataById(Long id);

    CustomerDetails updateCustomerData(CustomerDetails updatedCustomerData);

    String deleteCustomerDataById(Long id);
}
