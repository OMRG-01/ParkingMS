package com.parking.demo.model;

import java.time.LocalDateTime;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String mobileNumber;

    @Column(nullable = false)
    private String password;  // Stored as plain text (not encoded)

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String gender;
    
    @Column(name = "active")
    private boolean active; // ✅ Add this field

    
    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;
    
 //@Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // Getter
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // Setter (optional, but usually not required)
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    
    public User() {
    }

    public User(Long id, String name, String email, String mobileNumber, String password, String city, String gender, Role role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.mobileNumber = mobileNumber;
        this.password = password;
        this.city = city;
        this.gender = gender;
        this.role = role;
    }
    
    public User(String email, String name) {
        this.email = email;
        this.name = name;
        this.mobileNumber = "N/A"; // or a default value
        this.password = "GOOGLE_USER"; // or generate a dummy value
        this.city = "N/A";
        this.gender = "Other";
        this.role = new Role(2L, "USER"); // make sure this matches your DB
    }
    
    // ✅ Getter and Setter for 'active'
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}