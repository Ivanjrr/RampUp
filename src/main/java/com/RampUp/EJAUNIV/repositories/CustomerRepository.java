package com.RampUp.EJAUNIV.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.RampUp.EJAUNIV.entities.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {

}
