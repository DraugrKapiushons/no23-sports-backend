package org.no23sports.authservice.model;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Table(name = "Users", uniqueConstraints = {@UniqueConstraint(columnNames = "email", name = "uk_users_email")})
@Entity
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID uuid;
	@Column(name = "name_surname", nullable = false)
	private String nameSurname;
    @Column(unique = true, nullable = false, name = "email")
    private String emailAddress;
	@Column(name = "password_hash", nullable = false)
	private String passwordHash;
	@Column(name = "role", nullable = false)
	@Enumerated(EnumType.STRING)
	private Role role;
	@CreationTimestamp
	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;
	@UpdateTimestamp
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;
	private boolean enabled;
	private boolean accountExpired;
	private boolean accountLocked;
	private int failedLoginAttempts;
	
	public UUID getUuid() {
		return uuid;
	}
	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}
	public User(String nameSurname, String emailAddress, String passwordHash, Role role) {
		this.nameSurname = nameSurname;
		this.emailAddress = emailAddress;
		this.passwordHash = passwordHash;
		this.role = role;
		this.failedLoginAttempts = 0;
		enabled=true;
		accountExpired=false;
		accountLocked=false;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public boolean isAccountExpired() {
		return accountExpired;
	}
	public void setAccountExpired(boolean accountExpired) {
		this.accountExpired = accountExpired;
	}
	public boolean isAccountLocked() {
		return accountLocked;
	}
	public void setAccountLocked(boolean accountLocked) {
		this.accountLocked = accountLocked;
	}
	
	public User() {
	}

	public String getNameSurname() {
		return nameSurname;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public String getEmailAddress() {
		return emailAddress;
	}
	
	public void setNameSurname(String nameSurname) {
		this.nameSurname = nameSurname;
	}
	
	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}
	
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public UserResponse toResponse() {
		return new UserResponse(uuid, null);
	}
	
	public UserResponse toResponse(String token) {
		return new UserResponse(uuid, token);
	}
	
	public int getFailedLoginAttempts() {
		return failedLoginAttempts;
	}
	
	public void setFailedLoginAttempts(int failedLoginAttempts) {
		this.failedLoginAttempts = failedLoginAttempts;
	}
	
	public Role getRole() {
		return role;
	}
	
	public void setRole(Role role) {
		this.role = role;
	}
	
	public LocalDateTime getCreatedAt() {
	    return createdAt;
	}

	public LocalDateTime getUpdatedAt() {
	    return updatedAt;
	}
}
