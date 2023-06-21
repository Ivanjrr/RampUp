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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.RampUp.EJAUNIV.entities.ProductOffering;
import com.RampUp.EJAUNIV.entities.TimePeriod;
import com.RampUp.EJAUNIV.entities.enums.POState;
import com.RampUp.EJAUNIV.services.ProductOfferingService;
import com.RampUp.EJAUNIV.services.exceptions.DatabaseException;
import com.RampUp.EJAUNIV.services.exceptions.ResourceNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(ProductOfferingResource.class)
public class ProductOfferingResourcesTest {	

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	ProductOfferingService productOfferingService;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	

	private Integer productOfferingExistingId;
	private Integer productOfferingNonExistingId;
	private Integer productOfferingDependentId;
	private ProductOffering productOffering;
	private TimePeriod timePeriod;

		
	@BeforeEach
		void setUp() throws Exception {
		
		productOfferingExistingId = 1;
		productOfferingNonExistingId = 10264;
		productOfferingDependentId = 3;
			
		productOffering = new ProductOffering(productOfferingExistingId, "TV", true , new TimePeriod(sdf.parse("22/09/2022"), sdf.parse("23/09/2022"), sdf.parse("24/09/2022"), productOffering), POState.TECHNICAL);
	
		when(productOfferingService.findById(eq(productOfferingExistingId))).thenReturn(productOffering);
		when(productOfferingService.findById(eq(productOfferingNonExistingId))).thenThrow(ResourceNotFoundException.class);
		
		when(productOfferingService.findAll()).thenReturn(List.of(productOffering));
		
		when(productOfferingService.insert(any())).thenReturn(productOffering);
		
		when(productOfferingService.update(eq(productOfferingExistingId), any())).thenReturn(productOffering);
		when(productOfferingService.update(eq(productOfferingNonExistingId), any())).thenThrow(ResourceNotFoundException.class);
		
		doNothing().when(productOfferingService).delete(eq(productOfferingExistingId));
		doThrow(ResourceNotFoundException.class).when(productOfferingService).delete(productOfferingNonExistingId);
		doThrow(DatabaseException.class).when(productOfferingService).delete(productOfferingDependentId);
	}
	
	
	@Test
		public void findByIdShouldReturnProductWhenIdExists() throws Exception{
		
		 ResultActions result = mockMvc.perform(get("/productOfferings/{id}", productOfferingExistingId).accept(MediaType.APPLICATION_JSON));
		 
		 result.andExpect(status().isOk());
	        result.andExpect(jsonPath("$.id").exists());
	        result.andExpect(jsonPath("$.name").exists());
	        result.andExpect(jsonPath("$.sellIndicator").exists());
	        result.andExpect(jsonPath("$.validFor").exists());
	        result.andExpect(jsonPath("$.state").exists());
	}
	
	@Test
	public void findByIdShouldReturnNotFoundWhenIdDoesNotExists() throws Exception{
	
	 ResultActions result = mockMvc.perform(get("/productOfferings/{id}", productOfferingNonExistingId).accept(MediaType.APPLICATION_JSON));
	 
	 result.andExpect(status().isNotFound());
        
	}
	
	@Test
	public void deleteShouldReturnBadRequestWhenIdisDependent() throws Exception{
	
	 ResultActions result = mockMvc.perform(delete("/productOfferings/{id}", productOfferingDependentId).accept(MediaType.APPLICATION_JSON));
	 
	 result.andExpect(status().isBadRequest());
	
	}

	@Test
	public void deleteShouldReturnNothingWhenIdExists() throws Exception{
		
		String json = objectMapper.writeValueAsString(productOffering);
	
	 ResultActions result = mockMvc.perform(delete("/productOfferings/{id}", productOfferingExistingId).accept(MediaType.APPLICATION_JSON));
	 
	 result.andExpect(status().isNoContent());
	
	}
	
	@Test
	public void deleteShouldReturnNotFoundWhenIdDoesNotExist() throws Exception{
		
		String json = objectMapper.writeValueAsString(productOffering);
	
	 ResultActions result = mockMvc.perform(delete("/productOfferings/{id}", productOfferingNonExistingId).accept(MediaType.APPLICATION_JSON));
	 
	 result.andExpect(status().isNotFound());
	
	}
	
	@Test
	public void insertShouldReturnProductOfferingWasCreated() throws Exception{
		
		String json = objectMapper.writeValueAsString(productOffering);
	
		ResultActions result = mockMvc.perform(post("/productOfferings").content(json)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));
	 
		 result.andExpect(status().isCreated());
	        result.andExpect(jsonPath("$.id").exists());
	        result.andExpect(jsonPath("$.name").exists());
	        result.andExpect(jsonPath("$.sellIndicator").exists());
	        result.andExpect(jsonPath("$.validFor").exists());
	        result.andExpect(jsonPath("$.state").exists());
	
	}
	
	@Test
	public void updateShouldReturnProductOfferingWhenIdExists() throws Exception{
		
		String json = objectMapper.writeValueAsString(productOffering);
	
		ResultActions result = mockMvc.perform(put("/productOfferings/{id}", productOfferingExistingId).content(json)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));
	 
		 result.andExpect(status().isOk());
	        result.andExpect(jsonPath("$.id").exists());
	        result.andExpect(jsonPath("$.name").exists());
	        result.andExpect(jsonPath("$.sellIndicator").exists());
	        result.andExpect(jsonPath("$.validFor").exists());
	        result.andExpect(jsonPath("$.state").exists());
	
	}
	
	@Test
	public void updateShouldReturnNotFoundWhenIdDoesNotExist() throws Exception{
		
		String json = objectMapper.writeValueAsString(productOffering);
	
		ResultActions result = mockMvc.perform(put("/productOfferings/{id}", productOfferingNonExistingId).content(json)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));
	 
		result.andExpect(status().isNotFound());
     	
	}
	
	@Test
	public void findAllShouldReturnList() throws Exception{
		
		String json = objectMapper.writeValueAsString(productOffering);
	
		ResultActions result = mockMvc.perform(get("/productOfferings").content(json)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));
	 
		result.andExpect(status().isOk());
     	
	}
}
