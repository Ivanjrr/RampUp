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

import com.RampUp.EJAUNIV.entities.User;
import com.RampUp.EJAUNIV.repositories.UserRepository;
import com.RampUp.EJAUNIV.services.exceptions.ResourceNotFoundException;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UserServiceTest {	
	
	@InjectMocks
	private UserService userservice;
	
	@Mock
	private UserRepository userRepository;
	

	private Integer userExistingId;
	private Integer userNonExistingId;
	private Integer userDependentId;
	private User user;

		
	@BeforeEach
		void setUp() throws Exception {
		
		userExistingId = 1;
		userNonExistingId = 10264;
		userDependentId = 3;
		
		
		user = new User(null, "Gabriel@gmail.com", "141234", new Customer());
		
		
		when(userRepository.findById(userExistingId)).thenReturn(Optional.of(user));
		when(userRepository.findById(userNonExistingId)).thenThrow(EntityNotFoundException.class);
		when(userRepository.getReferenceById(userNonExistingId)).thenThrow(EntityNotFoundException.class);
		when(userRepository.getReferenceById(userExistingId)).thenReturn(user);
		
		when(userRepository.findAll()).thenReturn(List.of(user));
;
		
		when(userRepository.save(Mockito.any(User.class))).thenReturn(user);
		
		doNothing().when(userRepository).deleteById(userExistingId);
		doThrow(EmptyResultDataAccessException.class).when(userRepository).deleteById(userNonExistingId);
		doThrow(DataIntegrityViolationException.class).when(userRepository).deleteById(userDependentId);
		
	}
		@Test
		public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		
			Assertions.assertThrows(ResourceNotFoundException.class,() -> {
				userservice.update(userNonExistingId,user);		
			});
		verify(userRepository, times(1)).getReferenceById(userNonExistingId);
	
	
		}

		@Test
		public void updateShouldReturnUserWhenIdExists() {
		
			userservice.update(userExistingId,user);
			Assertions.assertNotNull(user);
			
			verify(userRepository, times(1)).getReferenceById(userExistingId);
			verify(userRepository, times(1)).save(user);
			};
		
		@Test
		public void findByIdShouldReturnUserWhenIdExists() {
		
			userservice.findById(userExistingId);
			Assertions.assertNotNull(user);
			
			verify(userRepository, times(1)).findById(userExistingId);
			};			
		
		@Test
		public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		
			Assertions.assertThrows(EntityNotFoundException.class,() -> {
				userservice.findById(userNonExistingId);		
			});
			
			verify(userRepository, times(1)).findById(userNonExistingId);
			};		
		
		@Test
		public void findAllShouldReturnListOfUsers() {
		
			userservice.findAll();
			Assertions.assertNotNull(List.of(user));
			
			verify(userRepository, times(1)).findAll();
			};			
		
		@Test
		public void deleteShouldDoNothingWhenIdExists() {
		
			Assertions.assertDoesNotThrow(() -> {
				userservice.delete(userExistingId);
			});
			
			verify(userRepository, times(1)).deleteById(userExistingId);
			};
		
		@Test
		public void deleteShouldDoNothingWhenIdDoesNotExists() {
		
			Assertions.assertThrows(ResourceNotFoundException.class,() -> {
				userservice.delete(userNonExistingId);
			});
			
			verify(userRepository, times(1)).deleteById(userNonExistingId);
			};	
			
		@Test
		public void insertShouldAddANewUser() {
		
			Assertions.assertDoesNotThrow(() -> {
				userservice.insert(user);
			});
			
			verify(userRepository, times(1)).save(user);
			};				
}

