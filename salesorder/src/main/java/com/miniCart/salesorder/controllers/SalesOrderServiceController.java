package com.miniCart.salesorder.controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.miniCart.salesorder.configs.SalesOrderServiceConfig;
import com.miniCart.salesorder.entities.CustomerSoS;
import com.miniCart.salesorder.entities.OrderLineItem;
import com.miniCart.salesorder.entities.SalesOrder;
import com.miniCart.salesorder.models.ItemDto;
import com.miniCart.salesorder.models.OrderDto;
import com.miniCart.salesorder.models.OrderResponseDto;
import com.miniCart.salesorder.models.Properties;
import com.miniCart.salesorder.models.StatusResponse;
import com.miniCart.salesorder.services.SalesOrderService;

import feign.FeignException;
import feign.RetryableException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@RestController
@RequestMapping("/sales-order-service/api/v1")
public class SalesOrderServiceController {

	@Autowired
	private SalesOrderServiceConfig serviceConfig;

	@Autowired
	private SalesOrderService service;

	@PostMapping("/orders")
	@CircuitBreaker(name = "itemServiceProxyCB", fallbackMethod = "itemServiceOutagePost")
	public ResponseEntity<?> createOrder(@RequestBody OrderDto orderDto) {
		// Step 1: check if customer exists in localDB
		CustomerSoS custSos = this.service.checkIfCustomerExistsInLocalDB(orderDto.getCustomerId());
		if (custSos == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No customer exists with provided customer ID");
		}

		// Step 2: Validate items by invoking items microservice
		Map<OrderLineItem, Double> oliAndTp = null;
		oliAndTp = this.service.buildOrderLineItemListFromItemNameList(orderDto.getItemNamesAndCount());

		// Step 3: create order by inserting SalesOrder object
		double totalPrice = 0;
		List<OrderLineItem> oliList = new ArrayList<>();
		SalesOrder order = new SalesOrder();

		// Calculate total order price and get a list of OrderLineItem
		for (Map.Entry<OrderLineItem, Double> es : oliAndTp.entrySet()) {
			totalPrice += es.getValue();
			OrderLineItem key = es.getKey();
			key.setSalesOrder(order);
			oliList.add(key);
		}

		// Set the properties of SalesOrder service to persists
		order.setOrderDate(LocalDate.now());
		order.setOrderDesc(orderDto.getOrderDescription());
		order.setTotalPrice(totalPrice);
		order.setCustomerSoS(custSos);
		order.setItems(oliList);

		// Persist the order in DB
		OrderResponseDto orderResponse = new OrderResponseDto(this.service.placeOrder(order));
		return ResponseEntity.ok(orderResponse);
	}

	@GetMapping("/orders")
	public ResponseEntity<?> lookUpOrdersForCustomer(@RequestParam("customerId") long custId) {
		return ResponseEntity.ok(this.service.getAllOrders(custId));
	}

	@GetMapping("/orders/{orderId}")
	public ResponseEntity<?> getOrderDetails(@PathVariable long orderId) {
		return ResponseEntity.ok(this.service.getOrderSnapshot(orderId));
	}

	@GetMapping("/customers-sos")
	public ResponseEntity<?> getAllCustomerSoS() {
		return ResponseEntity.ok(this.service.fetchAllCustomer());
	}

	@GetMapping("/properties")
	public ResponseEntity<?> getPropertyDetails() {
		Properties properties = new Properties(this.serviceConfig.getMsg(), this.serviceConfig.getBuildVersion(),
				this.serviceConfig.getMailDetails(), this.serviceConfig.getActiveDBs());
		return ResponseEntity.ok(properties);
	}

	@GetMapping("/items")
	@CircuitBreaker(name = "itemServiceProxyCB", fallbackMethod = "itemServiceOutage")
	public ResponseEntity<?> getAllProducts() {
		List<ItemDto> allItemsFromProxy = this.service.getAllItemsFromProxy();
		return ResponseEntity.ok(allItemsFromProxy);
	}

	@GetMapping("/item/status")
	@CircuitBreaker(name = "itemServiceProxyCB", fallbackMethod = "itemServiceOutage")
	public ResponseEntity<?> getItemServiceStatus() {
		StatusResponse status = this.service.fetchItemServiceStatus();
		return ResponseEntity.ok(status);
	}

	@SuppressWarnings("unused")
	private ResponseEntity<?> itemServiceOutage(Throwable t) {
		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Item service is temporarily down");

	}

	@SuppressWarnings("unused")
	private ResponseEntity<?> itemServiceOutagePost(OrderDto dto, RuntimeException ex) {
		if (ex instanceof RetryableException) {
			return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Item service is temporarily down");
		}
		if (ex instanceof FeignException) {
			FeignException exp = (FeignException) ex;
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exp.contentUTF8());
		}
		throw new RuntimeException();
	}
}
