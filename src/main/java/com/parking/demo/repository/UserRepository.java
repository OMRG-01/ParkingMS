package com.parking.demo.repository;

import com.parking.demo.model.Role;
import com.parking.demo.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmailAndPassword(String email, String password);
    List<User> findByRoleId(int roleId);
    
    User findByName(String name);
    
    Long countByRoleId(Long roleId);
    
    Optional<User> findByEmail(String email);
    
    long count();
    
    List<User> findByRole_Id(int roleId); // ✅ correct!


}
