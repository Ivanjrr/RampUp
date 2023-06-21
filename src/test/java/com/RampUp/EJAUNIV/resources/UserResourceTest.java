package com.RampUp.EJAUNIV.resources;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import com.RampUp.EJAUNIV.entities.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.RampUp.EJAUNIV.entities.User;
import com.RampUp.EJAUNIV.services.UserService;
import com.RampUp.EJAUNIV.services.exceptions.DatabaseException;
import com.RampUp.EJAUNIV.services.exceptions.ResourceNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;


@WebMvcTest(UserResource.class)
public class UserResourceTest {	

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	UserService userService;
	
	@Autowired
	private ObjectMapper objectMapper;;
	
	

	private Integer userExistingId;
	private Integer userNonExistingId;
	private Integer userDependentId;
	private User user;

		
	@BeforeEach
		void setUp() throws Exception {
		
		userExistingId = 1;
		userNonExistingId = 10264;
		userDependentId = 3;
			
		user = new User(userExistingId, "Gabriel@gmail.com", "141234", new Customer());
	
		when(userService.findById(eq(userExistingId))).thenReturn(user);
		when(userService.findById(eq(userNonExistingId))).thenThrow(ResourceNotFoundException.class);
		
		when(userService.findAll()).thenReturn(List.of(user));
		
		when(userService.insert(any())).thenReturn(user);
		
		when(userService.update(eq(userExistingId), any())).thenReturn(user);
		when(userService.update(eq(userNonExistingId), any())).thenThrow(ResourceNotFoundException.class);
		
		doNothing().when(userService).delete(eq(userExistingId));
		doThrow(ResourceNotFoundException.class).when(userService).delete(userNonExistingId);
		doThrow(DatabaseException.class).when(userService).delete(userDependentId);
	}
	
	
	@Test
		public void findByIdShouldReturnProductWhenIdExists() throws Exception{
		
		 ResultActions result = mockMvc.perform(get("/users/{id}", userExistingId).accept(MediaType.APPLICATION_JSON));
		 
		 result.andExpect(status().isOk());
	        result.andExpect(jsonPath("$.id").exists());
	        result.andExpect(jsonPath("$.email").exists());
	        result.andExpect(jsonPath("$.password").exists());
	}
	
	@Test
	public void findByIdShouldReturnNotFoundWhenIdDoesNotExists() throws Exception{
	
	 ResultActions result = mockMvc.perform(get("/users/{id}", userNonExistingId).accept(MediaType.APPLICATION_JSON));
	 
	 result.andExpect(status().isNotFound());
        
	}
	
	@Test
	public void deleteShouldReturnBadRequestWhenIdisDependent() throws Exception{
	
	 ResultActions result = mockMvc.perform(delete("/users/{id}", userDependentId).accept(MediaType.APPLICATION_JSON));
	 
	 result.andExpect(status().isBadRequest());
	
	}

	@Test
	public void deleteShouldReturnNothingWhenIdExists() throws Exception{
		
		String json = objectMapper.writeValueAsString(user);
	
	 ResultActions result = mockMvc.perform(delete("/users/{id}", userExistingId).accept(MediaType.APPLICATION_JSON));
	 
	 result.andExpect(status().isNoContent());
	
	}
	
	@Test
	public void deleteShouldReturnNotFoundWhenIdDoesNotExist() throws Exception{
		
		String json = objectMapper.writeValueAsString(user);
	
	 ResultActions result = mockMvc.perform(delete("/users/{id}", userNonExistingId).accept(MediaType.APPLICATION_JSON));
	 
	 result.andExpect(status().isNotFound());
	
	}
	
	@Test
	public void insertShouldReturnUserWasCreated() throws Exception{
		
		String json = objectMapper.writeValueAsString(user);
	
		ResultActions result = mockMvc.perform(post("/users").content(json)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));
	 
		result.andExpect(status().isCreated());
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.email").exists());
        result.andExpect(jsonPath("$.password").exists());
	
	}
	
	@Test
	public void updateShouldReturnUserWhenIdExists() throws Exception{
		
		String json = objectMapper.writeValueAsString(user);
	
		ResultActions result = mockMvc.perform(put("/users/{id}", userExistingId).content(json)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));
	 
		result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.email").exists());
        result.andExpect(jsonPath("$.password").exists());
	
	}
	
	@Test
	public void updateShouldReturnNotFoundWhenIdDoesNotExist() throws Exception{
		
		String json = objectMapper.writeValueAsString(user);
	
		ResultActions result = mockMvc.perform(put("/users/{id}", userNonExistingId).content(json)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));
	 
		result.andExpect(status().isNotFound());
     	
	}
	
	@Test
	public void findAllShouldReturnList() throws Exception{
		
		String json = objectMapper.writeValueAsString(user);
	
		ResultActions result = mockMvc.perform(get("/users").content(json)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));
	 
		result.andExpect(status().isOk());
     	
	}
}

		
	
