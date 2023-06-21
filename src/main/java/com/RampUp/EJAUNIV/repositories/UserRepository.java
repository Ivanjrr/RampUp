package com.RampUp.EJAUNIV.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.RampUp.EJAUNIV.entities.User;
import org.springframework.stereotype.Repository;

public interface UserRepository extends JpaRepository<User, Integer> {
	Optional<User>findByEmail(String email);

}
