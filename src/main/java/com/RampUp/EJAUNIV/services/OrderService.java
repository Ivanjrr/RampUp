package com.RampUp.EJAUNIV.services;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;

import com.RampUp.EJAUNIV.entities.*;
import com.RampUp.EJAUNIV.entities.enums.Authorities;
import com.RampUp.EJAUNIV.repositories.*;
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

import com.RampUp.EJAUNIV.services.exceptions.DatabaseException;
import com.RampUp.EJAUNIV.services.exceptions.ResourceNotFoundException;

@Service
public class OrderService {

	@Autowired
	private OrderRepository repository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private AddressRepository addressRepository;

	@Autowired
	AddressService addressService;

	@Autowired
	EntityManager entityManager;

	public List<Order> findAll() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Boolean hasAdminRole = auth.getAuthorities().stream()
				.anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"));
		if (hasAdminRole) {

			return repository.findAll();
		} else {
			throw new SecurityException("You don't have permission to access orders data");
		}

	}

	@GetMapping
	public Order findById(Integer id) {
		Optional<Order> obj = repository.findById(id);
		Optional<Customer> CustomerObj = customerRepository.findById(obj.get().getClient().getId());
		Optional<User> UserObj = userRepository.findById(CustomerObj.get().getClient().getId());
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth.getName().compareTo(UserObj.get().getEmail()) == 0) {
			return obj.orElseThrow(() -> new ResourceNotFoundException(id));
		} else {
			throw new SecurityException("You don't have permission to access this order data");
		}
	}

	@PostMapping
	public Order insert(Order obj) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Boolean hasOperatorRole = auth.getAuthorities().stream()
				.anyMatch(r -> r.getAuthority().equals("ROLE_OPERATOR"));
		if (hasOperatorRole) {
			if (obj.getDeliveryAddress() != null) {
				Optional<User> UserObj = userRepository.findByEmail(auth.getName());
				obj.setClient(UserObj.get().getCustomer());
				obj.getDeliveryAddress().setClient(UserObj.get().getCustomer());
				obj.getDeliveryAddress().setOrder(obj);

				return repository.save(obj);
			}
			else throw new DatabaseException("You need to insert an address");
		}
		else throw new SecurityException("Admins can`t insert orders to database");
	}
	@DeleteMapping
	public void delete(Integer id) {
		Optional<Order> obj = repository.findById(id);
		Optional<Customer> CustomerObj = customerRepository.findById(obj.get().getClient().getId());
		Optional<User> UserObj = userRepository.findById(CustomerObj.get().getClient().getId());
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth.getName().compareTo(UserObj.get().getEmail()) == 0) {
			try {
				repository.deleteById(id);
			} catch (EmptyResultDataAccessException e) {
				throw new ResourceNotFoundException(id);
			} catch (DataIntegrityViolationException e) {
				throw new DatabaseException(e.getMessage());
			}
		} else {
			throw new SecurityException("You don't have permission to access this order data");
		}
	}

	@PutMapping
	public Order update(Integer id, Order obj) {
		Order entity = repository.getReferenceById(id);
		Optional<Customer> CustomerObj = customerRepository.findById(entity.getClient().getId());
		Optional<User> UserObj = userRepository.findById(CustomerObj.get().getClient().getId());
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth.getName().compareTo(UserObj.get().getEmail()) == 0) {

			try {
				updateData(entity, obj);
				return repository.save(entity);
			} catch (EntityNotFoundException e) {
				throw new ResourceNotFoundException(id);
			}
		} else {
			throw new SecurityException("You don't have permission to access this order data");
		}
	}

	private void updateData(Order entity, Order obj) {
		if(obj.getClient() != null){
			entity.setClient(obj.getClient());
		}
		if (obj.getPayment() != null){
			entity.setPayment(obj.getPayment());
		}

		if (obj.getDeliveryAddress() != null) {
			entity.setDeliveryAddress(obj.getDeliveryAddress());
			addressService.insert(obj.getDeliveryAddress());
		}
		entity.setDate(obj.getDate());
	}

}


