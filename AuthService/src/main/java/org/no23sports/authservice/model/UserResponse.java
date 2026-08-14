package org.no23sports.authservice.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class UserResponse {
	private UUID uuid;
	private String token;
	private String nameSurname;
	private String emailAddress;
	private String role;
	private boolean enabled;
	private boolean accountLocked;
	private LocalDateTime createdAt;
	
	public UserResponse(UUID uuid, String token) {
		this.uuid = uuid;
		this.token = token;
	}

	public UserResponse(UUID uuid, String token, String nameSurname, String emailAddress, String role,
			boolean enabled, boolean accountLocked, LocalDateTime createdAt) {
		this.uuid = uuid;
		this.token = token;
		this.nameSurname = nameSurname;
		this.emailAddress = emailAddress;
		this.role = role;
		this.enabled = enabled;
		this.accountLocked = accountLocked;
		this.createdAt = createdAt;
	}

	public UserResponse() {}
	
	public String getToken() {
		return token;
	}
	
	public UUID getUuid() {
		return uuid;
	}

	public String getNameSurname() {
		return nameSurname;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public String getRole() {
		return role;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public boolean isAccountLocked() {
		return accountLocked;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
}
