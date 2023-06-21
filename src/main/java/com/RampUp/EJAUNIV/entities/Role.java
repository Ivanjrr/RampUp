package com.RampUp.EJAUNIV.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.RampUp.EJAUNIV.entities.enums.Authorities;
import com.RampUp.EJAUNIV.entities.views.AddressView;
import com.RampUp.EJAUNIV.entities.views.CustomerView;
import com.RampUp.EJAUNIV.entities.views.UserView;
import com.fasterxml.jackson.annotation.JsonView;

@Entity
@Table(name = "tb_roles")
public class Role implements Serializable {
	private static final long serialVersionUID = 1L; 

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonView({AddressView.AddressComplete.class,UserView.UserComplete.class, CustomerView.CustomerComplete.class})
	private Integer id;
	@ManyToMany(mappedBy = "roles")
	private List<User> user = new ArrayList<>();
	@JsonView({AddressView.AddressComplete.class,UserView.UserSummary.class, CustomerView.CustomerSummary.class})
	private Integer authorities;
	
	
	public Role() {
		
	}
	
	public Role(Integer id, Authorities authorities) {
		super();
		this.id = id;
		setAuthorities(authorities);
	
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Authorities getAuthorities() {
		return Authorities.valueOf(authorities);
	}

	public void setAuthorities(Authorities authorities) {
		if (authorities != null) {
			this.authorities = authorities.getCode();
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
		Role other = (Role) obj;
		return Objects.equals(id, other.id);
	}
	
	
}
