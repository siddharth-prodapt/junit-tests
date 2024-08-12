package com.application.customer;

import com.application.customer.api.customer.controller.CustomerController;
import jdk.jfr.Experimental;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
//@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = CustomerController.class)
class CustomerApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void contextLoads() {
	}


//	@Test
//	void testTestHelloWorld_shouldReturnHelloWorld() throws Exception {
//		mockMvc.perform(MockMvcRequestBuilders.get("/test"))
//				.andExpect(MockMvcResultMatchers.status().isOk())
//				.andExpect(MockMvcResultMatchers.content().string("Hello WOrld test"));
//	}

}
