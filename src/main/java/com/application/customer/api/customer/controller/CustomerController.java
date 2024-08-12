package com.application.customer.api.customer.controller;

import com.application.customer.api.customer.entity.Customer;
import com.application.customer.api.customer.entity.CustomerDetails;
import com.application.customer.api.customer.services.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class CustomerController {

    private static final Logger LOGGER = LogManager.getLogger(CustomerController.class);

    @Autowired
    CustomerService customerService;
    @GetMapping("/test")
    public String test(){
        return "Hello WOrld test";
    }

    @GetMapping("/customer")
    public ResponseEntity<List<Customer>> getCustomerDetails(){
        List<Customer> customerDetails = customerService.getCustomerDetails();

        return new ResponseEntity<>(customerDetails, HttpStatus.OK);
    }

    @GetMapping("/customer/orderPrice")
    public ResponseEntity<Page<Customer>> getSortedCustomers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "DESC") String sort
    ) {

        Sort.Direction sortDirection = sort.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(sortDirection, "orderPrice"));

        Page<Customer> customers = customerService.getCustomersSortedByOrderPrice(pageable, sort);
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

    @PostMapping("/customer")
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer){
        Customer newCustomer = customerService.createCustomer(customer);

        return new ResponseEntity<>(newCustomer, HttpStatus.CREATED);
    }

    @PostMapping("/customer-details")
    public ResponseEntity<CustomerDetails> addCustomerData(@RequestBody CustomerDetails customer_data){
        CustomerDetails persistCustomerData = customerService.addCustomerData(customer_data);

        return new ResponseEntity<CustomerDetails>(customer_data, HttpStatus.CREATED);
    }

    @GetMapping(value = "/customer-details/{id}")
    public ResponseEntity<CustomerDetails> getCustomerDataById(@PathVariable Long id){

        CustomerDetails customerDetails = new CustomerDetails();
        try{
            customerDetails = customerService.getCustomerDataById(id);
        }catch(RuntimeException e){
            throw new RuntimeException("Runtime exception from controler");
        }
        return new ResponseEntity<>(customerDetails, HttpStatus.OK);
    }

    @PutMapping(value = "/customer-details")
    public ResponseEntity<CustomerDetails> updateCustomerData(@RequestBody CustomerDetails updatedCustomerData){
        return new ResponseEntity<>(customerService.updateCustomerData(updatedCustomerData), HttpStatus.OK);
    }

    @DeleteMapping(value = "/customer-details/{id}")
    public ResponseEntity<String> deleteCustomerDataById(@PathVariable Long id){
        return new ResponseEntity<>(customerService.deleteCustomerDataById(id), HttpStatus.OK);
    }


}
