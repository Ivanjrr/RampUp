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

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.List;

import com.RampUp.EJAUNIV.entities.Address;
import com.RampUp.EJAUNIV.entities.BankSlipPayment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.RampUp.EJAUNIV.entities.Customer;
import com.RampUp.EJAUNIV.entities.Order;
import com.RampUp.EJAUNIV.entities.enums.CustomerType;
import com.RampUp.EJAUNIV.services.CustomerService;
import com.RampUp.EJAUNIV.services.OrderService;
import com.RampUp.EJAUNIV.services.exceptions.DatabaseException;
import com.RampUp.EJAUNIV.services.exceptions.ResourceNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(OrderResource.class)
public class OrderResourceTest {	

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	OrderService orderService;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@MockBean
	CustomerService customerService;
	
	

	private Integer orderExistingId;
	private Integer orderNonExistingId;
	private Integer orderDependentId;
	private Order order;
	private Customer customer;

	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	@BeforeEach
		void setUp() throws Exception {
		
		orderExistingId = 1;
		orderNonExistingId = 10264;
		orderDependentId = 3;
			
		customer = new Customer(orderExistingId,"Jorge","RG","Ativo","1000","141235",CustomerType.LEGALPERSON);
		order = new Order(null, sdf.parse("25/10/2022"), new Customer(null, "Jorge", "RG", "Ativo", "1000", "141235", CustomerType.LEGALPERSON), new BankSlipPayment(), new Address());
	
		when(orderService.findById(eq(orderExistingId))).thenReturn(order);
		when(orderService.findById(eq(orderNonExistingId))).thenThrow(ResourceNotFoundException.class);
		
		when(orderService.findAll()).thenReturn(List.of(order));
		
		when(orderService.insert(any())).thenReturn(order);
		
		when(orderService.update(eq(orderExistingId), any())).thenReturn(order);
		when(orderService.update(eq(orderNonExistingId), any())).thenThrow(ResourceNotFoundException.class);
		
		doNothing().when(orderService).delete(eq(orderExistingId));
		doThrow(ResourceNotFoundException.class).when(orderService).delete(orderNonExistingId);
		doThrow(DatabaseException.class).when(orderService).delete(orderDependentId);
	}
	
	
	@Test
		public void findByIdShouldReturnProductWhenIdExists() throws Exception{
		
		 ResultActions result = mockMvc.perform(get("/orders/{id}", orderExistingId).accept(MediaType.APPLICATION_JSON));
		 
		 result.andExpect(status().isOk());
	        result.andExpect(jsonPath("$.id").exists());
	        result.andExpect(jsonPath("$.instant").exists());
	        result.andExpect(jsonPath("$.client").exists());
	}
	
	@Test
	public void findByIdShouldReturnNotFoundWhenIdDoesNotExists() throws Exception{
	
	 ResultActions result = mockMvc.perform(get("/orders/{id}", orderNonExistingId).accept(MediaType.APPLICATION_JSON));
	 
	 result.andExpect(status().isNotFound());
        
	}
	
	@Test
	public void deleteShouldReturnBadRequestWhenIdisDependent() throws Exception{
	
	 ResultActions result = mockMvc.perform(delete("/orders/{id}", orderDependentId).accept(MediaType.APPLICATION_JSON));
	 
	 result.andExpect(status().isBadRequest());
	
	}

	@Test
	public void deleteShouldReturnNothingWhenIdExists() throws Exception{
		
		String json = objectMapper.writeValueAsString(order);
	
	 ResultActions result = mockMvc.perform(delete("/orders/{id}", orderExistingId).accept(MediaType.APPLICATION_JSON));
	 
	 result.andExpect(status().isNoContent());
	
	}
	
	@Test
	public void deleteShouldReturnNotFoundWhenIdDoesNotExist() throws Exception{
		
		String json = objectMapper.writeValueAsString(order);
	
	 ResultActions result = mockMvc.perform(delete("/orders/{id}", orderNonExistingId).accept(MediaType.APPLICATION_JSON));
	 
	 result.andExpect(status().isNotFound());
	
	}
	
	@Test
	public void insertShouldReturnOrderWasCreated() throws Exception{
		
		String json = objectMapper.writeValueAsString(order);
	
		ResultActions result = mockMvc.perform(post("/orders").content(json)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));
	 
		result.andExpect(status().isCreated());
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.instant").exists());
        result.andExpect(jsonPath("$.client").exists());
	
	}
	
	@Test
	public void updateShouldReturnOrderWhenIdExists() throws Exception{
		
		String json = objectMapper.writeValueAsString(order);
	
		ResultActions result = mockMvc.perform(put("/orders/{id}", orderExistingId).content(json)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));
	 
		result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.instant").exists());
        result.andExpect(jsonPath("$.client").exists());
	
	}
	
	@Test
	public void updateShouldReturnNotFoundWhenIdDoesNotExist() throws Exception{
		
		String json = objectMapper.writeValueAsString(order);
	
		ResultActions result = mockMvc.perform(put("/orders/{id}", orderNonExistingId).content(json)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));
	 
		result.andExpect(status().isNotFound());
     	
	}
	
	@Test
	public void findAllShouldReturnList() throws Exception{
		
		String json = objectMapper.writeValueAsString(order);
	
		ResultActions result = mockMvc.perform(get("/orders").content(json)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));
	 
		result.andExpect(status().isOk());
     	
	}
}