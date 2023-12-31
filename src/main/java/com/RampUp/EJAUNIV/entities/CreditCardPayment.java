package com.RampUp.EJAUNIV.entities;

import java.util.Objects;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.RampUp.EJAUNIV.entities.enums.PaymentState;
import com.RampUp.EJAUNIV.entities.views.CustomerView;
import com.RampUp.EJAUNIV.entities.views.OrderView;
import com.RampUp.EJAUNIV.entities.views.UserView;
import com.fasterxml.jackson.annotation.JsonView;

@Entity
@DiscriminatorValue(value = "CreditCardPayment")
public class CreditCardPayment extends Payment {
	private static final long serialVersionUID = 1L;
	@JsonView({CustomerView.CustomerComplete.class,OrderView.OrderViewComplete.class, OrderView.OrderViewPost.class})
	private Integer installments;

	public CreditCardPayment() {
		
	}
	
	public CreditCardPayment(Integer id, PaymentState paymentState, Order order, Integer installments) {
		super(id, paymentState, order);
		this.installments = installments;
	}

	public Integer getInstallments() {
		return installments;
	}

	public void setInstallments(Integer installments) {
		this.installments = installments;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(installments);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		CreditCardPayment other = (CreditCardPayment) obj;
		return Objects.equals(installments, other.installments);
	}
	
	

}
