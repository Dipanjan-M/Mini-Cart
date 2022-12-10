package com.miniCart.item.controllers;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.miniCart.item.configs.ItemServiceConfig;
import com.miniCart.item.entities.Item;
import com.miniCart.item.models.CustomValidationError;
import com.miniCart.item.models.HelloResponse;
import com.miniCart.item.models.Properties;
import com.miniCart.item.repos.ItemRepository;

@RestController
@RequestMapping("/item-service/api/v1")
public class ItemController {

	@Autowired
	private ItemServiceConfig serviceConfig;

	@Autowired
	private ItemRepository itemRepo;

	@Autowired
	private Environment env;

	@GetMapping("/items")
	public ResponseEntity<?> getItems() {
		List<Item> items = this.itemRepo.findAll();
		return ResponseEntity.ok(items);
	}

	@GetMapping("/items/{itemName}")
	public ResponseEntity<?> getItemDetails(@PathVariable String itemName) {
		Optional<Item> item = this.itemRepo.findByName(itemName);
		if (item.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("No item is available with given item name : << " + itemName + " >>");
		}
		return ResponseEntity.ok(item.get());
	}

	@PostMapping("/item")
	public ResponseEntity<?> createItem(@Valid @RequestBody Item item, Errors errors) {
		if (errors.hasErrors()) {
			CustomValidationError validationErrs = new CustomValidationError(errors.getFieldErrors());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validationErrs.getValidationErrResponses());
		}

		Optional<Item> ifExists = this.itemRepo.findByName(item.getName());

		if (ifExists.isPresent()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Item with name << " + ifExists.get().getName() + " >> already exists");
		}

		Item savedItem = this.itemRepo.save(item);
		if (savedItem.getId() == 0) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Unable to create the item, Database operation failed!!!");
		}
		return ResponseEntity.ok(savedItem);
	}

	@PutMapping("/item")
	public ResponseEntity<?> updateItem(@Valid @RequestBody Item item, Errors errors) {
		if (errors.hasErrors()) {
			CustomValidationError validationErrs = new CustomValidationError(errors.getFieldErrors());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validationErrs.getValidationErrResponses());
		}
		Optional<Item> itemsOptional = this.itemRepo.findByName(item.getName());
		if (itemsOptional.isPresent()) {
			if (itemsOptional.get().getId() != item.getId()) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Can't update the item's name to << "
						+ item.getName() + " >> as item with same name already exists.");
			}
		}
		Item updatedItem = this.itemRepo.save(item);
		return ResponseEntity.ok(updatedItem);
	}

	@DeleteMapping("/item/{itemId}")
	public ResponseEntity<?> removeItem(@PathVariable("itemId") long itemId) {
		Optional<Item> itemToRemove = this.itemRepo.findById(itemId);
		if (itemToRemove.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Item doesn't exist.");
		} else {
			this.itemRepo.delete(itemToRemove.get());
		}
		return ResponseEntity.ok(itemToRemove.get());
	}

	@GetMapping("/properties")
	public ResponseEntity<?> getPropertyDetails() {
		Properties properties = new Properties(this.serviceConfig.getMsg(), this.serviceConfig.getBuildVersion(),
				this.serviceConfig.getMailDetails(), this.serviceConfig.getActiveDBs());
		return ResponseEntity.ok(properties);
	}

	@GetMapping("/status")
	public ResponseEntity<?> sayHello() {
		HelloResponse hello = new HelloResponse();
		hello.setMessage("Hello from Item service !!! Status is UP");
		hello.setEnvironment(this.env.getProperty("local.server.port"));
		return ResponseEntity.ok(hello);
	}

}
