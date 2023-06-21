package com.RampUp.EJAUNIV.services;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import com.RampUp.EJAUNIV.entities.User;
import com.RampUp.EJAUNIV.entities.enums.Authorities;
import com.RampUp.EJAUNIV.repositories.UserRepository;
import com.RampUp.EJAUNIV.services.exceptions.SecurityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import com.RampUp.EJAUNIV.entities.Customer;
import com.RampUp.EJAUNIV.repositories.CustomerRepository;
import com.RampUp.EJAUNIV.services.exceptions.DatabaseException;
import com.RampUp.EJAUNIV.services.exceptions.ResourceNotFoundException;
import com.fasterxml.jackson.annotation.JsonView;

@Service
public class CustomerService {
	
	@Autowired
	private CustomerRepository repository;

	@Autowired
	private UserRepository userRepository;


	
	public List<Customer> findAll() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Boolean hasAdminRole = auth.getAuthorities().stream()
				.anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"));
		if(hasAdminRole) {


			return repository.findAll();
		}
		else {
			throw new SecurityException("You don't have permission to access customers data");
		}
		}
	@GetMapping
	public Customer findById(Integer id) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Optional<Customer> obj = repository.findById(id);
		Optional <User> userObj = userRepository.findById(obj.get().getClient().getId());
		if(auth.getName().compareTo(userObj.get().getEmail()) == 0) {
			return obj.orElseThrow(() -> new ResourceNotFoundException(id));
		}
		else {
			throw new SecurityException("You don't have permission to access this costumer data");
		}


	}
	@PostMapping
	public Customer insert(Customer obj) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Boolean hasOperatorRole = auth.getAuthorities().stream()
				.anyMatch(r -> r.getAuthority().equals("ROLE_OPERATOR"));
		if(obj.getCustomerName() == null) { throw new DatabaseException("You need to insert a customer name");
		}
		else if(obj.getDocumentNumber() == null)
		{throw new DatabaseException("Document number cannot be null");
		}

		else {
			if (hasOperatorRole) {
				Optional<User> userObj = userRepository.findByEmail(auth.getName());

				obj.setClient(userObj.get());

				return repository.save(obj);
			} else throw new SecurityException("Admins can`t create customer profile");
		}
		}
	@DeleteMapping
	public void delete(Integer id) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Optional<Customer> obj = repository.findById(id);
		Optional <User> userObj = userRepository.findById(obj.get().getClient().getId());
		if(auth.getName().compareTo(userObj.get().getEmail()) == 0) {
			try {
				repository.deleteById(id);
			} catch (EmptyResultDataAccessException e) {
				throw new ResourceNotFoundException(id);
			} catch (DataIntegrityViolationException e) {
				throw new DatabaseException(e.getMessage());
			}
		}
		else {
			throw  new SecurityException("You don't have permission to delete this costumer data");
		}
	}
	@PutMapping
	public Customer update(Integer id, Customer obj){
		Customer entity = repository.getReferenceById(id);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Optional <User> userObj = userRepository.findById(entity.getClient().getId());
		if(auth.getName().compareTo(userObj.get().getEmail()) == 0) {
			try {
				updateData(entity, obj);
				return repository.save(entity);
			} catch (EntityNotFoundException e) {
				throw new ResourceNotFoundException(id);
			}
		}
		else {
			throw new SecurityException("You don't have permission to update this costumer data");
		}
	}
	
	private void updateData(Customer entity, Customer obj) {
		if(obj.getCustomerName() != null){
		entity.setCustomerName(obj.getCustomerName());}
		if(obj.getCustomerStatus() != null){
		entity.setCustomerStatus(obj.getCustomerStatus());}
		if(obj.getDocumentNumber() != null){
		entity.setDocumentNumber(obj.getDocumentNumber());}
		if(obj.getCreditScore() != null) {
		entity.setCreditScore(obj.getCreditScore());}
		if(obj.getCustomerType() != null) {
		entity.setCustomerType(obj.getCustomerType());}

		
	}
}
