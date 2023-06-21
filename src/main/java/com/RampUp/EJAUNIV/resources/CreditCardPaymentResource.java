package com.RampUp.EJAUNIV.resources;

import java.net.URI;
import java.util.List;

import com.RampUp.EJAUNIV.entities.Order;
import com.RampUp.EJAUNIV.entities.views.OrderView;
import com.RampUp.EJAUNIV.services.OrderService;
import com.fasterxml.jackson.annotation.JsonView;
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

import com.RampUp.EJAUNIV.entities.CreditCardPayment;
import com.RampUp.EJAUNIV.services.CreditCardPaymentService;

@RestController
@RequestMapping(value = "/creditcardpayment")
public class CreditCardPaymentResource {
	@Autowired
	private CreditCardPaymentService service;

	@Autowired
	private OrderService orderService;
	
	@GetMapping
	public ResponseEntity<List<CreditCardPayment>> findAll(){
		List<CreditCardPayment> list = service.findAll();
		return ResponseEntity.ok().body(list);
	}
	@GetMapping(value = "/{id}")
	public ResponseEntity<CreditCardPayment> findById(@PathVariable Integer id)	{
		CreditCardPayment obj = service.findById(id);
		return ResponseEntity.ok().body(obj);
	}
	
	@PostMapping
	@JsonView(OrderView.OrderViewPost.class)
	public ResponseEntity<CreditCardPayment> insert(@RequestBody CreditCardPayment obj) {
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
	public ResponseEntity<CreditCardPayment> update(@PathVariable Integer id, @RequestBody CreditCardPayment obj){
		obj = service.update(id, obj);
		return ResponseEntity.ok().body(obj);
	}
}
