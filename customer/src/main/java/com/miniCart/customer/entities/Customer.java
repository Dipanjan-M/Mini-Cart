package com.miniCart.customer.entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

@Entity
@Table(name = "customers")
public class Customer {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	private long id;

	@Column(nullable = false)
	@NotBlank(message = "First name must not be blank")
	@Size(min = 3, max = 20, message = "First name should be in between 3 to 20 characters")
	@Pattern(regexp = "^[A-Z]{1}[a-z]+", message = "First name must contain alphabetical characters only and should start with an uppercase character")
	private String firstName;

	@Column(nullable = false)
	@NotBlank(message = "Last name must not be blank")
	@Size(min = 3, max = 20, message = "Last name should be in between 3 to 20 characters")
	@Pattern(regexp = "^[A-Z]{1}[a-z]+", message = "Last name must contain alphabetical characters only and should start with an uppercase character")
	private String lastName;

	@Column(nullable = false, unique = true)
	@NotBlank(message = "Email must not be blank")
	@Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message = "Please enter a valid email address")
	private String email;

	@JsonProperty(access = Access.WRITE_ONLY)
	@NotBlank(message = "Password must not be blank")
	private String password;

	@OneToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "role_id", referencedColumnName = "roleId")
	private Role role;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

}
