package com.RampUp.EJAUNIV.repositories;

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

import com.RampUp.EJAUNIV.entities.Customer;
import com.RampUp.EJAUNIV.entities.enums.CustomerType;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("junit")
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    private Integer customerNonExistingId;
    private Integer customerExistingId;
    private Integer countCustomers;
    private Customer customer;

    @BeforeEach
    void  setUp() throws Exception{
    	customerNonExistingId = 32132;
    	customerExistingId = 1;
    	countCustomers = 5;
    	
    	
    }

    @Test
    public void findShouldReturnACustomerOptionalWhenExistingId(){

        Optional<Customer> customerOptional = customerRepository.findById(customerExistingId);

        Assertions.assertTrue(customerOptional.isPresent());
    }

    @Test
    public void findShouldThrowResourceNotFoundExceptionOptionalWhencustomerNonExistingId(){

        Optional<Customer> customerOptional = customerRepository.findById(customerNonExistingId);

        Assertions.assertTrue(customerOptional.isEmpty());
    }

    @Test
    public void saveShouldPersistWithAutoincrementWhenIdIsNull(){

    	customer = new Customer(null,"Jorge","RG","Ativo","1000","141235",CustomerType.LEGALPERSON);
    	
    	customer.setId(null);

        customer = customerRepository.save(customer);

        Assertions.assertNotNull(customer.getId());
        Assertions.assertEquals(countCustomers + 1, customer.getId());
    }

    @Test
    public void deleteShouldDeleteObjectWhenIdExists(){

        customerRepository.deleteById(customerExistingId);

        Optional<Customer> customer = customerRepository.findById(customerExistingId);
        Assertions.assertTrue(customer.isEmpty());
    }

    @Test
    public void deleteShouldThrowEmptyResultNotFoundExceptionWhenIdDoesNotExists(){

        Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
            customerRepository.deleteById(customerNonExistingId);
        });
    }
}