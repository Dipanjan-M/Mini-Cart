package com.miniCart.salesorder.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.miniCart.salesorder.entities.CustomerSoS;
import com.miniCart.salesorder.entities.SalesOrder;

@Repository
public interface SalesOrderRepository extends JpaRepository<SalesOrder, Long> {

	public List<SalesOrder> findAllByCustomerSoS(CustomerSoS custSos);

}
