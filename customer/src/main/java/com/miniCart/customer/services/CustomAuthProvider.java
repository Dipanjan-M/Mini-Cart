package com.miniCart.customer.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomAuthProvider implements AuthenticationProvider {

	@Autowired
	private CustomUserDetailsService userDetailsSvc;

	@Bean
	public PasswordEncoder getPasswordEncoderBean() {
		return new BCryptPasswordEncoder();
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		String email = authentication.getName();
		String password = authentication.getCredentials().toString();

		UserDetails loadedUser = this.userDetailsSvc.loadUserByUsername(email);

		if (this.getPasswordEncoderBean().matches(password, loadedUser.getPassword())) {
			return new UsernamePasswordAuthenticationToken(loadedUser.getUsername(), loadedUser.getPassword(),
					loadedUser.getAuthorities());
		} else {
			throw new BadCredentialsException("Wrong password !!");
		}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
	}

}
