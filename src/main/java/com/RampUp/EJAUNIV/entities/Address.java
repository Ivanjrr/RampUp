package com.RampUp.EJAUNIV.entities;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.*;

import com.RampUp.EJAUNIV.entities.enums.AddressType;
import com.RampUp.EJAUNIV.entities.views.AddressView;
import com.RampUp.EJAUNIV.entities.views.CustomerView;
import com.RampUp.EJAUNIV.entities.views.OrderView;
import com.RampUp.EJAUNIV.entities.views.UserView;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;



@Entity
@Table(name = "tb_address")
public class Address implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonView({AddressView.AddressSummary.class,UserView.UserComplete.class, CustomerView.CustomerSummary.class,OrderView.OrderViewSummary.class})
	private Integer id;
	@JsonView({AddressView.AddressSummary.class,CustomerView.CustomerComplete.class,OrderView.OrderViewComplete.class})
	private String street;
	@JsonView({AddressView.AddressSummary.class,CustomerView.CustomerComplete.class,OrderView.OrderViewComplete.class})
	private Integer houseNumber;
	@JsonView({AddressView.AddressSummary.class,CustomerView.CustomerComplete.class,OrderView.OrderViewComplete.class})
	private String neighborhood;
	@JsonView({AddressView.AddressSummary.class,CustomerView.CustomerComplete.class,OrderView.OrderViewComplete.class})
	private String complement;
	@JsonView({AddressView.AddressSummary.class, CustomerView.CustomerSummary.class, UserView.UserComplete.class,OrderView.OrderViewComplete.class})
	private String zipCode;
	@JsonView({AddressView.AddressSummary.class,CustomerView.CustomerComplete.class,OrderView.OrderViewComplete.class})
	private String country;
	@JsonView({AddressView.AddressSummary.class, UserView.UserComplete.class, CustomerView.CustomerSummary.class,OrderView.OrderViewComplete.class})
	private Integer addressType;
	
	@ManyToOne
	@JoinColumn(name = "client_id")
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	@JsonView(AddressView.AddressComplete.class)
	private Customer client;
	@OneToOne
	@JsonView(AddressView.AddressComplete.class)
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private Order order;

	public Address() {
		
	}
	
	public Address(Integer id, String street, Integer houseNumber, String neighborhood, String complement,
			String zipCode, String country, AddressType addressType, Customer client, Order order) {
		super();
		this.id = id;
		this.street = street;
		this.houseNumber = houseNumber;
		this.neighborhood = neighborhood;
		this.complement = complement;
		this.zipCode = zipCode;
		this.country = country;
		setAddressType(addressType);
		this.client = client;
		this.order = order;
	}
	@JsonIgnore
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

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public Integer getHouseNumber() {
		return houseNumber;
	}

	public void setHouseNumber(Integer houseNumber) {
		this.houseNumber = houseNumber;
	}

	public String getNeighborhood() {
		return neighborhood;
	}

	public void setNeighborhood(String neighborhood) {
		this.neighborhood = neighborhood;
	}

	public String getComplement() {
		return complement;
	}

	public void setComplement(String complement) {
		this.complement = complement;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
	
	public AddressType getAddressType() {
		return AddressType.valueOf(addressType);
	}

	public void setAddressType(AddressType addressType) {
		if (addressType != null) {
			this.addressType = addressType.getCode();
		}
	}

	public Customer getClient() {
		return client;
	}

	public void setClient(Customer client) {
		this.client = client;
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
		Address other = (Address) obj;
		return Objects.equals(id, other.id);
	}


	

}
