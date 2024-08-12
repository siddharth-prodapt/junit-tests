package com.application.customer.api.customer.controller;

import com.application.customer.api.customer.dao.CustomerDetailsRepo;
import com.application.customer.api.customer.entity.CustomerDetails;
import com.application.customer.api.customer.services.CustomerService;
import com.fasterxml.jackson.databind.JsonSerializable;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = CustomerController.class)
public class TestCustomerController {


    @Autowired
    private MockMvc mockMvc;


    @Mock
    CustomerDetailsRepo customerDetailsRepo;
    @MockBean
    CustomerService customerService;

    @Test
    @DisplayName("create customer")
    void testAddCustomerData_success() throws Exception {
        //arrange
        CustomerDetails customerDetails = new CustomerDetails();

        customerDetails.setId(1L);
        customerDetails.setSiteId(11L);
        customerDetails.setBuildingName("asdf");
        customerDetails.setCustomerName("XYZ");
        customerDetails.setPin("123455");
        customerDetails.setLatitude("123");
        customerDetails.setLongitude("456");
        customerDetails.setStreet("160th Street");
        customerDetails.setLocation("asdf");

        Mockito.when(customerService.addCustomerData(Mockito.any(CustomerDetails.class)))
                .thenReturn(customerDetails);

        //act
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/customer-details")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content( "{" +
                        "  \"customerName\": \"David Wilson\"," +
                        "  \"buildingName\": \"Riverside Cottage\"," +
                        "  \"siteId\": 98765," +
                        "  \"street\": \"River Road\"," +
                        "  \"pin\": \"54321\"," +
                        "  \"location\": \"Riverside Village\"," +
                        "  \"longitude\": \"56.7890\"," +
                        "  \"latitude\": \"-12.3456\"" +
                        "}" ))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }


    @Test
    void testTestHelloWorld_shouldReturnHelloWorld() throws Exception {
        // Perform a GET request to the /test endpoint
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/test")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())  // Check if the status is 200 OK
                .andExpect(MockMvcResultMatchers.content().string("Hello WOrld test"));  // Check the response body
    }

    @Test
    void testgetCustomerDataById_success() throws Exception {

        int customerId = 1;
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/customer-details/{id}", customerId )
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
//                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(customerId));
    }

    @DisplayName("Update Customer Details")
    @Test
    void testUpdateCustomerData_success() throws Exception {
        //arrange
        CustomerDetails expectedCustomerData = new CustomerDetails();
        expectedCustomerData.setId(1L);
        expectedCustomerData.setCustomerName("X");
        expectedCustomerData.setLocation("Lcoation");
        expectedCustomerData.setPin("12345");
        expectedCustomerData.setLongitude("89.4321");
        expectedCustomerData.setLatitude("85.1232");
        expectedCustomerData.setBuildingName("building name");
        expectedCustomerData.setSiteId(2L);
        expectedCustomerData.setStreet("60th street");


        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/v1/customer-details")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content( new ObjectMapper().writeValueAsString(expectedCustomerData));

        Mockito.when(customerService.updateCustomerData(Mockito.any(CustomerDetails.class)))
                .thenReturn(expectedCustomerData);

        //act and assert
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        String responseBodyAsString = mvcResult.getResponse().getContentAsString();
        CustomerDetails actualCustomerDetails = new ObjectMapper().readValue(responseBodyAsString, CustomerDetails.class);

        //assert
        Assertions.assertEquals(mvcResult.getResponse().getStatus(), 200, "Status should be 200");
        Assertions.assertEquals(expectedCustomerData.getCustomerName(), actualCustomerDetails.getCustomerName(), "Both name must be same");
        Assertions.assertNotNull(actualCustomerDetails, "It must not be null");
        Assertions.assertNotNull(actualCustomerDetails.getId());
    }


}
