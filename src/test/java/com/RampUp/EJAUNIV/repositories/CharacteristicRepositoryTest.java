package com.RampUp.EJAUNIV.repositories;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.RampUp.EJAUNIV.entities.Characteristic;
import com.RampUp.EJAUNIV.entities.TimePeriod;
import com.RampUp.EJAUNIV.entities.enums.CharacteristicType;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("junit")
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class CharacteristicRepositoryTest {

    @Autowired
    private CharacteristicRepository characteristicRepository;

    private Integer characteristicNonExistingId;
    private Integer characteristicExistingId;
    private Integer countCharacteristics;
    private Characteristic characteristic;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    @BeforeEach
    void  setUp() throws Exception{
    	characteristicNonExistingId = 32132;
    	characteristicExistingId = 1;
    	countCharacteristics = 11;
    	
    	
    }

    @Test
    public void findShouldReturnACharacteristicOptionalWhenExistingId(){

        Optional<Characteristic> characteristicOptional = characteristicRepository.findById(characteristicExistingId);

        Assertions.assertTrue(characteristicOptional.isPresent());
    }

    @Test
    public void findShouldThrowResourceNotFoundExceptionOptionalWhencharacteristicNonExistingId(){

        Optional<Characteristic> characteristicOptional = characteristicRepository.findById(characteristicNonExistingId);

        Assertions.assertTrue(characteristicOptional.isEmpty());
    }

    @Test
    public void saveShouldPersistWithAutoincrementWhenIdIsNull() throws ParseException{

    	characteristic = new Characteristic(null, "Eletronic", "5000", CharacteristicType.USER, new TimePeriod(sdf.parse("22/09/2022"), sdf.parse("23/09/2022"), sdf.parse("24/09/2022"), characteristic));
    	
    	characteristic.setId(null);

        characteristic = characteristicRepository.save(characteristic);

        Assertions.assertNotNull(characteristic.getId());
        Assertions.assertEquals(countCharacteristics + 1, characteristic.getId());
    }

    @Test
    public void deleteShouldDeleteObjectWhenIdExists(){

        characteristicRepository.deleteById(characteristicExistingId);

        Optional<Characteristic> characteristic = characteristicRepository.findById(characteristicExistingId);
        Assertions.assertTrue(characteristic.isEmpty());
    }

    @Test
    public void deleteShouldThrowEmptyResultNotFoundExceptionWhenIdDoesNotExists(){

        Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
            characteristicRepository.deleteById(characteristicNonExistingId);
        });
    }
}