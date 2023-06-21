package com.RampUp.EJAUNIV.services;

import java.util.*;

import javax.persistence.EntityNotFoundException;

import com.RampUp.EJAUNIV.entities.enums.Authorities;
import com.RampUp.EJAUNIV.services.exceptions.SecurityException;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import com.RampUp.EJAUNIV.entities.Role;
import com.RampUp.EJAUNIV.entities.User;
import com.RampUp.EJAUNIV.repositories.RoleRepository;
import com.RampUp.EJAUNIV.repositories.UserRepository;
import com.RampUp.EJAUNIV.services.exceptions.DatabaseException;
import com.RampUp.EJAUNIV.services.exceptions.ResourceNotFoundException;

@Service
public class UserService implements UserDetailsService {
	
	@Autowired
	private UserRepository repository;

	@Autowired
	private RoleRepository roleRepository;
	
	private PasswordEncoder passwordEncoder;


	public List<User> findAll() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Boolean hasAdminRole = auth.getAuthorities().stream()
				.anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"));


			if (hasAdminRole){
			return repository.findAll();

		}
			else throw new SecurityException("You don't have permission to execute this command");
	}

	@GetMapping
	public User findById(Integer id) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Optional<User> obj = repository.findById(id);

		if(auth.getName().compareTo(obj.get().getEmail()) == 0) {


			return obj.orElseThrow(() -> new ResourceNotFoundException(id));
		}
		else {
			throw new SecurityException("You don't have permission to access this user data");
		}
		}
	@PostMapping
	public User insert(User obj) {
		if (obj.getEmail() == null) {
			throw new DatabaseException("You need to insert a email");

		}
		else if (obj.getPassword() == null) {
			throw new DatabaseException("You need a password");
		}
		else {
			passwordEncoder = new BCryptPasswordEncoder();

			obj.setPassword(passwordEncoder.encode(obj.getPassword()));
			return repository.save(obj);
		}

	}
	@DeleteMapping
	public void delete(Integer id) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Optional<User> obj = repository.findById(id);
		if(auth.getName().compareTo(obj.get().getEmail()) == 0) {
			try {
				repository.deleteById(id);
			} catch (EmptyResultDataAccessException e) {
				throw new ResourceNotFoundException(id);
			} catch (DataIntegrityViolationException e) {
				throw new DatabaseException(e.getMessage());
			}
		}
		else {
			throw new SecurityException("You don't have permission to access this user data");
		}
	}
	@PutMapping
	public User update(Integer id, User obj){
		User entity = repository.getReferenceById(id);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth.getName().compareTo(obj.getEmail())==0){
			try {
				updateData(entity, obj);
				return repository.save(entity);
			} catch (EntityNotFoundException e) {
				throw new ResourceNotFoundException(id);
			}
		}
		else {
			throw new SecurityException("You don't have permission to access this user data");
		}
	}
	
	@PutMapping
	public User updateRole(Integer id){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Boolean hasAdminRole = auth.getAuthorities().stream()
				.anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"));
		if (hasAdminRole) {

			try {
				User entity = repository.getReferenceById(id);
				Optional<Role> role = roleRepository.findById(1);
				List<Role> roles = new ArrayList<>();
				roles.add(role.get());
				entity.setRole(roles);
				return repository.save(entity);
			} catch (EntityNotFoundException e) {
				throw new ResourceNotFoundException(id);
			}
		}
		else {
			throw new SecurityException("You don't have permission to access this user data");
		}
	}
	private void updateData(User entity, User obj) {
		if(obj.getEmail() != null){
		entity.setEmail(obj.getEmail());}
		if(obj.getPassword() != null) {
			passwordEncoder = new BCryptPasswordEncoder();
			entity.setPassword(passwordEncoder.encode(obj.getPassword()));}
		entity.setCustomer(obj.getCustomer());
	}
	
	private void changeRole(User entity, User obj) {
		entity.setRole(obj.getRole());
	}
	
	@PostMapping
	public User insertOperator(User obj) {
		if (obj.getEmail() == null) {
			throw new DatabaseException("You need to insert a email");

		} else if (obj.getPassword() == null) {
			throw new DatabaseException("You need a password");
		} else {
			Optional<Role> role = roleRepository.findById(2);
			List<Role> roles = new ArrayList<>();
			roles.add(role.get());
			obj.setRole(roles);
			passwordEncoder = new BCryptPasswordEncoder();

			obj.setPassword(passwordEncoder.encode(obj.getPassword()));

			return repository.save(obj);
		}
	}
	
	
	// Security Configuration
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {



       User user = repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Non-Existent Login"));



       if (user.getRoleString() == null || user.getRoleString().length == 0) {
            throw new UsernameNotFoundException("Non-Existent Login");
        }



       return org.springframework.security.core.userdetails.User.builder().username(user.getEmail())
                .password(user.getPassword())
                // .roles("2")
                .roles(user.getRoleString()) // Adicionar Fetch no User.roles
                .build();
        //
    }
	



	private static Collection<? extends GrantedAuthority> getAuthorities(User user) {
	String[] userRoles = user.getRole().stream().map((role) -> role.getAuthorities()).toArray(String[]:: new);
	Collection<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(userRoles);
	return authorities;
}}