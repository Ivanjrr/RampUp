package com.RampUp.EJAUNIV.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.RampUp.EJAUNIV.entities.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {

}
