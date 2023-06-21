package com.RampUp.EJAUNIV.repositories;

import java.util.Optional;

import com.RampUp.EJAUNIV.entities.Customer;
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

import com.RampUp.EJAUNIV.entities.User;


@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("junit")
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private Integer userNonExistingId;
    private Integer userExistingId;
    private Integer countUsers;
    private User user;

    @BeforeEach
    void  setUp() throws Exception{
    	userNonExistingId = 32132;
    	userExistingId = 1;
    	countUsers = 5;
    	
    	
    }

    @Test
    public void findShouldReturnAUserOptionalWhenExistingId(){

        Optional<User> userOptional = userRepository.findById(userExistingId);

        Assertions.assertTrue(userOptional.isPresent());
    }

    @Test
    public void findShouldThrowResourceNotFoundExceptionOptionalWhenuserNonExistingId(){

        Optional<User> userOptional = userRepository.findById(userNonExistingId);

        Assertions.assertTrue(userOptional.isEmpty());
    }

    @Test
    public void saveShouldPersistWithAutoincrementWhenIdIsNull(){

    	user = new User(1, "Gabriel@gmail.com", "141234", new Customer());
    	
    	user.setId(null);

        user = userRepository.save(user);

        Assertions.assertNotNull(user.getId());
        Assertions.assertEquals(countUsers + 1, user.getId());
    }

    @Test
    public void deleteShouldDeleteObjectWhenIdExists(){

        userRepository.deleteById(userExistingId);

        Optional<User> user = userRepository.findById(userExistingId);
        Assertions.assertTrue(user.isEmpty());
    }

    @Test
    public void deleteShouldThrowEmptyResultNotFoundExceptionWhenIdDoesNotExists(){

        Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
            userRepository.deleteById(userNonExistingId);
        });
    }
}