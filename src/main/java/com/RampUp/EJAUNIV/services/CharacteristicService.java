package com.RampUp.EJAUNIV.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import com.RampUp.EJAUNIV.entities.enums.CharacteristicType;
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

import com.RampUp.EJAUNIV.entities.Characteristic;
import com.RampUp.EJAUNIV.repositories.CharacteristicRepository;
import com.RampUp.EJAUNIV.services.exceptions.DatabaseException;
import com.RampUp.EJAUNIV.services.exceptions.ResourceNotFoundException;

@Service
public class CharacteristicService {
	
	@Autowired
	private CharacteristicRepository repository;
	
	public List<Characteristic> findAll() {
		List<Characteristic> listall = repository.findAll();
		List<Characteristic> list = new ArrayList<>();
			for(Characteristic characteristic : listall){
				if(characteristic.getType().equals(CharacteristicType.USER)){;
					list.add(characteristic);}
				}
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Boolean hasOperatorRole = auth.getAuthorities().stream()
				.anyMatch(r -> r.getAuthority().equals("ROLE_OPERATOR"));
			if(hasOperatorRole){
				return list;}
			else	{
				return listall;
	}
	}
	@GetMapping
	public Characteristic findById(Integer id) {
		Optional<Characteristic> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ResourceNotFoundException(id));
	}
	@PostMapping
	public Characteristic insert(Characteristic obj) {
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
	public Characteristic update(Integer id, Characteristic obj){
	try {	
		Characteristic entity = repository.getReferenceById(id);
		updateData(entity, obj);
		return repository.save(entity);
		}
	catch(EntityNotFoundException e) {
		throw new ResourceNotFoundException(id);
	}
	}
	private void updateData(Characteristic entity, Characteristic obj) {
		entity.setName(obj.getName());
		entity.setValidFor(obj.getValidFor());
		entity.setType(obj.getType());
		entity.setValueType(obj.getValueType());
	}
}
