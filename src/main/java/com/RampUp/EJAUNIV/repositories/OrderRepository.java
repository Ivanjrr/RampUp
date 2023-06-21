package com.RampUp.EJAUNIV.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.RampUp.EJAUNIV.entities.Order;

public interface OrderRepository extends JpaRepository<Order, Integer> {

}
