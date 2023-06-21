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

import com.RampUp.EJAUNIV.entities.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.RampUp.EJAUNIV.entities.Role;
import com.RampUp.EJAUNIV.entities.User;
import com.RampUp.EJAUNIV.entities.enums.Authorities;
import com.RampUp.EJAUNIV.services.RoleService;
import com.RampUp.EJAUNIV.services.UserService;
import com.RampUp.EJAUNIV.services.exceptions.DatabaseException;
import com.RampUp.EJAUNIV.services.exceptions.ResourceNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(RoleResource.class)
public class RoleResourceTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	RoleService roleService;
	
	@MockBean
	UserService userService;

	@Autowired
	private ObjectMapper objectMapper;;

	private Integer roleExistingId;
	private Integer roleNonExistingId;
	private Integer roleDependentId;
	private Role role;
	private User user;

	@BeforeEach
	void setUp() throws Exception {

		roleExistingId = 1;
		roleNonExistingId = 10264;
		roleDependentId = 3;

		user = new User(roleExistingId, "Gabriel@gmail.com", "141234", new Customer());
		role = new Role(roleExistingId, Authorities.ADMIN) ;

		when(roleService.findById(eq(roleExistingId))).thenReturn(role);
		when(roleService.findById(eq(roleNonExistingId))).thenThrow(ResourceNotFoundException.class);

		when(roleService.findAll()).thenReturn(List.of(role));

		when(roleService.insert(any())).thenReturn(role);

		when(roleService.update(eq(roleExistingId), any())).thenReturn(role);
		when(roleService.update(eq(roleNonExistingId), any())).thenThrow(ResourceNotFoundException.class);

		doNothing().when(roleService).delete(eq(roleExistingId));
		doThrow(ResourceNotFoundException.class).when(roleService).delete(roleNonExistingId);
		doThrow(DatabaseException.class).when(roleService).delete(roleDependentId);
	}

	@Test
	public void findByIdShouldReturnProductWhenIdExists() throws Exception {

		ResultActions result = mockMvc.perform(get("/roles/{id}", roleExistingId).accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.authorities").exists());
	}

	@Test
	public void findByIdShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {

		ResultActions result = mockMvc
				.perform(get("/roles/{id}", roleNonExistingId).accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isNotFound());

	}

	@Test
	public void deleteShouldReturnBadRequestWhenIdisDependent() throws Exception {

		ResultActions result = mockMvc
				.perform(delete("/roles/{id}", roleDependentId).accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isBadRequest());

	}

	@Test
	public void deleteShouldReturnNothingWhenIdExists() throws Exception {

		String json = objectMapper.writeValueAsString(role);

		ResultActions result = mockMvc
				.perform(delete("/roles/{id}", roleExistingId).accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isNoContent());

	}

	@Test
	public void deleteShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {

		String json = objectMapper.writeValueAsString(role);

		ResultActions result = mockMvc
				.perform(delete("/roles/{id}", roleNonExistingId).accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isNotFound());

	}

	@Test
	public void insertShouldReturnRoleWasCreated() throws Exception {

		String json = objectMapper.writeValueAsString(role);

		ResultActions result = mockMvc.perform(post("/roles").content(json).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isCreated());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.authorities").exists());

	}

	@Test
	public void updateShouldReturnRoleWhenIdExists() throws Exception {

		String json = objectMapper.writeValueAsString(role);

		ResultActions result = mockMvc.perform(put("/roles/{id}", roleExistingId).content(json)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.authorities").exists());

	}

	@Test
	public void updateShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {

		String json = objectMapper.writeValueAsString(role);

		ResultActions result = mockMvc.perform(put("/roles/{id}", roleNonExistingId).content(json)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isNotFound());

	}

	@Test
	public void findAllShouldReturnList() throws Exception {

		String json = objectMapper.writeValueAsString(role);

		ResultActions result = mockMvc.perform(
				get("/roles").content(json).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isOk());

	}
}
