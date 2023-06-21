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

import com.RampUp.EJAUNIV.entities.ProductOffering;
import com.RampUp.EJAUNIV.entities.TimePeriod;
import com.RampUp.EJAUNIV.entities.enums.POState;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("junit")
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class ProductOfferingRepositoryTest {

    @Autowired
    private ProductOfferingRepository productOfferingRepository;

    private Integer productOfferingNonExistingId;
    private Integer productOfferingExistingId;
    private Integer countProductOfferings;
    private ProductOffering productOffering;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    @BeforeEach
    void  setUp() throws Exception{
    	productOfferingNonExistingId = 32132;
    	productOfferingExistingId = 1;
    	countProductOfferings = 4;
    	
    	
    }

    @Test
    public void findShouldReturnAProductOfferingOptionalWhenExistingId(){

        Optional<ProductOffering> productOfferingOptional = productOfferingRepository.findById(productOfferingExistingId);

        Assertions.assertTrue(productOfferingOptional.isPresent());
    }

    @Test
    public void findShouldThrowResourceNotFoundExceptionOptionalWhenproductOfferingNonExistingId(){

        Optional<ProductOffering> productOfferingOptional = productOfferingRepository.findById(productOfferingNonExistingId);

        Assertions.assertTrue(productOfferingOptional.isEmpty());
    }

    @Test
    public void saveShouldPersistWithAutoincrementWhenIdIsNull() throws ParseException{

    	productOffering = new ProductOffering(null, "TV", true , new TimePeriod(sdf.parse("22/09/2022"), sdf.parse("23/09/2022"), sdf.parse("24/09/2022"), productOffering), POState.TECHNICAL);
    	
    	productOffering.setId(null);

        productOffering = productOfferingRepository.save(productOffering);

        Assertions.assertNotNull(productOffering.getId());
        Assertions.assertEquals(countProductOfferings + 1, productOffering.getId());
    }

    @Test
    public void deleteShouldDeleteObjectWhenIdExists(){

        productOfferingRepository.deleteById(productOfferingExistingId);

        Optional<ProductOffering> productOffering = productOfferingRepository.findById(productOfferingExistingId);
        Assertions.assertTrue(productOffering.isEmpty());
    }

    @Test
    public void deleteShouldThrowEmptyResultNotFoundExceptionWhenIdDoesNotExists(){

        Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
            productOfferingRepository.deleteById(productOfferingNonExistingId);
        });
    }
}
