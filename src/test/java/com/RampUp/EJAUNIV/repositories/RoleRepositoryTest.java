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

import com.RampUp.EJAUNIV.entities.Role;
import com.RampUp.EJAUNIV.entities.enums.Authorities;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("junit")
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    private Integer roleNonExistingId;
    private Integer roleExistingId;
    private Integer countRoles;
    private Role role;

    @BeforeEach
    void  setUp() throws Exception{
    	roleNonExistingId = 32132;
    	roleExistingId = 1;
    	countRoles = 2;
    	
    	
    }

    @Test
    public void findShouldReturnARoleOptionalWhenExistingId(){

        Optional<Role> roleOptional = roleRepository.findById(roleExistingId);

        Assertions.assertTrue(roleOptional.isPresent());
    }

    @Test
    public void findShouldThrowResourceNotFoundExceptionOptionalWhenroleNonExistingId(){

        Optional<Role> roleOptional = roleRepository.findById(roleNonExistingId);

        Assertions.assertTrue(roleOptional.isEmpty());
    }

    @Test
    public void saveShouldPersistWithAutoincrementWhenIdIsNull(){

    	role = new Role(null, Authorities.ADMIN);
    	
    	role.setId(null);

        role = roleRepository.save(role);

        Assertions.assertNotNull(role.getId());
        Assertions.assertEquals(countRoles + 1, role.getId());
    }

    @Test
    public void deleteShouldDeleteObjectWhenIdExists(){

        roleRepository.deleteById(roleExistingId);

        Optional<Role> role = roleRepository.findById(roleExistingId);
        Assertions.assertTrue(role.isEmpty());
    }

    @Test
    public void deleteShouldThrowEmptyResultNotFoundExceptionWhenIdDoesNotExists(){

        Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
            roleRepository.deleteById(roleNonExistingId);
        });
    }
}
