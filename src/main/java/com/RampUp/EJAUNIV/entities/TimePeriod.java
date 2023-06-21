package com.RampUp.EJAUNIV.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "tb_timeperiod")
	public class TimePeriod implements Serializable {
		private static final long serialVersionUID = 1L;
	
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Integer id;
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM--dd'T'HH:mm:ss'Z'", timezone = "GMT")
		private Date startDateTime;
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM--dd'T'HH:mm:ss'Z'", timezone = "GMT")
		private Date endDateTime;
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM--dd'T'HH:mm:ss'Z'", timezone = "GMT")
		private Date retireTime;
	
	
		@OneToOne(mappedBy = "validFor", cascade = CascadeType.ALL)
		private Characteristic characteristic;
	
		
		@OneToOne(mappedBy = "validFor", cascade = CascadeType.ALL)
		private ProductOffering productOffering;
	
		public TimePeriod() {	
		}
	
		public TimePeriod(Date startDateTime, Date endDateTime, Date retireTime) {
			super();
			this.startDateTime = startDateTime;
			this.endDateTime = endDateTime;
			this.retireTime = retireTime;
;
		}
		
		public TimePeriod(Date startDateTime, Date endDateTime, Date retireTime, ProductOffering productOffering) {
			super();
			this.startDateTime = startDateTime;
			this.endDateTime = endDateTime;
			this.retireTime = retireTime;
			this.setProductOffering(productOffering);
		}
	
		public TimePeriod(Date startDateTime, Date endDateTime, Date retireTime, Characteristic characteristic) {
			super();
			this.startDateTime = startDateTime;
			this.endDateTime = endDateTime;
			this.retireTime = retireTime;
			this.setCharacteristic(characteristic);
		}
	
		public Date getStartDateTime() {
			return startDateTime;
		}
		public void setStartDateTime(Date startDateTime) {
			this.startDateTime = startDateTime;
		}
		public Date getEndDateTime() {
			return endDateTime;
		}
		public void setEndDateTime(Date endDateTime) {
			this.endDateTime = endDateTime;
		}
		public Date getRetireTime() {
			return retireTime;
		}
		public void setRetireTime(Date retireTime) {
			this.retireTime = retireTime;
		}
	
		public Characteristic getCharacteristic() {
			return characteristic;
		}
	
		public void setCharacteristic(Characteristic characteristic) {
			this.characteristic = characteristic;
		}
	
		public ProductOffering getProductOffering() {
			return productOffering;
		}
	
		public void setProductOffering(ProductOffering productOffering) {
			this.productOffering = productOffering;
		}
	
		public Integer getId() {
			return id;
		}
	
		public void setId(Integer id) {
			this.id = id;
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
			TimePeriod other = (TimePeriod) obj;
			return Objects.equals(id, other.id);
		}}