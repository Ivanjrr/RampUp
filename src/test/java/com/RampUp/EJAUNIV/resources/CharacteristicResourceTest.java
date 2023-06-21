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

import com.RampUp.EJAUNIV.entities.Characteristic;
import com.RampUp.EJAUNIV.entities.TimePeriod;
import com.RampUp.EJAUNIV.entities.enums.CharacteristicType;
import com.RampUp.EJAUNIV.services.CharacteristicService;
import com.RampUp.EJAUNIV.services.exceptions.DatabaseException;
import com.RampUp.EJAUNIV.services.exceptions.ResourceNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(CharacteristicResource.class)
public class CharacteristicResourceTest {	

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	CharacteristicService characteristicService;
	
	@Autowired
	private ObjectMapper objectMapper;;
	
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

	private Integer characteristicExistingId;
	private Integer characteristicNonExistingId;
	private Integer characteristicDependentId;
	private Characteristic characteristic;

		
	@BeforeEach
		void setUp() throws Exception {
		
		characteristicExistingId = 1;
		characteristicNonExistingId = 10264;
		characteristicDependentId = 3;
			
		characteristic = new Characteristic(characteristicExistingId, "Eletronic", "5000", CharacteristicType.USER, new TimePeriod(sdf.parse("22/09/2022"), sdf.parse("23/09/2022"), sdf.parse("24/09/2022"), characteristic));
	
		when(characteristicService.findById(eq(characteristicExistingId))).thenReturn(characteristic);
		when(characteristicService.findById(eq(characteristicNonExistingId))).thenThrow(ResourceNotFoundException.class);
		
		when(characteristicService.findAll()).thenReturn(List.of(characteristic));
		
		when(characteristicService.insert(any())).thenReturn(characteristic);
		
		when(characteristicService.update(eq(characteristicExistingId), any())).thenReturn(characteristic);
		when(characteristicService.update(eq(characteristicNonExistingId), any())).thenThrow(ResourceNotFoundException.class);
		
		doNothing().when(characteristicService).delete(eq(characteristicExistingId));
		doThrow(ResourceNotFoundException.class).when(characteristicService).delete(characteristicNonExistingId);
		doThrow(DatabaseException.class).when(characteristicService).delete(characteristicDependentId);
	}
	
	
	@Test
		public void findByIdShouldReturnProductWhenIdExists() throws Exception{
		
		 ResultActions result = mockMvc.perform(get("/characteristics/{id}", characteristicExistingId).accept(MediaType.APPLICATION_JSON));
		 
		 result.andExpect(status().isOk());
	        result.andExpect(jsonPath("$.id").exists());
	        result.andExpect(jsonPath("$.name").exists());
	        result.andExpect(jsonPath("$.valueType").exists());
	        result.andExpect(jsonPath("$.type").exists());
	        result.andExpect(jsonPath("$.validFor").exists());
	}
	
	@Test
	public void findByIdShouldReturnNotFoundWhenIdDoesNotExists() throws Exception{
	
	 ResultActions result = mockMvc.perform(get("/characteristics/{id}", characteristicNonExistingId).accept(MediaType.APPLICATION_JSON));
	 
	 result.andExpect(status().isNotFound());
        
	}
	
	@Test
	public void deleteShouldReturnBadRequestWhenIdisDependent() throws Exception{
	
	 ResultActions result = mockMvc.perform(delete("/characteristics/{id}", characteristicDependentId).accept(MediaType.APPLICATION_JSON));
	 
	 result.andExpect(status().isBadRequest());
	
	}

	@Test
	public void deleteShouldReturnNothingWhenIdExists() throws Exception{
		
		String json = objectMapper.writeValueAsString(characteristic);
	
	 ResultActions result = mockMvc.perform(delete("/characteristics/{id}", characteristicExistingId).accept(MediaType.APPLICATION_JSON));
	 
	 result.andExpect(status().isNoContent());
	
	}
	
	@Test
	public void deleteShouldReturnNotFoundWhenIdDoesNotExist() throws Exception{
		
		String json = objectMapper.writeValueAsString(characteristic);
	
	 ResultActions result = mockMvc.perform(delete("/characteristics/{id}", characteristicNonExistingId).accept(MediaType.APPLICATION_JSON));
	 
	 result.andExpect(status().isNotFound());
	
	}
	
	@Test
	public void insertShouldReturnCharacteristicWasCreated() throws Exception{
		
		String json = objectMapper.writeValueAsString(characteristic);
	
		ResultActions result = mockMvc.perform(post("/characteristics").content(json)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));
	 
		result.andExpect(status().isCreated());
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.name").exists());
        result.andExpect(jsonPath("$.valueType").exists());
        result.andExpect(jsonPath("$.type").exists());
        result.andExpect(jsonPath("$.validFor").exists());
	
	}
	
	@Test
	public void updateShouldReturnCharacteristicWhenIdExists() throws Exception{
		
		String json = objectMapper.writeValueAsString(characteristic);
	
		ResultActions result = mockMvc.perform(put("/characteristics/{id}", characteristicExistingId).content(json)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));
	 
		result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.name").exists());
        result.andExpect(jsonPath("$.valueType").exists());
        result.andExpect(jsonPath("$.type").exists());
        result.andExpect(jsonPath("$.validFor").exists());
	
	}
	
	@Test
	public void updateShouldReturnNotFoundWhenIdDoesNotExist() throws Exception{
		
		String json = objectMapper.writeValueAsString(characteristic);
	
		ResultActions result = mockMvc.perform(put("/characteristics/{id}", characteristicNonExistingId).content(json)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));
	 
		result.andExpect(status().isNotFound());
     	
	}
	
	@Test
	public void findAllShouldReturnList() throws Exception{
		
		String json = objectMapper.writeValueAsString(characteristic);
	
		ResultActions result = mockMvc.perform(get("/characteristics").content(json)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));
	 
		result.andExpect(status().isOk());
     	
	}
}