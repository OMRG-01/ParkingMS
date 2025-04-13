package com.parking.demo.service;

import com.parking.demo.model.Role;
import com.parking.demo.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public Role findById(Long id) {
        return roleRepository.findById(id).orElse(null);  // Fetch the role by ID
    }
    
    public Role getOrCreateVendorRole() {
        Role vendorRole = roleRepository.findByRoleName("Vendor");
        if (vendorRole == null) {
            vendorRole = new Role();
            vendorRole.setRoleName("Vendor");
            roleRepository.save(vendorRole);
        }
        return vendorRole;
    }
}
