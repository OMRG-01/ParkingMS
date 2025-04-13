package com.parking.demo.repository;

import com.parking.demo.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    // Additional query methods can be defined here if needed
	Role findByRoleName(String roleName);
}
