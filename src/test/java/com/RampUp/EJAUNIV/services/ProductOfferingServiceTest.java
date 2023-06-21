package com.RampUp.EJAUNIV.services;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.text.SimpleDateFormat;
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

import com.RampUp.EJAUNIV.entities.ProductOffering;
import com.RampUp.EJAUNIV.entities.TimePeriod;
import com.RampUp.EJAUNIV.entities.enums.POState;
import com.RampUp.EJAUNIV.repositories.ProductOfferingRepository;
import com.RampUp.EJAUNIV.repositories.TimePeriodRepository;
import com.RampUp.EJAUNIV.services.exceptions.ResourceNotFoundException;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class ProductOfferingServiceTest {	
	
	@InjectMocks
	private ProductOfferingService productOfferingservice;
	
	@Mock
	private ProductOfferingRepository productOfferingRepository;
	
	@Mock
	private TimePeriodRepository timePeriodRepository;
	
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
		
		timePeriod = new TimePeriod(sdf.parse("22/09/2022"), sdf.parse("23/09/2022"), sdf.parse("24/09/2022"), productOffering);
		productOffering = new ProductOffering(null, "TV", true , timePeriod, POState.TECHNICAL);
		
		
		when(productOfferingRepository.findById(productOfferingExistingId)).thenReturn(Optional.of(productOffering));
		when(productOfferingRepository.findById(productOfferingNonExistingId)).thenThrow(EntityNotFoundException.class);
		when(productOfferingRepository.getReferenceById(productOfferingNonExistingId)).thenThrow(EntityNotFoundException.class);
		when(productOfferingRepository.getReferenceById(productOfferingExistingId)).thenReturn(productOffering);
		
		when(productOfferingRepository.findAll()).thenReturn(List.of(productOffering));
;
		
		when(productOfferingRepository.save(Mockito.any(ProductOffering.class))).thenReturn(productOffering);
		
		doNothing().when(productOfferingRepository).deleteById(productOfferingExistingId);
		doThrow(EmptyResultDataAccessException.class).when(productOfferingRepository).deleteById(productOfferingNonExistingId);
		doThrow(DataIntegrityViolationException.class).when(productOfferingRepository).deleteById(productOfferingDependentId);
		
	}
		@Test
		public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		
			Assertions.assertThrows(ResourceNotFoundException.class,() -> {
				productOfferingservice.update(productOfferingNonExistingId,productOffering);		
			});
		verify(productOfferingRepository, times(1)).getReferenceById(productOfferingNonExistingId);
	
	
		}

		@Test
		public void updateShouldReturnProductOfferingWhenIdExists() {
		
			productOfferingservice.update(productOfferingExistingId,productOffering);
			Assertions.assertNotNull(productOffering);
			
			verify(productOfferingRepository, times(1)).getReferenceById(productOfferingExistingId);
			verify(productOfferingRepository, times(1)).save(productOffering);
			};
		
		@Test
		public void findByIdShouldReturnProductOfferingWhenIdExists() {
		
			productOfferingservice.findById(productOfferingExistingId);
			Assertions.assertNotNull(productOffering);
			
			verify(productOfferingRepository, times(1)).findById(productOfferingExistingId);
			};			
		
		@Test
		public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		
			Assertions.assertThrows(EntityNotFoundException.class,() -> {
				productOfferingservice.findById(productOfferingNonExistingId);		
			});
			
			verify(productOfferingRepository, times(1)).findById(productOfferingNonExistingId);
			};		
		
		@Test
		public void findAllShouldReturnListOfProductOfferings() {
		
			productOfferingservice.findAll();
			Assertions.assertNotNull(List.of(productOffering));
			
			verify(productOfferingRepository, times(1)).findAll();
			};			
		
		@Test
		public void deleteShouldDoNothingWhenIdExists() {
		
			Assertions.assertDoesNotThrow(() -> {
				productOfferingservice.delete(productOfferingExistingId);
			});
			
			verify(productOfferingRepository, times(1)).deleteById(productOfferingExistingId);
			};
		
		@Test
		public void deleteShouldDoNothingWhenIdDoesNotExists() {
		
			Assertions.assertThrows(ResourceNotFoundException.class,() -> {
				productOfferingservice.delete(productOfferingNonExistingId);
			});
			
			verify(productOfferingRepository, times(1)).deleteById(productOfferingNonExistingId);
			};	
			
		@Test
		public void insertShouldAddANewProductOffering() {
		
			Assertions.assertDoesNotThrow(() -> {
				productOfferingservice.insert(productOffering);
			});
			
			verify(productOfferingRepository, times(1)).save(productOffering);
			};				
}