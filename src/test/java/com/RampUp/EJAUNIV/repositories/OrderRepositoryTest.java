package com.RampUp.EJAUNIV.repositories;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Optional;

import com.RampUp.EJAUNIV.entities.*;
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

import com.RampUp.EJAUNIV.entities.enums.CustomerType;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("junit")
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private Address addressRepository;

    private Integer orderNonExistingId;
    private Integer orderExistingId;
    private Integer countOrders;
    private Order order;

    
    @BeforeEach
    void  setUp() throws Exception{
    	orderNonExistingId = 32132;
    	orderExistingId = 1;
    	countOrders = 3;
    	
    	
    }

    @Test
    public void findShouldReturnAOrderOptionalWhenExistingId(){

        Optional<Order> orderOptional = orderRepository.findById(orderExistingId);

        Assertions.assertTrue(orderOptional.isPresent());
    }

    @Test
    public void findShouldThrowResourceNotFoundExceptionOptionalWhenorderNonExistingId(){

        Optional<Order> orderOptional = orderRepository.findById(orderNonExistingId);

        Assertions.assertTrue(orderOptional.isEmpty());
    }

    @Test
    public void saveShouldPersistWithAutoincrementWhenIdIsNull() throws ParseException{

    	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    	
    	order = new Order(null, sdf.parse("25/10/2022"), new Customer(null, "Jorge", "RG", "Ativo", "1000", "141235", CustomerType.LEGALPERSON), new BankSlipPayment(), new Address());
    	
    	order.setId(null);

        order = orderRepository.save(order);

        Assertions.assertNotNull(order.getId());
        Assertions.assertEquals(countOrders + 1, order.getId());
    }

    @Test
    public void deleteShouldDeleteObjectWhenIdExists(){

        orderRepository.deleteById(orderExistingId);

        Optional<Order> order = orderRepository.findById(orderExistingId);
        Assertions.assertTrue(order.isEmpty());
    }

    @Test
    public void deleteShouldThrowEmptyResultNotFoundExceptionWhenIdDoesNotExists(){

        Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
            orderRepository.deleteById(orderNonExistingId);
        });
    }
}
