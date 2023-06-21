package com.RampUp.EJAUNIV.services;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import com.RampUp.EJAUNIV.entities.BankSlipPayment;
import com.RampUp.EJAUNIV.repositories.BankSlipPaymentRepository;
import com.RampUp.EJAUNIV.services.exceptions.DatabaseException;
import com.RampUp.EJAUNIV.services.exceptions.ResourceNotFoundException;

@Service
public class BankSlipPaymentService {
	
	@Autowired
	private BankSlipPaymentRepository repository;
	
	public List<BankSlipPayment> findAll() {
		return repository.findAll();
	}
	@GetMapping
	public BankSlipPayment findById(Integer id) {
		Optional<BankSlipPayment> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ResourceNotFoundException(id));
	}
	@PostMapping
	public BankSlipPayment insert(BankSlipPayment obj) {
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
	public BankSlipPayment update(Integer id, BankSlipPayment obj){
	try {	
		BankSlipPayment entity = repository.getReferenceById(id);
		updateData(entity, obj);
		return repository.save(entity);
		}
	catch(EntityNotFoundException e) {
		throw new ResourceNotFoundException(id);
	}
	}
	private void updateData(BankSlipPayment entity, BankSlipPayment obj) {
		entity.setOrder(obj.getOrder());
		entity.setStartDate(obj.getStartDate());
		entity.setDueDate(obj.getDueDate());
		entity.setPaymentState(obj.getPaymentState());
	}
}