package com.application.customer.api.customer.controller;

import com.application.customer.api.customer.entity.Customer;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;

import java.util.Arrays;

// look for main application class and use it to start context with this application
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT) // with mocked -> only beans related to web layer
@TestPropertySource(locations = "/application-test.properties")
public class CustomerControllerIntegrationTest {

    @Value("${server.port}")
    private int localServerPort;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    @DisplayName("Create User")
    void testCreateCustomer_whenValidDetailsProvided_returnsCustomerData() throws JSONException {
        //arrange
        Customer customer = new Customer();

        JSONObject customerRequestJson = new JSONObject();
        customerRequestJson.put("orderId", "101");
        customerRequestJson.put("processId", "201");
        customerRequestJson.put("orderPrice", "699");
        customerRequestJson.put("createdTimestamp", "2024-08-13T10:30:00");
        customerRequestJson.put("lastModifiedTimestamp", "2024-07-16T10:30:00");

        HttpHeaders headers  = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        HttpEntity<String> request = new HttpEntity<>(customerRequestJson.toString(), headers);

        //act
        ResponseEntity<Customer> createdCustomerDataEntity = testRestTemplate.postForEntity("/api/v1/customer", request, Customer.class);

        Customer createdCustomer = createdCustomerDataEntity.getBody();

        //assert
        Assertions.assertEquals(HttpStatus.CREATED, createdCustomerDataEntity.getStatusCode(), "http status must be 201 as created");
    }


}
