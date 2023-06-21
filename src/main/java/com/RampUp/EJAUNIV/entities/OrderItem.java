package com.RampUp.EJAUNIV.entities;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.RampUp.EJAUNIV.entities.pk.OrderItemPK;
import com.RampUp.EJAUNIV.entities.views.CustomerView;
import com.RampUp.EJAUNIV.entities.views.OrderView;
import com.RampUp.EJAUNIV.entities.views.UserView;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

@Entity
@Table(name = "tb_order_item")
public class OrderItem implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	@JsonView({CustomerView.CustomerComplete.class, OrderView.OrderViewSummary.class})
	private OrderItemPK id = new OrderItemPK();
	@JsonView({CustomerView.CustomerComplete.class,OrderView.OrderViewComplete.class, OrderView.OrderViewPost.class})
	private Double discount;
	@JsonView({CustomerView.CustomerComplete.class,OrderView.OrderViewComplete.class, OrderView.OrderViewPost.class})
	private Integer quantity;
	@JsonView({CustomerView.CustomerComplete.class, OrderView.OrderViewSummary.class, OrderView.OrderViewPost.class})
	private Double totalPrice;
	
	public OrderItem() {
		
	}
	
	public OrderItem(ProductOffering product, Order order, Double discount, Integer quantity, Double totalPrice) {
		super();
		id.setProduct(product);
		id.setOrder(order);
		this.discount = discount;
		this.quantity = quantity;
		this.totalPrice = totalPrice;
	}
	
	@JsonIgnore
	public Order getOrder() {
		return id.getOrder();
	}
	
	public void setOrder (Order order) {
		id.setOrder(order);
	}
	
	public ProductOffering getProduct() {
		return id.getProduct();
	}
	
	public void setProduct (ProductOffering product) {
		id.setProduct(product);
	}


	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
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
		OrderItem other = (OrderItem) obj;
		return Objects.equals(id, other.id);
	}
	
	

}
