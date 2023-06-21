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

import com.RampUp.EJAUNIV.entities.Characteristic;
import com.RampUp.EJAUNIV.entities.TimePeriod;
import com.RampUp.EJAUNIV.entities.enums.CharacteristicType;
import com.RampUp.EJAUNIV.repositories.CharacteristicRepository;
import com.RampUp.EJAUNIV.repositories.TimePeriodRepository;
import com.RampUp.EJAUNIV.services.exceptions.ResourceNotFoundException;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class CharacteristicServiceTest {	
	
	@InjectMocks
	private CharacteristicService characteristicservice;
	
	@Mock
	private CharacteristicRepository characteristicRepository;
	
	@Mock
	private TimePeriodRepository timePeriodRepository;
	
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

	private Integer characteristicExistingId;
	private Integer characteristicNonExistingId;
	private Integer characteristicDependentId;
	private Characteristic characteristic;
	private TimePeriod timePeriod;

		
	@BeforeEach
		void setUp() throws Exception {
		
		characteristicExistingId = 1;
		characteristicNonExistingId = 10264;
		characteristicDependentId = 3;
		
		timePeriod = new TimePeriod(sdf.parse("22/09/2022"), sdf.parse("23/09/2022"), sdf.parse("24/09/2022"), characteristic);
		characteristic = new Characteristic(null, "Eletronic", "5000", CharacteristicType.USER, timePeriod);
		
		
		when(characteristicRepository.findById(characteristicExistingId)).thenReturn(Optional.of(characteristic));
		when(characteristicRepository.findById(characteristicNonExistingId)).thenThrow(EntityNotFoundException.class);
		when(characteristicRepository.getReferenceById(characteristicNonExistingId)).thenThrow(EntityNotFoundException.class);
		when(characteristicRepository.getReferenceById(characteristicExistingId)).thenReturn(characteristic);
		
		when(characteristicRepository.findAll()).thenReturn(List.of(characteristic));
;
		
		when(characteristicRepository.save(Mockito.any(Characteristic.class))).thenReturn(characteristic);
		
		doNothing().when(characteristicRepository).deleteById(characteristicExistingId);
		doThrow(EmptyResultDataAccessException.class).when(characteristicRepository).deleteById(characteristicNonExistingId);
		doThrow(DataIntegrityViolationException.class).when(characteristicRepository).deleteById(characteristicDependentId);
		
	}
		@Test
		public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		
			Assertions.assertThrows(ResourceNotFoundException.class,() -> {
				characteristicservice.update(characteristicNonExistingId,characteristic);		
			});
		verify(characteristicRepository, times(1)).getReferenceById(characteristicNonExistingId);
	
	
		}

		@Test
		public void updateShouldReturnCharacteristicWhenIdExists() {
		
			characteristicservice.update(characteristicExistingId,characteristic);
			Assertions.assertNotNull(characteristic);
			
			verify(characteristicRepository, times(1)).getReferenceById(characteristicExistingId);
			verify(characteristicRepository, times(1)).save(characteristic);
			};
		
		@Test
		public void findByIdShouldReturnCharacteristicWhenIdExists() {
		
			characteristicservice.findById(characteristicExistingId);
			Assertions.assertNotNull(characteristic);
			
			verify(characteristicRepository, times(1)).findById(characteristicExistingId);
			};			
		
		@Test
		public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		
			Assertions.assertThrows(EntityNotFoundException.class,() -> {
				characteristicservice.findById(characteristicNonExistingId);		
			});
			
			verify(characteristicRepository, times(1)).findById(characteristicNonExistingId);
			};		
		
		@Test
		public void findAllShouldReturnListOfCharacteristics() {
		
			characteristicservice.findAll();
			Assertions.assertNotNull(List.of(characteristic));
			
			verify(characteristicRepository, times(1)).findAll();
			};			
		
		@Test
		public void deleteShouldDoNothingWhenIdExists() {
		
			Assertions.assertDoesNotThrow(() -> {
				characteristicservice.delete(characteristicExistingId);
			});
			
			verify(characteristicRepository, times(1)).deleteById(characteristicExistingId);
			};
		
		@Test
		public void deleteShouldDoNothingWhenIdDoesNotExists() {
		
			Assertions.assertThrows(ResourceNotFoundException.class,() -> {
				characteristicservice.delete(characteristicNonExistingId);
			});
			
			verify(characteristicRepository, times(1)).deleteById(characteristicNonExistingId);
			};	
			
		@Test
		public void insertShouldAddANewCharacteristic() {
		
			Assertions.assertDoesNotThrow(() -> {
				characteristicservice.insert(characteristic);
			});
			
			verify(characteristicRepository, times(1)).save(characteristic);
			};				
}
