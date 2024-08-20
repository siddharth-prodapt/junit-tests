package com.application.customer.api.customer.controller;

import com.application.customer.api.customer.entity.Customer;
import net.bytebuddy.description.method.MethodDescription;
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
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.jaxb.SpringDataJaxb;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;
import org.springframework.util.Assert;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// look for main application class and use it to start context with this application
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT) // with mocked -> only beans related to web layer
@TestPropertySource(locations = "/application-test.properties")
@DisplayName("Controller Integration Test")
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


    @Test
    @DisplayName("Get Customer Details")
    void testGetCustomerDetails_shouldReturnListOfCustomerDetails() throws JSONException {
        //arrange
        Customer customer = new Customer();

        HttpHeaders headers  = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        HttpEntity<String> request = new HttpEntity<>(null, headers);

        //act

        ResponseEntity<List<Customer>> fetchedCustomerDetailsList = testRestTemplate.exchange("/api/v1/customer",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Customer>>(){}
        );


        //assert
        Assertions.assertEquals(HttpStatus.OK,
                fetchedCustomerDetailsList.getStatusCode(),
                "http status must be 200 as Ok success");
    }


    @Test
    @DisplayName("Pageable Customer Data")
    void testSortedCustomer_shouldReturnPageableCustomerData(){

        String uri = "http://localhost:8080/api/v1/customer/orderPrice";
        UriComponents builder =  UriComponentsBuilder.fromHttpUrl(uri)
                .queryParam("page", "0")
                .queryParam("size", "10")
                .queryParam("sort", "ASC").build();

        System.out.println("URI Builder String: "+builder.toUriString());

        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        header.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        HttpEntity<String> httpEntity = new HttpEntity<>(null, header);

        ResponseEntity<Page<Customer>> pageCustomerDataEntity = testRestTemplate.exchange(builder.toUriString(),
                HttpMethod.GET,
                httpEntity,
                new ParameterizedTypeReference<Page<Customer>>(){});

        Assertions.assertEquals(HttpStatus.OK, pageCustomerDataEntity.getStatusCode(), "Status code must be 200 OK");

    }
}
