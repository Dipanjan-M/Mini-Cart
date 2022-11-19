package com.miniCart.salesorder.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.miniCart.salesorder.entities.CustomerSoS;

@Repository
public interface CustomerSoSRepository extends JpaRepository<CustomerSoS, Long> {

}
