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

import com.RampUp.EJAUNIV.entities.Address;
import com.RampUp.EJAUNIV.entities.views.AddressView;
import com.RampUp.EJAUNIV.services.AddressService;
import com.fasterxml.jackson.annotation.JsonView;


@RestController
@RequestMapping(value = "/addresses")
public class AddressResource {
	@Autowired
	private AddressService service;
	
	@GetMapping
	@JsonView(AddressView.AddressSummary.class)
	public ResponseEntity<List<Address>> findAll(){
		List<Address> list = service.findAll();
		return ResponseEntity.ok().body(list);
	}
	@GetMapping(value = "/{id}")
	@JsonView(AddressView.AddressComplete.class)
	public ResponseEntity<Address> findById(@PathVariable Integer id)	{
		Address obj = service.findById(id);
		return ResponseEntity.ok().body(obj);
	}
	
	@PostMapping
	@JsonView(AddressView.AddressComplete.class)
	public ResponseEntity<Address> insert(@RequestBody Address obj) {
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
	public ResponseEntity<Address> update(@PathVariable Integer id, @RequestBody Address obj){
		obj = service.update(id, obj);
		return ResponseEntity.ok().body(obj);
	}
}