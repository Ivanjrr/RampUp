package com.RampUp.EJAUNIV.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.*;

import com.RampUp.EJAUNIV.entities.enums.CustomerType;
import com.RampUp.EJAUNIV.entities.views.AddressView;
import com.RampUp.EJAUNIV.entities.views.CustomerView;
import com.RampUp.EJAUNIV.entities.views.OrderView;
import com.RampUp.EJAUNIV.entities.views.UserView;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "tb_customer")
@SQLDelete(sql = "UPDATE tb_customer SET deleted = 1 WHERE id=?")
@Where(clause = "deleted=0")
public class Customer implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonView({AddressView.AddressComplete.class,UserView.UserSummary.class,CustomerView.CustomerSummary.class,OrderView.OrderViewSummary.class, OrderView.OrderViewPost.class})
	private Integer id;
	@JsonView({AddressView.AddressComplete.class,UserView.UserSummary.class,CustomerView.CustomerSummary.class,OrderView.OrderViewSummary.class,AddressView.AddressComplete.class})
	private String customerName;
	@JsonView({AddressView.AddressComplete.class,UserView.UserComplete.class,CustomerView.CustomerSummary.class,OrderView.OrderViewComplete.class})
	private String documentNumber;
	@JsonView({AddressView.AddressComplete.class,UserView.UserComplete.class,CustomerView.CustomerSummary.class,OrderView.OrderViewComplete.class, AddressView.AddressComplete.class})
	private String customerStatus;
	@JsonView({AddressView.AddressComplete.class,UserView.UserComplete.class,CustomerView.CustomerSummary.class,OrderView.OrderViewComplete.class})
	private String creditScore;
	@JsonView({UserView.UserComplete.class,CustomerView.CustomerSummary.class,OrderView.OrderViewSummary.class, AddressView.AddressComplete.class})
	private Integer customerType;
	@JsonView({AddressView.AddressComplete.class,CustomerView.CustomerSummary.class})

	@OneToOne(orphanRemoval = true)
	private User user;
	@JsonView({CustomerView.CustomerSummary.class, OrderView.OrderViewSummary.class})
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	@OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
	List<Address> addresses = new ArrayList<>();
	@JsonView({CustomerView.CustomerSummary.class})
	@OneToMany(mappedBy = "client",cascade = CascadeType.ALL)
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	List<Order> orders = new ArrayList<>();

	private Boolean deleted = Boolean.FALSE;

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	public Customer () {
		
	}

	public Customer(Integer id, String customerName, String documentNumber, String customerStatus, String creditScore,
			String password, CustomerType customerType) {
		this.id = id;
		this.customerName = customerName;
		this.documentNumber = documentNumber;
		this.customerStatus = customerStatus;
		this.creditScore = creditScore;
		setCustomerType(customerType);
	}
	@JsonIgnore
	public User getClient() {
		return user;
	}

	public void setClient(User client) {
		this.user = client;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getDocumentNumber() {
		return documentNumber;
	}

	public void setDocumentNumber(String documentNumber) {
		this.documentNumber = documentNumber;
	}

	public String getCustomerStatus() {
		return customerStatus;
	}

	public void setCustomerStatus(String customerStatus) {
		this.customerStatus = customerStatus;
	}

	public String getCreditScore() {
		return creditScore;
	}

	public void setCreditScore(String creditScore) {
		this.creditScore = creditScore;
	}


	@JsonIgnore
	public CustomerType getCustomerType() {
		return CustomerType.valueOf(customerType);
	}
	
	public void setCustomerType(CustomerType customerType) {
		if(customerType != null) {
			this.customerType = customerType.getCode();
		}
	}

	@JsonIgnore
	public List<Address> getAddress() {
		return addresses;
	}
	@JsonIgnore
	public List<Order> getOrders() {
		return orders;
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
		Customer other = (Customer) obj;
		return Objects.equals(id, other.id);
	}
	
	
	

}
