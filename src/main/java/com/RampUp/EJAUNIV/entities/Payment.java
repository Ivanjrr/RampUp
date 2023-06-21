package com.RampUp.EJAUNIV.entities;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.RampUp.EJAUNIV.entities.enums.PaymentState;
import com.RampUp.EJAUNIV.entities.views.CustomerView;
import com.RampUp.EJAUNIV.entities.views.OrderView;
import com.RampUp.EJAUNIV.entities.views.UserView;
import com.fasterxml.jackson.annotation.JsonView;

@Entity
@Table(name = "tb_payment")
public abstract class Payment implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonView( OrderView.OrderViewPost.class)
	private Integer id;
	@JsonView({CustomerView.CustomerSummary.class, OrderView.OrderViewSummary.class, OrderView.OrderViewPost.class})
	private Integer paymentState;
	
	@OneToOne
	@JsonView(OrderView.OrderViewPost.class)
	private Order order;
	
	public Payment() {
		
	}
	
	
	
	public Payment(Integer id, PaymentState paymentState, Order order) {
		super();
		this.id = id;
		setPaymentState(paymentState);
		this.order = order;
	}

	

	public Order getOrder() {
		return order;
	}



	public void setOrder(Order order) {
		this.order = order;
	}



	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public PaymentState getPaymentState() {
		return PaymentState.valueOf(paymentState);
	}

	public void setPaymentState(PaymentState paymentState) {
		if (paymentState != null) {
			this.paymentState = paymentState.getCode();
		}
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
		Payment other = (Payment) obj;
		return Objects.equals(id, other.id);
	}
	
	
}
