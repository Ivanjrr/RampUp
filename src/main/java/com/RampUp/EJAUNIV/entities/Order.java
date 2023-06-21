package com.RampUp.EJAUNIV.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.*;
import org.springframework.format.annotation.DateTimeFormat;

import com.RampUp.EJAUNIV.entities.views.AddressView;
import com.RampUp.EJAUNIV.entities.views.CustomerView;
import com.RampUp.EJAUNIV.entities.views.OrderView;
import com.RampUp.EJAUNIV.entities.views.UserView;
import com.fasterxml.jackson.annotation.JsonView;


@Entity
@Table(name = "tb_order")
@SQLDelete(sql = "UPDATE tb_order SET deleted = 1 WHERE id=?")
@Where(clause = "deleted=0")
public class Order implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonView({UserView.UserComplete.class, CustomerView.CustomerSummary.class, OrderView.OrderViewSummary.class, OrderView.OrderViewPost.class})
	private Integer id;
	@JsonView({UserView.UserComplete.class, CustomerView.CustomerSummary.class, OrderView.OrderViewSummary.class})
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date instant;
	@ManyToOne
	@JoinColumn(name = "client_id")
	@JsonView({OrderView.OrderViewSummary.class})
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private Customer client;
	@OneToMany(mappedBy = "id.order",cascade = CascadeType.REMOVE)
	@JsonView({CustomerView.CustomerComplete.class, UserView.UserComplete.class,OrderView.OrderViewSummary.class})
	private Set<OrderItem> products = new HashSet<>();
	@OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
	@JsonView({CustomerView.CustomerSummary.class, UserView.UserComplete.class,OrderView.OrderViewSummary.class})
	private Payment payment;
	@JsonView({CustomerView.CustomerSummary.class, UserView.UserComplete.class,OrderView.OrderViewSummary.class})
	@OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private Address deliveryAddress;

	private Boolean deleted = Boolean.FALSE;

	
	public Order() {	
	}

	public Order(Integer id, Date instant, Customer client, Payment payment, Address deliveryAddress) {
		super();
		this.id = id;
		this.instant = instant;
		this.client = client;
		this.payment = payment;
		this.deliveryAddress = deliveryAddress;
	}



	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getDate() {
		return instant;
	}

	public void setDate(Date instant) {
		this.instant = instant;
	}

	public Customer getClient() {
		return client;
	}

	public void setClient(Customer client) {
		this.client = client;
	}
	
	public Set<OrderItem> getOrders(){
		return products;
	}

	public Payment getPayment() {
		return payment;
	}

	public void setPayment(Payment payment) {
		this.payment = payment;
	}

	public Address getDeliveryAddress() {
		return deliveryAddress;
	}

	public void setDeliveryAddress(Address deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Order other = (Order) obj;
		return Objects.equals(id, other.id);
	}


	
	
	

}
