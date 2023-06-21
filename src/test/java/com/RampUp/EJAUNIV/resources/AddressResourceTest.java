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

import com.RampUp.EJAUNIV.entities.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.RampUp.EJAUNIV.entities.Address;
import com.RampUp.EJAUNIV.entities.Customer;
import com.RampUp.EJAUNIV.entities.enums.AddressType;
import com.RampUp.EJAUNIV.entities.enums.CustomerType;
import com.RampUp.EJAUNIV.services.AddressService;
import com.RampUp.EJAUNIV.services.CustomerService;
import com.RampUp.EJAUNIV.services.exceptions.DatabaseException;
import com.RampUp.EJAUNIV.services.exceptions.ResourceNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(AddressResource.class)
public class AddressResourceTest {	

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	AddressService addressService;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@MockBean
	CustomerService customerService;
	
	

	private Integer addressExistingId;
	private Integer addressNonExistingId;
	private Integer addressDependentId;
	private Address address;
	private Customer customer;

		
	@BeforeEach
		void setUp() throws Exception {
		
		addressExistingId = 1;
		addressNonExistingId = 10264;
		addressDependentId = 3;
			
		customer = new Customer(null,"Jorge","RG","Ativo","1000","141235",CustomerType.LEGALPERSON);
		address = new Address(addressExistingId, "Travessa Remanso", 43 , "Rio Vermelho", "Casa", "41950700", "Brazil", AddressType.HOMEADDRESS, customer, new Order());
	
		when(addressService.findById(eq(addressExistingId))).thenReturn(address);
		when(addressService.findById(eq(addressNonExistingId))).thenThrow(ResourceNotFoundException.class);
		
		when(addressService.findAll()).thenReturn(List.of(address));
		
		when(addressService.insert(any())).thenReturn(address);
		
		when(addressService.update(eq(addressExistingId), any())).thenReturn(address);
		when(addressService.update(eq(addressNonExistingId), any())).thenThrow(ResourceNotFoundException.class);
		
		doNothing().when(addressService).delete(eq(addressExistingId));
		doThrow(ResourceNotFoundException.class).when(addressService).delete(addressNonExistingId);
		doThrow(DatabaseException.class).when(addressService).delete(addressDependentId);
	}
	
	
	@Test
		public void findByIdShouldReturnProductWhenIdExists() throws Exception{
		
		 ResultActions result = mockMvc.perform(get("/addresses/{id}", addressExistingId).accept(MediaType.APPLICATION_JSON));
		 
		 result.andExpect(status().isOk());
	        result.andExpect(jsonPath("$.id").exists());
	        result.andExpect(jsonPath("$.street").exists());
	        result.andExpect(jsonPath("$.houseNumber").exists());
	        result.andExpect(jsonPath("$.neighborhood").exists());
	        result.andExpect(jsonPath("$.complement").exists());
	        result.andExpect(jsonPath("$.zipCode").exists());
	        result.andExpect(jsonPath("$.addressType").exists());
	        result.andExpect(jsonPath("$.client").exists());
	}
	
	@Test
	public void findByIdShouldReturnNotFoundWhenIdDoesNotExists() throws Exception{
	
	 ResultActions result = mockMvc.perform(get("/addresses/{id}", addressNonExistingId).accept(MediaType.APPLICATION_JSON));
	 
	 result.andExpect(status().isNotFound());
        
	}
	
	@Test
	public void deleteShouldReturnBadRequestWhenIdisDependent() throws Exception{
	
	 ResultActions result = mockMvc.perform(delete("/addresses/{id}", addressDependentId).accept(MediaType.APPLICATION_JSON));
	 
	 result.andExpect(status().isBadRequest());
	
	}

	@Test
	public void deleteShouldReturnNothingWhenIdExists() throws Exception{
		
		String json = objectMapper.writeValueAsString(address);
	
	 ResultActions result = mockMvc.perform(delete("/addresses/{id}", addressExistingId).accept(MediaType.APPLICATION_JSON));
	 
	 result.andExpect(status().isNoContent());
	
	}
	
	@Test
	public void deleteShouldReturnNotFoundWhenIdDoesNotExist() throws Exception{
		
		String json = objectMapper.writeValueAsString(address);
	
	 ResultActions result = mockMvc.perform(delete("/addresses/{id}", addressNonExistingId).accept(MediaType.APPLICATION_JSON));
	 
	 result.andExpect(status().isNotFound());
	
	}
	
	@Test
	public void insertShouldReturnAddressWasCreated() throws Exception{
		
		String json = objectMapper.writeValueAsString(address);
	
		ResultActions result = mockMvc.perform(post("/addresses").content(json)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));
	 
		result.andExpect(status().isCreated());
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.street").exists());
        result.andExpect(jsonPath("$.houseNumber").exists());
        result.andExpect(jsonPath("$.neighborhood").exists());
        result.andExpect(jsonPath("$.complement").exists());
        result.andExpect(jsonPath("$.zipCode").exists());
        result.andExpect(jsonPath("$.addressType").exists());
        result.andExpect(jsonPath("$.client").exists());
        
	
	}
	
	@Test
	public void updateShouldReturnAddressWhenIdExists() throws Exception{
		
		String json = objectMapper.writeValueAsString(address);
	
		ResultActions result = mockMvc.perform(put("/addresses/{id}", addressExistingId).content(json)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));
	 
		result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.street").exists());
        result.andExpect(jsonPath("$.houseNumber").exists());
        result.andExpect(jsonPath("$.neighborhood").exists());
        result.andExpect(jsonPath("$.complement").exists());
        result.andExpect(jsonPath("$.zipCode").exists());
        result.andExpect(jsonPath("$.addressType").exists());
        result.andExpect(jsonPath("$.client").exists());
	
	}
	
	@Test
	public void updateShouldReturnNotFoundWhenIdDoesNotExist() throws Exception{
		
		String json = objectMapper.writeValueAsString(address);
	
		ResultActions result = mockMvc.perform(put("/addresses/{id}", addressNonExistingId).content(json)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));
	 
		result.andExpect(status().isNotFound());
     	
	}
	
	@Test
	public void findAllShouldReturnList() throws Exception{
		
		String json = objectMapper.writeValueAsString(address);
	
		ResultActions result = mockMvc.perform(get("/addresses").content(json)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));
	 
		result.andExpect(status().isOk());
     	
	}
}
