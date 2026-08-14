package org.no23sports.authservice.model;

public class UserRequest {
	private String emailAddress;
	private String password;
	
	public UserRequest(String emailAddress, String password) {
		this.emailAddress = emailAddress;
		this.password = password;
	}
	
	public UserRequest() {}
	
	public String getEmailAddress() {
		return emailAddress;
	}

	public String getPassword() {
		return password;
	}
}
