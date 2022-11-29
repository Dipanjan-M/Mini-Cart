package com.miniCart.customer.services;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.miniCart.customer.entities.Customer;
import com.miniCart.customer.entities.Role;
import com.miniCart.customer.repos.CustomerRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private CustomerRepository custRepo;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Optional<Customer> optionalCust = this.custRepo.findByEmail(email);
		optionalCust.orElseThrow(() -> new UsernameNotFoundException("No customer registered with email : " + email));
		Customer customer = optionalCust.get();
		User user = new User(customer.getEmail(), customer.getPassword(), getAuthorities(customer.getRole()));
		return user;
	}

	private Collection<? extends GrantedAuthority> getAuthorities(Role role) {
		List<SimpleGrantedAuthority> authorities = List.of(role).stream().map(Role::getRoleName)
				.map((data) -> "ROLE_" + data).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
		return authorities;
	}

}
