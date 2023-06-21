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

import com.RampUp.EJAUNIV.entities.TimePeriod;
import com.RampUp.EJAUNIV.repositories.TimePeriodRepository;
import com.RampUp.EJAUNIV.services.exceptions.DatabaseException;
import com.RampUp.EJAUNIV.services.exceptions.ResourceNotFoundException;

@Service
public class TimePeriodService {
	
	@Autowired
	private TimePeriodRepository repository;
	
	public List<TimePeriod> findAll() {
		return repository.findAll();
	}
	@GetMapping
	public TimePeriod findById(Integer id) {
		Optional<TimePeriod> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ResourceNotFoundException(id));
	}
	@PostMapping
	public TimePeriod insert(TimePeriod obj) {
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
	public TimePeriod update(Integer id, TimePeriod obj){
	try {	
		TimePeriod entity = repository.getReferenceById(id);
		updateData(entity, obj);
		return repository.save(entity);
		}
	catch(EntityNotFoundException e) {
		throw new ResourceNotFoundException(id);
	}
	}
	private void updateData(TimePeriod entity, TimePeriod obj) {
		entity.setStartDateTime(obj.getStartDateTime());
		entity.setEndDateTime(obj.getEndDateTime());
		entity.setRetireTime(obj.getRetireTime());
	}
}
