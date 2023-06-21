package com.RampUp.EJAUNIV.services;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import com.RampUp.EJAUNIV.entities.Order;
import com.RampUp.EJAUNIV.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import com.RampUp.EJAUNIV.entities.CreditCardPayment;
import com.RampUp.EJAUNIV.repositories.CreditCardPaymentRepository;
import com.RampUp.EJAUNIV.services.exceptions.DatabaseException;
import com.RampUp.EJAUNIV.services.exceptions.ResourceNotFoundException;

@Service
public class CreditCardPaymentService {
	
	@Autowired
	private CreditCardPaymentRepository repository;

	@Autowired
	private OrderRepository orderRepository;
	
	public List<CreditCardPayment> findAll() {
		return repository.findAll();
	}
	@GetMapping
	public CreditCardPayment findById(Integer id) {
		Optional<CreditCardPayment> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ResourceNotFoundException(id));
	}

	@PostMapping
	public CreditCardPayment insert(CreditCardPayment obj)
	{
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
	public CreditCardPayment update(Integer id, CreditCardPayment obj){
	try {	
		CreditCardPayment entity = repository.getReferenceById(id);
		updateData(entity, obj);
		return repository.save(entity);
		}
	catch(EntityNotFoundException e) {
		throw new ResourceNotFoundException(id);
	}
	}
	private void updateData(CreditCardPayment entity, CreditCardPayment obj) {
		entity.setInstallments(obj.getInstallments());
		entity.setOrder(obj.getOrder());
		entity.setPaymentState(obj.getPaymentState());
	}
}