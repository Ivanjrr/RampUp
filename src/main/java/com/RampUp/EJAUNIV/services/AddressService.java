package com.RampUp.EJAUNIV.services;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import com.RampUp.EJAUNIV.entities.User;
import com.RampUp.EJAUNIV.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import com.RampUp.EJAUNIV.entities.Address;
import com.RampUp.EJAUNIV.repositories.AddressRepository;
import com.RampUp.EJAUNIV.services.exceptions.DatabaseException;
import com.RampUp.EJAUNIV.services.exceptions.ResourceNotFoundException;

@Service
public class AddressService {
	
	@Autowired
	private AddressRepository repository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	
	public List<Address> findAll() {
		return repository.findAll();
	}
	@GetMapping
	public Address findById(Integer id) {
		Optional<Address> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ResourceNotFoundException(id));
	}
	@PostMapping
	public Address insert(Address obj) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Optional<User> UserObj = userRepository.findByEmail(auth.getName());

		obj.setClient(UserObj.get().getCustomer());


		return repository.save(obj);
	}
	@DeleteMapping
	public void delete(Integer id) {
	try {
		repository.deleteById(id);
	} catch (EmptyResultDataAccessException e) {
		throw new ResourceNotFoundException(id);
	} catch (DataIntegrityViolationException e) {
		throw new DatabaseException(e.getMessage());
	}
	
	}
	@PutMapping
	public Address update(Integer id, Address obj){
	try {	
		Address entity = repository.getReferenceById(id);
		updateData(entity, obj);
		return repository.save(entity);
		}
	catch(EntityNotFoundException e) {
		throw new ResourceNotFoundException(id);
	}
	}	
	
	
	private void updateData(Address entity, Address obj) {
		entity.setHouseNumber(obj.getHouseNumber());
		entity.setNeighborhood(obj.getNeighborhood());
		entity.setStreet(obj.getStreet());
		entity.setCountry(obj.getCountry());
		entity.setZipCode(obj.getZipCode());
		entity.setComplement(obj.getComplement());
		
	}
}