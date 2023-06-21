package com.RampUp.EJAUNIV.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.RampUp.EJAUNIV.entities.views.AddressView;
import com.RampUp.EJAUNIV.entities.views.CustomerView;
import com.RampUp.EJAUNIV.entities.views.UserView;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "tb_user")
@SQLDelete(sql = "UPDATE tb_user SET deleted = 1 WHERE id=?")
@Where(clause = "deleted=0")
public class User implements Serializable{

	private static final long serialVersionUID = 1L;
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonView({AddressView.AddressComplete.class,UserView.UserSummary.class, CustomerView.CustomerSummary.class})
	private Integer id;
	@JsonView({AddressView.AddressComplete.class,UserView.UserSummary.class, CustomerView.CustomerSummary.class})
	private String email;
	@JsonView()
	private String password;
	@JsonView({AddressView.AddressComplete.class,UserView.UserSummary.class})
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	@OneToOne(mappedBy = "user", fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
	private Customer client;
	@JsonView({AddressView.AddressComplete.class,UserView.UserSummary.class, CustomerView.CustomerSummary.class})
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "tb_user_roles" ,
	joinColumns = @JoinColumn(name = "user_id"),
	inverseJoinColumns = @JoinColumn(name = "roles_id")	)
	private List<Role> roles = new ArrayList<>();

	private Boolean deleted = Boolean.FALSE;

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	public User() {
		
	}

	public User(Integer id, String email, String password, Customer client) {
		super();
		this.id = id;
		this.email = email;
		this.password = password;
		this.client = client;

		
	}
	
	public User(User user) {
		super();
		this.id = user.id;
		this.email = user.email;
		this.password = user.password;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	@JsonIgnore
	public Customer getCustomer() {
		return client;
	}

	public void setCustomer(Customer customer) {
		this.client = customer;
	}
	@JsonIgnore
	public void setRole(List<Role> roles) {
		this.roles = roles;
	}

	@JsonIgnore
	public List<Role> getRole(){
		return roles;
		
	}
	
	@JsonIgnore
	public void addRole(Role role){
		this.roles.add(role);
	}
	
	@JsonIgnore
    public String[] getRoleString()
    {
       String[] roleString = new String[roles.size()];
       int index = 0;
       for (Role role : roles)
       {
          roleString[index++] = role.getAuthorities().name();
       }
       return roleString;}
	
	
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
		User other = (User) obj;
		return Objects.equals(id, other.id);
	}
	
	
}
