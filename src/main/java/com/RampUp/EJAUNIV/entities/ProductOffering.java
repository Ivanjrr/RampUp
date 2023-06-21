package com.RampUp.EJAUNIV.entities;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.RampUp.EJAUNIV.entities.enums.POState;
import com.RampUp.EJAUNIV.entities.views.CustomerView;
import com.RampUp.EJAUNIV.entities.views.OrderView;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
@Entity
@Table(name = "tb_productoffering")
public class ProductOffering implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonView({CustomerView.CustomerComplete.class,OrderView.OrderViewSummary.class})
	private Integer id;
	@JsonView({CustomerView.CustomerComplete.class,OrderView.OrderViewSummary.class})
	private String name;
	private Boolean sellIndicator;
	
	@OneToOne
	@JoinColumn(name = "ProductOffering_ValidFor")
	private TimePeriod validFor;
	
	private Integer state;
	
	@ManyToMany
	@JoinTable(name = "tb_product_characteristic",joinColumns = @JoinColumn(name = "productoffering_id"), inverseJoinColumns = @JoinColumn(name= "characteristic_id"))
	@JsonView(OrderView.OrderViewComplete.class)
	private Set<Characteristic> characteristics = new HashSet<>();
	
	@OneToMany(mappedBy = "id.product")
	private Set<OrderItem> orders = new HashSet<>();
	
	public ProductOffering() {	
	}

	public ProductOffering(Integer id, String name, Boolean sellIndicator, TimePeriod validFor, POState state) {
		super();
		this.id = id;
		this.name = name;
		this.sellIndicator = sellIndicator;
		this.validFor = validFor;
		setState(state);
	}
	@JsonIgnore
	public Set<Characteristic> getCharacteristics() {
		return characteristics;
	}

	public void setCharacteristics(Set<Characteristic> characteristics) {
		this.characteristics = characteristics;
	}


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getSellIndicator() {
		return sellIndicator;
	}

	public void setSellIndicator(Boolean sellIndicator) {
		this.sellIndicator = sellIndicator;
	}

	public TimePeriod getValidFor() {
		return validFor;
	}

	public void setValidFor(TimePeriod validFor) {
		this.validFor = validFor;
	}
	
	public POState getState() {
		return POState.valueOf(state);
	}

	public void setState(POState state) {
		if (state != null) {
			this.state = state.getCode();
		}
	}
	
	@JsonIgnore
	public Set<Order> getOrders(){
		Set<Order> set = new HashSet<>();
		for (OrderItem x : orders) {
		set.add(x.getOrder());
		}
		return set;
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
		ProductOffering other = (ProductOffering) obj;
		return Objects.equals(id, other.id);
	}

	}
