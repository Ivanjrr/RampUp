package com.RampUp.EJAUNIV.resources;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.RampUp.EJAUNIV.entities.Customer;
import com.RampUp.EJAUNIV.entities.views.CustomerView;
import com.RampUp.EJAUNIV.services.CustomerService;
import com.fasterxml.jackson.annotation.JsonView;

@RestController
@RequestMapping(value = "/customers")
public class CustomerResource {
	@Autowired
	private CustomerService service;
	
	@GetMapping
	@JsonView(CustomerView.CustomerSummary.class)
	public ResponseEntity<List<Customer>> findAll(){
		List<Customer> list = service.findAll();
		return ResponseEntity.ok().body(list);
	}
	@GetMapping(value = "/{id}")
	@JsonView(CustomerView.CustomerComplete.class)
	public ResponseEntity<Customer> findById(@PathVariable Integer id)	{
		Customer obj = service.findById(id);
		return ResponseEntity.ok().body(obj);
	}
	
	@PostMapping
	@JsonView(CustomerView.CustomerSummary.class)
	public ResponseEntity<Customer> insert(@RequestBody Customer obj) {
			obj = service.insert(obj);
			URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
			
			return ResponseEntity.created(uri).body(obj);
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	@PutMapping(value = "/{id}")
	public ResponseEntity<Customer> update(@PathVariable Integer id, @RequestBody Customer obj){
		obj = service.update(id, obj);
		return ResponseEntity.ok().body(obj);
	}
}
