package com.RampUp.EJAUNIV.services;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

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

import com.RampUp.EJAUNIV.entities.Customer;
import com.RampUp.EJAUNIV.entities.enums.CustomerType;
import com.RampUp.EJAUNIV.repositories.CustomerRepository;
import com.RampUp.EJAUNIV.services.exceptions.ResourceNotFoundException;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class CustomerServiceTest {	
	
	@InjectMocks
	private CustomerService customerservice;
	
	@Mock
	private CustomerRepository customerRepository;
	
	
	private Integer customerExistingId;
	private Integer customerNonExistingId;
	private Integer customerDependentId;
	private Customer customer;

	
	
	
	@BeforeEach
		void setUp() throws Exception {
		
		customerExistingId = 1;
		customerNonExistingId = 10264;
		customerDependentId = 3;
		
		
		customer = new Customer(null,"Jorge","RG","Ativo","1000","141235",CustomerType.LEGALPERSON) ;
		
		
		when(customerRepository.findById(customerExistingId)).thenReturn(Optional.of(customer));
		when(customerRepository.findById(customerNonExistingId)).thenThrow(EntityNotFoundException.class);
		when(customerRepository.getReferenceById(customerNonExistingId)).thenThrow(EntityNotFoundException.class);
		when(customerRepository.getReferenceById(customerExistingId)).thenReturn(customer);
		
		when(customerRepository.findAll()).thenReturn(List.of(customer));
		
		when(customerRepository.save(Mockito.any(Customer.class))).thenReturn(customer);
		
		doNothing().when(customerRepository).deleteById(customerExistingId);
		doThrow(EmptyResultDataAccessException.class).when(customerRepository).deleteById(customerNonExistingId);
		doThrow(DataIntegrityViolationException.class).when(customerRepository).deleteById(customerDependentId);
		
	}
		@Test
		public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		
			Assertions.assertThrows(ResourceNotFoundException.class,() -> {
				customerservice.update(customerNonExistingId,customer);		
			});
		verify(customerRepository, times(1)).getReferenceById(customerNonExistingId);
	
	
		}

		@Test
		public void updateShouldReturnCustomerWhenIdExists() {
		
			customerservice.update(customerExistingId,customer);
			Assertions.assertNotNull(customer);
			
			verify(customerRepository, times(1)).getReferenceById(customerExistingId);
			verify(customerRepository, times(1)).save(customer);
			};
		
		@Test
		public void findByIdShouldReturnCustomerWhenIdExists() {
		
			customerservice.findById(customerExistingId);
			Assertions.assertNotNull(customer);
			
			verify(customerRepository, times(1)).findById(customerExistingId);
			};			
		
		@Test
		public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		
			Assertions.assertThrows(EntityNotFoundException.class,() -> {
				customerservice.findById(customerNonExistingId);		
			});
			
			verify(customerRepository, times(1)).findById(customerNonExistingId);
			};		
		
		@Test
		public void findAllShouldReturnListOfCustomers() {
		
			customerservice.findAll();
			Assertions.assertNotNull(List.of(customer));
			
			verify(customerRepository, times(1)).findAll();
			};			
		
		@Test
		public void deleteShouldDoNothingWhenIdExists() {
		
			Assertions.assertDoesNotThrow(() -> {
				customerservice.delete(customerExistingId);
			});
			
			verify(customerRepository, times(1)).deleteById(customerExistingId);
			};
		
		@Test
		public void deleteShouldDoNothingWhenIdDoesNotExists() {
		
			Assertions.assertThrows(ResourceNotFoundException.class,() -> {
				customerservice.delete(customerNonExistingId);
			});
			
			verify(customerRepository, times(1)).deleteById(customerNonExistingId);
			};	
			
		@Test
		public void insertShouldAddANewCustomer() {
		
			Assertions.assertDoesNotThrow(() -> {
				customerservice.insert(customer);
			});
			
			verify(customerRepository, times(1)).save(customer);
			};				
}
