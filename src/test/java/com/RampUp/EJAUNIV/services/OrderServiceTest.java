package com.RampUp.EJAUNIV.services;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import com.RampUp.EJAUNIV.entities.Address;
import com.RampUp.EJAUNIV.entities.BankSlipPayment;
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
import com.RampUp.EJAUNIV.entities.Order;
import com.RampUp.EJAUNIV.entities.enums.CustomerType;
import com.RampUp.EJAUNIV.repositories.CustomerRepository;
import com.RampUp.EJAUNIV.repositories.OrderRepository;
import com.RampUp.EJAUNIV.services.exceptions.ResourceNotFoundException;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class OrderServiceTest {	
	
	@InjectMocks
	private OrderService orderservice;
	
	@Mock
	private OrderRepository orderRepository;
	
	@Mock
	private CustomerRepository customerRepository;
	

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
		
		customer = new Customer(null,"Jorge","RG","Ativo","1000","141235",CustomerType.LEGALPERSON);
		order = new Order(null, sdf.parse("25/10/2022"), new Customer(null, "Jorge", "RG", "Ativo", "1000", "141235", CustomerType.LEGALPERSON), new BankSlipPayment(), new Address());
		
		
		when(orderRepository.findById(orderExistingId)).thenReturn(Optional.of(order));
		when(orderRepository.findById(orderNonExistingId)).thenThrow(EntityNotFoundException.class);
		when(orderRepository.getReferenceById(orderNonExistingId)).thenThrow(EntityNotFoundException.class);
		when(orderRepository.getReferenceById(orderExistingId)).thenReturn(order);
		
		when(orderRepository.findAll()).thenReturn(List.of(order));
;
		
		when(orderRepository.save(Mockito.any(Order.class))).thenReturn(order);
		
		doNothing().when(orderRepository).deleteById(orderExistingId);
		doThrow(EmptyResultDataAccessException.class).when(orderRepository).deleteById(orderNonExistingId);
		doThrow(DataIntegrityViolationException.class).when(orderRepository).deleteById(orderDependentId);
		
	}
		@Test
		public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		
			Assertions.assertThrows(ResourceNotFoundException.class,() -> {
				orderservice.update(orderNonExistingId,order);		
			});
		verify(orderRepository, times(1)).getReferenceById(orderNonExistingId);
	
	
		}

		@Test
		public void updateShouldReturnOrderWhenIdExists() {
		
			orderservice.update(orderExistingId,order);
			Assertions.assertNotNull(order);
			
			verify(orderRepository, times(1)).getReferenceById(orderExistingId);
			verify(orderRepository, times(1)).save(order);
			};
		
		@Test
		public void findByIdShouldReturnOrderWhenIdExists() {
		
			orderservice.findById(orderExistingId);
			Assertions.assertNotNull(order);
			
			verify(orderRepository, times(1)).findById(orderExistingId);
			};			
		
		@Test
		public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		
			Assertions.assertThrows(EntityNotFoundException.class,() -> {
				orderservice.findById(orderNonExistingId);		
			});
			
			verify(orderRepository, times(1)).findById(orderNonExistingId);
			};		
		
		@Test
		public void findAllShouldReturnListOfOrders() {
		
			orderservice.findAll();
			Assertions.assertNotNull(List.of(order));
			
			verify(orderRepository, times(1)).findAll();
			};			
		
		@Test
		public void deleteShouldDoNothingWhenIdExists() {
		
			Assertions.assertDoesNotThrow(() -> {
				orderservice.delete(orderExistingId);
			});
			
			verify(orderRepository, times(1)).deleteById(orderExistingId);
			};
		
		@Test
		public void deleteShouldDoNothingWhenIdDoesNotExists() {
		
			Assertions.assertThrows(ResourceNotFoundException.class,() -> {
				orderservice.delete(orderNonExistingId);
			});
			
			verify(orderRepository, times(1)).deleteById(orderNonExistingId);
			};	
			
		@Test
		public void insertShouldAddANewOrder() {
		
			Assertions.assertDoesNotThrow(() -> {
				orderservice.insert(order);
			});
			
			verify(orderRepository, times(1)).save(order);
			};				
}
