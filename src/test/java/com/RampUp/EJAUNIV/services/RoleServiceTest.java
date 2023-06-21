package com.RampUp.EJAUNIV.services;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import com.RampUp.EJAUNIV.entities.Customer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;

import com.RampUp.EJAUNIV.entities.Role;
import com.RampUp.EJAUNIV.entities.User;
import com.RampUp.EJAUNIV.entities.enums.Authorities;
import com.RampUp.EJAUNIV.repositories.RoleRepository;
import com.RampUp.EJAUNIV.repositories.UserRepository;
import com.RampUp.EJAUNIV.services.exceptions.ResourceNotFoundException;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class RoleServiceTest {	
	
	@InjectMocks
	private RoleService roleservice;
	
	@Mock
	private RoleRepository roleRepository;
	
	@Mock
	private UserRepository userRepository;
	
	
	private Integer roleExistingId;
	private Integer roleNonExistingId;
	private Integer roleDependentId;
	private Role role;
	private Integer userExistingId;
	private Integer userNonExistingId;
	private Integer userDependentId;
	private User user;
	
	
	
	@BeforeEach
		void setUp() throws Exception {
		
		roleExistingId = 1;
		roleNonExistingId = 10264;
		roleDependentId = 3;
		userExistingId = 2;
		userNonExistingId = 28667;
		userDependentId = 5;
		
		user = new User(null, "Gabriel@gmail.com", "141234", new Customer());
		role = new Role(null, Authorities.ADMIN) ;
		
		
		when(roleRepository.findById(roleExistingId)).thenReturn(Optional.of(role));
		when(roleRepository.findById(roleNonExistingId)).thenThrow(EntityNotFoundException.class);
		when(roleRepository.getReferenceById(roleNonExistingId)).thenThrow(EntityNotFoundException.class);
		when(roleRepository.getReferenceById(roleExistingId)).thenReturn(role);
		
		when(roleRepository.findAll()).thenReturn(List.of(role));
		
		when(roleRepository.save(Mockito.any(Role.class))).thenReturn(role);
		
		doNothing().when(roleRepository).deleteById(roleExistingId);
		doThrow(EmptyResultDataAccessException.class).when(roleRepository).deleteById(roleNonExistingId);
		doThrow(DataIntegrityViolationException.class).when(roleRepository).deleteById(roleDependentId);
		
	}
		@Test
		public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		
			Assertions.assertThrows(ResourceNotFoundException.class,() -> {
				roleservice.update(roleNonExistingId,role);		
			});
		verify(roleRepository, times(1)).getReferenceById(roleNonExistingId);
	
	
		}

		@Test
		public void updateShouldReturnRoleWhenIdExists() {
		
			roleservice.update(roleExistingId,role);
			Assertions.assertNotNull(role);
			
			verify(roleRepository, times(1)).getReferenceById(roleExistingId);
			verify(roleRepository, times(1)).save(role);
			};
		
		@Test
		public void findByIdShouldReturnRoleWhenIdExists() {
		
			roleservice.findById(roleExistingId);
			Assertions.assertNotNull(role);
			
			verify(roleRepository, times(1)).findById(roleExistingId);
			};			
		
		@Test
		public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		
			Assertions.assertThrows(EntityNotFoundException.class,() -> {
				roleservice.findById(roleNonExistingId);		
			});
			
			verify(roleRepository, times(1)).findById(roleNonExistingId);
			};		
		
		@Test
		public void findAllShouldReturnListOfRoles() {
		
			roleservice.findAll();
			Assertions.assertNotNull(List.of(role));
			
			verify(roleRepository, times(1)).findAll();
			};			
		
		@Test
		public void deleteShouldDoNothingWhenIdExists() {
		
			Assertions.assertDoesNotThrow(() -> {
				roleservice.delete(roleExistingId);
			});
			
			verify(roleRepository, times(1)).deleteById(roleExistingId);
			};
		
		@Test
		public void deleteShouldDoNothingWhenIdDoesNotExists() {
		
			Assertions.assertThrows(ResourceNotFoundException.class,() -> {
				roleservice.delete(roleNonExistingId);
			});
			
			verify(roleRepository, times(1)).deleteById(roleNonExistingId);
			};	
			
		@Test
		public void insertShouldAddANewRole() {
		
			Assertions.assertDoesNotThrow(() -> {
				roleservice.insert(role);
			});
			
			verify(roleRepository, times(1)).save(role);
			};				
}
