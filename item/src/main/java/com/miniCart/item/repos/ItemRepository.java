package com.miniCart.item.repos;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.miniCart.item.entities.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

	Optional<Item> findByName(String name);

}
