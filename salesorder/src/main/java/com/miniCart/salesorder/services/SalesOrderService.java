package com.miniCart.salesorder.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.miniCart.salesorder.entities.CustomerSoS;
import com.miniCart.salesorder.entities.OrderLineItem;
import com.miniCart.salesorder.entities.SalesOrder;
import com.miniCart.salesorder.models.ItemDto;
import com.miniCart.salesorder.models.LookupResponseDto;
import com.miniCart.salesorder.models.OrderResponseDto;
import com.miniCart.salesorder.models.StatusResponse;
import com.miniCart.salesorder.proxies.ItemServiceProxy;
import com.miniCart.salesorder.repos.CustomerSoSRepository;
import com.miniCart.salesorder.repos.SalesOrderRepository;

@Service
public class SalesOrderService {

	@Autowired
	private CustomerSoSRepository custSosRepo;

	@Autowired
	private SalesOrderRepository salesOrderRepo;

	@Autowired
	private ItemServiceProxy itemServiceProxy;

	public void saveCustomerSoSDataFromEvent(CustomerSoS sos) {
		this.custSosRepo.save(sos);
	}

	public CustomerSoS checkIfCustomerExistsInLocalDB(long customerId) {
		Optional<CustomerSoS> cust = this.custSosRepo.findById(customerId);
		if (cust.isPresent()) {
			return cust.get();
		}
		return null;
	}

	public ItemDto getItemFromItemService(String jwt, String itemName) {
		ResponseEntity<ItemDto> item = this.itemServiceProxy.getItem(jwt, itemName);
		return item.getBody();
	}

	public Map<OrderLineItem, Double> buildOrderLineItemListFromItemNameList(String jwt,
			Map<String, Integer> itemNamesAndCount) {
		Map<OrderLineItem, Double> res = new HashMap<>();
		for (Map.Entry<String, Integer> es : itemNamesAndCount.entrySet()) {
			ItemDto item = this.getItemFromItemService(jwt, es.getKey());
			OrderLineItem oli = new OrderLineItem();
			oli.setItemName(item.getName());
			oli.setItemQuantity(Integer.valueOf(es.getValue()));
			oli.setUnitPrice(item.getPrice());
			Double totalPrice = Integer.valueOf(es.getValue()) * item.getPrice();
			res.put(oli, totalPrice);
		}
		return res;
	}

	public SalesOrder placeOrder(SalesOrder order) {
		SalesOrder savedOrder = this.salesOrderRepo.save(order);
		if (savedOrder.getId() == 0) {
			throw new RuntimeException("Unable to create the order. Database operation failed !!!");
		}
		return savedOrder;
	}

	public List<LookupResponseDto> getAllOrders(long custId) {
		Optional<CustomerSoS> cust = this.custSosRepo.findById(custId);
		if (cust.isEmpty()) {
			throw new RuntimeException("No customer with given customer id is present");
		}
		CustomerSoS customer = cust.get();
		List<SalesOrder> salesOrders = this.salesOrderRepo.findAllByCustomerSoS(customer);
		return salesOrders.stream().map(LookupResponseDto::new).collect(Collectors.toList());
	}

	public OrderResponseDto getOrderSnapshot(long orderId) {
		Optional<SalesOrder> order = this.salesOrderRepo.findById(orderId);
		order.orElseThrow(() -> new RuntimeException("No order found with the given order Id"));
		return new OrderResponseDto(order.get());
	}

	public List<ItemDto> getAllItemsFromProxy(String jwt) {
		ResponseEntity<List<ItemDto>> allItems = this.itemServiceProxy.getAllItems(jwt);
		return allItems.getBody();
	}

	public List<CustomerSoS> fetchAllCustomer() {
		return this.custSosRepo.findAll();
	}

	public StatusResponse fetchItemServiceStatus() {
		ResponseEntity<StatusResponse> status = this.itemServiceProxy.getStatusOfItemService();
		return status.getBody();
	}
}
