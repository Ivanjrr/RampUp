package com.RampUp.EJAUNIV.resources;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.RampUp.EJAUNIV.entities.Customer;
import com.RampUp.EJAUNIV.entities.enums.CustomerType;
import com.RampUp.EJAUNIV.services.CustomerService;
import com.RampUp.EJAUNIV.services.exceptions.DatabaseException;
import com.RampUp.EJAUNIV.services.exceptions.ResourceNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(CustomerResource.class)
public class CustomerResourceTest {	

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	CustomerService customerService;
	
	@Autowired
	private ObjectMapper objectMapper;;
	
	

	private Integer customerExistingId;
	private Integer customerNonExistingId;
	private Integer customerDependentId;
	private Customer customer;

		
	@BeforeEach
		void setUp() throws Exception {
		
		customerExistingId = 1;
		customerNonExistingId = 10264;
		customerDependentId = 3;
			
		customer = new Customer(customerExistingId,"Jorge","RG","Ativo","1000","141235",CustomerType.LEGALPERSON);
	
		when(customerService.findById(eq(customerExistingId))).thenReturn(customer);
		when(customerService.findById(eq(customerNonExistingId))).thenThrow(ResourceNotFoundException.class);
		
		when(customerService.findAll()).thenReturn(List.of(customer));
		
		when(customerService.insert(any())).thenReturn(customer);
		
		when(customerService.update(eq(customerExistingId), any())).thenReturn(customer);
		when(customerService.update(eq(customerNonExistingId), any())).thenThrow(ResourceNotFoundException.class);
		
		doNothing().when(customerService).delete(eq(customerExistingId));
		doThrow(ResourceNotFoundException.class).when(customerService).delete(customerNonExistingId);
		doThrow(DatabaseException.class).when(customerService).delete(customerDependentId);
	}
	
	
	@Test
		public void findByIdShouldReturnProductWhenIdExists() throws Exception{
		
		 ResultActions result = mockMvc.perform(get("/customers/{id}", customerExistingId).accept(MediaType.APPLICATION_JSON));
		 
		 result.andExpect(status().isOk());
	        result.andExpect(jsonPath("$.id").exists());
	        result.andExpect(jsonPath("$.customerName").exists());
	        result.andExpect(jsonPath("$.documentNumber").exists());
	        result.andExpect(jsonPath("$.customerStatus").exists());
	        result.andExpect(jsonPath("$.creditScore").exists());
	        result.andExpect(jsonPath("$.password").exists());
	}
	
	@Test
	public void findByIdShouldReturnNotFoundWhenIdDoesNotExists() throws Exception{
	
	 ResultActions result = mockMvc.perform(get("/customers/{id}", customerNonExistingId).accept(MediaType.APPLICATION_JSON));
	 
	 result.andExpect(status().isNotFound());
        
	}
	
	@Test
	public void deleteShouldReturnBadRequestWhenIdisDependent() throws Exception{
	
	 ResultActions result = mockMvc.perform(delete("/customers/{id}", customerDependentId).accept(MediaType.APPLICATION_JSON));
	 
	 result.andExpect(status().isBadRequest());
	
	}

	@Test
	public void deleteShouldReturnNothingWhenIdExists() throws Exception{
		
		String json = objectMapper.writeValueAsString(customer);
	
	 ResultActions result = mockMvc.perform(delete("/customers/{id}", customerExistingId).accept(MediaType.APPLICATION_JSON));
	 
	 result.andExpect(status().isNoContent());
	
	}
	
	@Test
	public void deleteShouldReturnNotFoundWhenIdDoesNotExist() throws Exception{
		
		String json = objectMapper.writeValueAsString(customer);
	
	 ResultActions result = mockMvc.perform(delete("/customers/{id}", customerNonExistingId).accept(MediaType.APPLICATION_JSON));
	 
	 result.andExpect(status().isNotFound());
	
	}
	
	@Test
	public void insertShouldReturnCustomerWasCreated() throws Exception{
		
		String json = objectMapper.writeValueAsString(customer);
	
		ResultActions result = mockMvc.perform(post("/customers").content(json)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));
	 
		result.andExpect(status().isCreated());
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.customerName").exists());
        result.andExpect(jsonPath("$.documentNumber").exists());
        result.andExpect(jsonPath("$.customerStatus").exists());
        result.andExpect(jsonPath("$.creditScore").exists());
        result.andExpect(jsonPath("$.password").exists());
	
	}
	
	@Test
	public void updateShouldReturnCustomerWhenIdExists() throws Exception{
		
		String json = objectMapper.writeValueAsString(customer);
	
		ResultActions result = mockMvc.perform(put("/customers/{id}", customerExistingId).content(json)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));
	 
		result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.customerName").exists());
        result.andExpect(jsonPath("$.documentNumber").exists());
        result.andExpect(jsonPath("$.customerStatus").exists());
        result.andExpect(jsonPath("$.creditScore").exists());
        result.andExpect(jsonPath("$.password").exists());
	
	}
	
	@Test
	public void updateShouldReturnNotFoundWhenIdDoesNotExist() throws Exception{
		
		String json = objectMapper.writeValueAsString(customer);
	
		ResultActions result = mockMvc.perform(put("/customers/{id}", customerNonExistingId).content(json)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));
	 
		result.andExpect(status().isNotFound());
     	
	}
	
	@Test
	public void findAllShouldReturnList() throws Exception{
		
		String json = objectMapper.writeValueAsString(customer);
	
		ResultActions result = mockMvc.perform(get("/customers").content(json)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));
	 
		result.andExpect(status().isOk());
     	
	}
}