package com.application.customer;

import com.application.customer.api.customer.dao.CustomerDetailsRepo;
import com.application.customer.api.customer.dao.CustomerRepository;
import com.application.customer.api.customer.entity.Customer;
import com.application.customer.api.customer.entity.CustomerDetails;
import com.application.customer.api.customer.services.CustomerServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
@DisplayName("Customer Service CRUD Test")
public class DemoTest {

    @InjectMocks
    CustomerServiceImpl customerService;
    @Mock
    CustomerDetailsRepo customerDetailsRepo;

    @Mock
    CustomerRepository customerRepository;

    Long id;
    String customerName;
    Long siteId;
    String buildingName;
    String street;
    String pin;
    String location;
    String longitude;
    String latitude;

    @BeforeEach
    void beforeEach(){
        id = 1L;
        customerName= "ABCD";
        buildingName="Building01";
        siteId=101L;
        street="60th Street";
        pin= "768200";
        location="XYZ";
        longitude="12";
        latitude="13";
    }


    @DisplayName("Add Customer Data")
    @Test
    void testAddCustomerData_whenCustomerDetailsGiven_shouldReturnAddedDetails(){
        //Arrange
        CustomerDetails customerDetails = new CustomerDetails();

        Mockito.when(customerDetailsRepo.save(Mockito.any(CustomerDetails.class))).thenReturn(customerDetails);

        customerDetails.setId(id);
        customerDetails.setCustomerName(customerName);
        customerDetails.setPin(pin);
        customerDetails.setLocation(location);
        customerDetails.setLatitude(latitude);
        customerDetails.setLongitude(longitude);
        customerDetails.setStreet(street);
        customerDetails.setSiteId(siteId);
        customerDetails.setBuildingName(buildingName);

        //Act
        CustomerDetails actualCustomerDetails = customerService.addCustomerData(customerDetails);


        //Assert
        assertNotNull(actualCustomerDetails, "Returned object should not be null");
        assertEquals(customerDetails.getCustomerName(), actualCustomerDetails.getCustomerName(), "Customer Name should be same");
        Mockito.verify(customerDetailsRepo, Mockito.times(1))
                .save(Mockito.any(CustomerDetails.class));
    }

    @DisplayName("Get Customer Details")
    @Test
    void testGetCustomerDetails_whenCalled_shouldReturnListofCustomer(){
//        Arrange
        Customer customer1 = new Customer();
        Customer customer2 = new Customer();
        List<Customer> customerList = Arrays.asList(customer1, customer2);
        Mockito.when(customerRepository.findAll()).thenReturn(customerList);

//        Act
        List<Customer> actualResult = customerService.getCustomerDetails();

//        Assert
        Mockito.verify(customerRepository, Mockito.times(1)).findAll();
        assertNotNull(actualResult, "The result should not be null");
        assertTrue(actualResult.contains(customer1) && actualResult.contains(customer2));
    }

    @DisplayName("Create Customer")
    @Test
    void testCreateCustomer_whenCalled_shouldReturnNewCustomerDetails(){
        //arrange
        Customer customer = new Customer();
        Mockito.when(customerRepository.save(Mockito.any(Customer.class))).thenReturn(customer);

        //act
        Customer actualCustomer = customerService.createCustomer(customer);

        //assert
        assertNotNull(actualCustomer, "Customer Object should not be null");
        assertEquals(customer.getCustomerId(), actualCustomer.getCustomerId(), "Customer Id should be same");
        Mockito.verify(customerRepository, Mockito.times(1))
                .save(Mockito.any(Customer.class));
    }

    @DisplayName("Ascending_customerDetailsByOrderPrice")
    @Test
    void testGetCustomersSortedByOrderPrice_Ascending(){
        //arrange
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC,"orderPrice"));
        List<Customer> customersList = Arrays.asList(
                new Customer(1L, 1L, 100.0, LocalDateTime.now(), LocalDateTime.now()),
                new Customer(2L, 1L, 200.0, LocalDateTime.now(), LocalDateTime.now()),
                new Customer(3L, 1L, 300.0, LocalDateTime.now(), LocalDateTime.now())
        );
        Page<Customer> expectedPage = new PageImpl<>(customersList);

        Mockito.when(customerRepository.findAllByOrderByOrderPriceAsc(pageable)).thenReturn(expectedPage);


        //act
        Page<Customer> actualPage = customerService.getCustomersSortedByOrderPrice(pageable,"asc");

        //assert
        assertNotNull(actualPage);

        List<Customer> resultCustomers = actualPage.getContent();
        for (int i = 0; i < resultCustomers.size() - 1; i++) {
            assertTrue(resultCustomers.get(i).getOrderPrice() <= resultCustomers.get(i + 1).getOrderPrice(),
                    "Results are not sorted in ascending order");
        }
                assertEquals(expectedPage, actualPage, "Both pages should be same");
        Mockito.verify(customerRepository).findAllByOrderByOrderPriceAsc(pageable);
        Mockito.verify(customerRepository, Mockito.never()).findAllByOrderByOrderPriceDesc(Mockito.any(Pageable.class));

    }



    @DisplayName("Descending_customerDetailsByOrderPrice")
    @Test
    void testGetCustomersSortedByOrderPrice_Descending(){
        //arrange
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC,"orderPrice"));
        List<Customer> customersList = Arrays.asList(
                new Customer(1L, 1L, 300.0, LocalDateTime.now(), LocalDateTime.now()),
                new Customer(2L, 1L, 200.0, LocalDateTime.now(), LocalDateTime.now()),
                new Customer(3L, 1L, 100.0, LocalDateTime.now(), LocalDateTime.now())
        );
        Page<Customer> expectedPage = new PageImpl<>(customersList);

        Mockito.when(customerRepository.findAllByOrderByOrderPriceDesc(pageable)).thenReturn(expectedPage);


        //act
        Page<Customer> actualPage = customerService.getCustomersSortedByOrderPrice(pageable,"desc");

        //assert
        assertNotNull(actualPage);

        List<Customer> resultCustomers = actualPage.getContent();
        for (int i = 0; i < resultCustomers.size() - 1; i++) {
            assertTrue(resultCustomers.get(i).getOrderPrice() >= resultCustomers.get(i + 1).getOrderPrice(),
                    "Results are not sorted in descending order");
        }
        assertEquals(expectedPage, actualPage, "Both pages should be same");
        Mockito.verify(customerRepository).findAllByOrderByOrderPriceDesc(pageable);
        Mockito.verify(customerRepository, Mockito.never()).findAllByOrderByOrderPriceAsc(Mockito.any(Pageable.class));

    }

    @DisplayName("Delete CustomerDetails By Id")
    @Test
    void testDeleteCustomerById_Success(){
        // Arrange
        Long customerId = 1L;
        // Mock the behavior of findById
        Mockito.when(customerDetailsRepo.findById(customerId)).thenReturn(java.util.Optional.of(new CustomerDetails()));

        // Act
        String result = customerService.deleteCustomerDataById(customerId);

        // Assert
        assertEquals("Customer Data deleted successfully !", result);
        Mockito.verify(customerDetailsRepo).findById(customerId);
        Mockito.verify(customerDetailsRepo).deleteById(customerId);
    }
}
