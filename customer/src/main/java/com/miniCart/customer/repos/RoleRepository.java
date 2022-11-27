package com.miniCart.customer.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.miniCart.customer.entities.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

	public Role findByRoleName(String roleName);

}
