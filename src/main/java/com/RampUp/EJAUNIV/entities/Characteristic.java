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
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.RampUp.EJAUNIV.entities.enums.CharacteristicType;
import com.RampUp.EJAUNIV.entities.views.OrderView;
import com.fasterxml.jackson.annotation.JsonView;

@Entity
@Table(name = "tb_characteristic")
public class Characteristic implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonView(OrderView.OrderViewComplete.class)
	private Integer id;
	@JsonView(OrderView.OrderViewComplete.class)
	private String name;
	@JsonView(OrderView.OrderViewComplete.class)
	private String valueType;
	@JsonView(OrderView.OrderViewComplete.class)
	private Integer Type;
	
	@ManyToMany(mappedBy = "characteristics")
	private Set<ProductOffering> productOfferings = new HashSet<>();
	
	@OneToOne
	@JoinColumn(name = "Characteristic_ValidFor")
	private TimePeriod validFor;
	
	public Characteristic() {
	}

	public Characteristic(Integer id, String name, String valueType, CharacteristicType Type, TimePeriod validFor) {
		super();
		this.id = id;
		this.name = name;
		this.valueType = valueType;
		setType(Type);
		this.validFor = validFor;
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

	public String getValueType() {
		return valueType;
	}

	public void setValueType(String valueType) {
		this.valueType = valueType;
	}

	public CharacteristicType getType() {
		return CharacteristicType.valueOf(Type);
	}

	public void setType(CharacteristicType Type) {
		if (Type != null) {
			this.Type = Type.getCode();
		}
	}
	
	public TimePeriod getValidFor() {
		return validFor;
	}
	
	public void setValidFor(TimePeriod validFor) {
		this.validFor = validFor;
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
		Characteristic other = (Characteristic) obj;
		return Objects.equals(id, other.id);
	}
}
