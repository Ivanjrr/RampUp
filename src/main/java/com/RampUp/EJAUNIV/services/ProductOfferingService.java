package com.RampUp.EJAUNIV.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import com.RampUp.EJAUNIV.entities.Characteristic;
import com.RampUp.EJAUNIV.entities.enums.CharacteristicType;
import com.RampUp.EJAUNIV.entities.enums.POState;
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

import com.RampUp.EJAUNIV.entities.ProductOffering;
import com.RampUp.EJAUNIV.repositories.ProductOfferingRepository;
import com.RampUp.EJAUNIV.services.exceptions.DatabaseException;
import com.RampUp.EJAUNIV.services.exceptions.ResourceNotFoundException;

@Service
public class ProductOfferingService {
	
	@Autowired
	private ProductOfferingRepository repository;
	
	public List<ProductOffering> findAll() {
		List<ProductOffering> listall = repository.findAll();
		List<ProductOffering> list = new ArrayList<>();
		for(ProductOffering productOffering : listall){
			if(productOffering.getState().equals(POState.ACTIVE)){;
				list.add(productOffering);}
		}
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Boolean hasOperatorRole = auth.getAuthorities().stream()
				.anyMatch(r -> r.getAuthority().equals("ROLE_OPERATOR"));
		if(hasOperatorRole){
			return list;}
		else {
			return repository.findAll();
		}
	}

	@GetMapping
	public ProductOffering findById(Integer id) {
		Optional<ProductOffering> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ResourceNotFoundException(id));
	}
	@PostMapping
	public ProductOffering insert(ProductOffering obj) {

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
	public ProductOffering update(Integer id, ProductOffering obj){
	try {	
		ProductOffering entity = repository.getReferenceById(id);
		updateData(entity, obj);
		return repository.save(entity);
		}
	catch(EntityNotFoundException e) {
		throw new ResourceNotFoundException(id);
	}
	}
	
	private void updateData(ProductOffering entity, ProductOffering obj) {
		if (obj.getName() != null) {
			entity.setName(obj.getName());
		}
		if (obj.getSellIndicator() != null) {
			entity.setSellIndicator(obj.getSellIndicator());
		}
		if (obj.getState() != null) {
			entity.setState(obj.getState());
		}
		if (obj.getValidFor() != null) {
			entity.setValidFor(obj.getValidFor());
		}
		if (obj.getCharacteristics() != null) {
			entity.setCharacteristics(obj.getCharacteristics());
		}
	}
}

