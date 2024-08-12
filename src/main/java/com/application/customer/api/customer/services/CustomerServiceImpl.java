package com.application.customer.api.customer.services;

import com.application.customer.api.customer.dao.CustomerDetailsRepo;
import com.application.customer.api.customer.dao.CustomerRepository;
import com.application.customer.api.customer.entity.Customer;
import com.application.customer.api.customer.entity.CustomerDetails;
import lombok.extern.log4j.Log4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {
    private static final Logger LOGGER = LogManager.getLogger(CustomerServiceImpl.class);
    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    CustomerDetailsRepo customerDetailsRepo;
    @Override
    public List<Customer> getCustomerDetails() {
        List<Customer> customerList = new ArrayList<Customer>();
        try{
            customerList =  customerRepository.findAll();
        }catch(Exception e){
            throw new RuntimeException("Could not find all customer list");
        }

        return customerList;
    }

    @Override
    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public Page<Customer> getCustomersSortedByOrderPrice(Pageable pageable, String sort) {
        {
            if (sort.equalsIgnoreCase("asc")){
                return customerRepository.findAllByOrderByOrderPriceAsc(pageable);
            }
                return customerRepository.findAllByOrderByOrderPriceDesc(pageable);
            }
        }

    @Override
    public CustomerDetails addCustomerData(CustomerDetails customerData) {
        return customerDetailsRepo.save(customerData);
    }

    @Override
    public CustomerDetails getCustomerDataById(Long id) {
        CustomerDetails customerDetails = new CustomerDetails();
        try{
            customerDetails =  customerDetailsRepo.findById(id).orElseThrow(()-> new RuntimeException("No Element found"));
        }catch(IllegalArgumentException e){
            throw new IllegalArgumentException("Invalid Argument "+e.getMessage());
        }catch(RuntimeException ex){
            throw new RuntimeException("runtime exception");
        }

        return customerDetails;
    }

    @Override
    public CustomerDetails updateCustomerData(CustomerDetails updatedCustomerData) {
        CustomerDetails persistCustomerData = customerDetailsRepo.findById(updatedCustomerData.getId()).get();
        persistCustomerData = updatedCustomerData;
        return customerDetailsRepo.save(persistCustomerData);
    }

    @Override
    public String deleteCustomerDataById(Long id) {
        CustomerDetails customerData = customerDetailsRepo.findById(id).get();
        customerDetailsRepo.deleteById(id);
        return "Customer Data deleted successfully !";
    }
}

