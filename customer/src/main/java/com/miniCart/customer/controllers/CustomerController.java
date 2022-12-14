package com.miniCart.customer.controllers;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.miniCart.customer.configs.CustomerServiceConfig;
import com.miniCart.customer.constants.AMQPConstants;
import com.miniCart.customer.entities.Customer;
import com.miniCart.customer.models.CustomValidationError;
import com.miniCart.customer.models.CustomerCreationEventData;
import com.miniCart.customer.models.Properties;
import com.miniCart.customer.repos.CustomerRepository;
import com.miniCart.customer.repos.RoleRepository;

@RestController
@RequestMapping("/customer-service/api/v1")
public class CustomerController {

	@Autowired
	private CustomerRepository customerRepo;

	@Autowired
	private RoleRepository roleRepo;

	@Autowired
	private CustomerServiceConfig serviceConfig;

	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Autowired
	@Qualifier("myAmqpTemplateBean")
	private AmqpTemplate template;

	@GetMapping("/customers")
	public ResponseEntity<?> getAllCustomers() {
		List<Customer> customers = this.customerRepo.findAll();
		return ResponseEntity.status(HttpStatus.OK).body(customers);
	}

	@PostMapping("/customer")
	public ResponseEntity<?> createCustomer(@Valid @RequestBody Customer customer, Errors errors) {
		if (errors.hasErrors()) {
			CustomValidationError validationErrs = new CustomValidationError(errors.getFieldErrors());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validationErrs.getValidationErrResponses());
		}

		Optional<Customer> existingCustomer = this.customerRepo.findByEmail(customer.getEmail());
		if (existingCustomer.isPresent()) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body("Customer with email << " + existingCustomer.get().getEmail() + " >>  already exists.");
		}

		customer.setPassword(getPasswordEncoder().encode(customer.getPassword()));
		customer.setRole(this.roleRepo.findByRoleName("CUSTOMER"));
		Customer savedCustomer = this.customerRepo.saveAndFlush(customer);
		if (savedCustomer.getId() == 0) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Database operation failed !!!");
		} else {
			this.template.convertAndSend(AMQPConstants.EXCHANGE_NAME, AMQPConstants.ROUTING_KEY,
					new CustomerCreationEventData(savedCustomer.getId(), savedCustomer.getFirstName(),
							savedCustomer.getLastName(), savedCustomer.getEmail()));
		}
		return ResponseEntity.ok(savedCustomer);
	}

	@GetMapping("/properties")
	public ResponseEntity<?> getPropertyDetails() {
		Properties properties = new Properties(this.serviceConfig.getMsg(), this.serviceConfig.getBuildVersion(),
				this.serviceConfig.getMailDetails(), this.serviceConfig.getActiveDBs());
		return ResponseEntity.ok(properties);
	}

}
