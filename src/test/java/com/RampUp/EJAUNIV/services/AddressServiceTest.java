package com.RampUp.EJAUNIV.services;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import com.RampUp.EJAUNIV.entities.Order;
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

import com.RampUp.EJAUNIV.entities.Address;
import com.RampUp.EJAUNIV.entities.Customer;
import com.RampUp.EJAUNIV.entities.enums.AddressType;
import com.RampUp.EJAUNIV.entities.enums.CustomerType;
import com.RampUp.EJAUNIV.repositories.AddressRepository;
import com.RampUp.EJAUNIV.repositories.CustomerRepository;
import com.RampUp.EJAUNIV.services.exceptions.ResourceNotFoundException;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class AddressServiceTest {	
	
	@InjectMocks
	private AddressService addressservice;
	
	@Mock
	private AddressRepository addressRepository;
	
	@Mock
	private CustomerRepository customerRepository;
	

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
		
		
		when(addressRepository.findById(addressExistingId)).thenReturn(Optional.of(address));
		when(addressRepository.findById(addressNonExistingId)).thenThrow(EntityNotFoundException.class);
		when(addressRepository.getReferenceById(addressNonExistingId)).thenThrow(EntityNotFoundException.class);
		when(addressRepository.getReferenceById(addressExistingId)).thenReturn(address);
		
		when(addressRepository.findAll()).thenReturn(List.of(address));

		
		when(addressRepository.save(Mockito.any(Address.class))).thenReturn(address);
		
		doNothing().when(addressRepository).deleteById(addressExistingId);
		doThrow(EmptyResultDataAccessException.class).when(addressRepository).deleteById(addressNonExistingId);
		doThrow(DataIntegrityViolationException.class).when(addressRepository).deleteById(addressDependentId);
		
	}
		@Test
		public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		
			Assertions.assertThrows(ResourceNotFoundException.class,() -> {
				addressservice.update(addressNonExistingId,address);		
			});
		verify(addressRepository, times(1)).getReferenceById(addressNonExistingId);
	
	
		}

		@Test
		public void updateShouldReturnAddressWhenIdExists() {
		
			addressservice.update(addressExistingId,address);
			Assertions.assertNotNull(address);
			
			verify(addressRepository, times(1)).getReferenceById(addressExistingId);
			verify(addressRepository, times(1)).save(address);
			};
		
		@Test
		public void findByIdShouldReturnAddressWhenIdExists() {
		
			addressservice.findById(addressExistingId);
			Assertions.assertNotNull(address);
			
			verify(addressRepository, times(1)).findById(addressExistingId);
			};			
		
		@Test
		public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		
			Assertions.assertThrows(EntityNotFoundException.class,() -> {
				addressservice.findById(addressNonExistingId);		
			});
			
			verify(addressRepository, times(1)).findById(addressNonExistingId);
			};		
		
		@Test
		public void findAllShouldReturnListOfAddresss() {
		
			addressservice.findAll();
			Assertions.assertNotNull(List.of(address));
			
			verify(addressRepository, times(1)).findAll();
			};			
		
		@Test
		public void deleteShouldDoNothingWhenIdExists() {
		
			Assertions.assertDoesNotThrow(() -> {
				addressservice.delete(addressExistingId);
			});
			
			verify(addressRepository, times(1)).deleteById(addressExistingId);
			};
		
		@Test
		public void deleteShouldDoNothingWhenIdDoesNotExists() {
		
			Assertions.assertThrows(ResourceNotFoundException.class,() -> {
				addressservice.delete(addressNonExistingId);
			});
			
			verify(addressRepository, times(1)).deleteById(addressNonExistingId);
			};	
			
		@Test
		public void insertShouldAddANewAddress() {
		
			Assertions.assertDoesNotThrow(() -> {
				addressservice.insert(address);
			});
			
			verify(addressRepository, times(1)).save(address);
			};				
}
