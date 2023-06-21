package com.RampUp.EJAUNIV.repositories;

import java.util.Optional;

import com.RampUp.EJAUNIV.entities.Order;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.RampUp.EJAUNIV.entities.Address;
import com.RampUp.EJAUNIV.entities.Customer;
import com.RampUp.EJAUNIV.entities.enums.AddressType;
import com.RampUp.EJAUNIV.entities.enums.CustomerType;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("junit")
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class AddressRepositoryTest {

    @Autowired
    private AddressRepository addressRepository;

    private Integer addressNonExistingId;
    private Integer addressExistingId;
    private Integer countAddress;
    private Address address;

    @BeforeEach
    void  setUp() throws Exception{
    	addressNonExistingId = 32132;
    	addressExistingId = 1;
    	countAddress = 6;
    	
    	
    }

    @Test
    public void findShouldReturnAAddressOptionalWhenExistingId(){

        Optional<Address> addressOptional = addressRepository.findById(addressExistingId);

        Assertions.assertTrue(addressOptional.isPresent());
    }

    @Test
    public void findShouldThrowResourceNotFoundExceptionOptionalWhenaddressNonExistingId(){

        Optional<Address> addressOptional = addressRepository.findById(addressNonExistingId);

        Assertions.assertTrue(addressOptional.isEmpty());
    }

    @Test
    public void saveShouldPersistWithAutoincrementWhenIdIsNull(){

    	address = new Address(addressExistingId, "Travessa Remanso", 43 , "Rio Vermelho", "Casa", "41950700", "Brazil", AddressType.HOMEADDRESS, new Customer(null,"Jorge","RG","Ativo","1000","141235", CustomerType.LEGALPERSON), new Order());
    	
    	address.setId(null);

        address = addressRepository.save(address);

        Assertions.assertNotNull(address.getId());
        Assertions.assertEquals(countAddress + 1, address.getId());
    }

    @Test
    public void deleteShouldDeleteObjectWhenIdExists(){

        addressRepository.deleteById(addressExistingId);

        Optional<Address> address = addressRepository.findById(addressExistingId);
        Assertions.assertTrue(address.isEmpty());
    }

    @Test
    public void deleteShouldThrowEmptyResultNotFoundExceptionWhenIdDoesNotExists(){

        Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
            addressRepository.deleteById(addressNonExistingId);
        });
    }
}