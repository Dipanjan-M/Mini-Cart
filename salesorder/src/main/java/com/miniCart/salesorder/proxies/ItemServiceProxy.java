package com.miniCart.salesorder.proxies;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.miniCart.salesorder.models.ItemDto;
import com.miniCart.salesorder.models.StatusResponse;

@FeignClient(value = "item")
public interface ItemServiceProxy {

	static final String COMMON_URL_PART = "/item-service/api/v1";

	@RequestMapping(method = RequestMethod.GET, value = COMMON_URL_PART
			+ "/items/{itemName}", consumes = "application/json")
	public ResponseEntity<ItemDto> getItem(@PathVariable("itemName") String itemName);

	@RequestMapping(method = RequestMethod.GET, value = COMMON_URL_PART + "/items", consumes = "application/json")
	public ResponseEntity<List<ItemDto>> getAllItems();

	@RequestMapping(method = RequestMethod.GET, value = COMMON_URL_PART + "/status")
	public ResponseEntity<StatusResponse> getStatusOfItemService();

}
