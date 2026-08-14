package org.no23sports.authservice.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@PasswordMatches
public class UserRegistration {
	@NotBlank(message = "Enter a valid name and surname")
	private String nameSurname;
	@Email(message = "Invalid email format")
	private String emailAddress;
	@NotBlank(message = "Password cannot be blank")
	@Size(min = 8, message = "Password must be at least 8 characters long")
	private String password;
	@NotBlank
	private String passwordConfirm;
	
	public UserRegistration(String nameSurname, String emailAddress, String password, String passwordConfirm) {
		this.nameSurname = nameSurname;
		this.emailAddress = emailAddress;
		this.password = password;
		this.passwordConfirm = passwordConfirm;
	}
	
	public UserRegistration(){}
	
	public String getNameSurname() {
		return nameSurname;
	}
	
	public String getEmailAddress() {
		return emailAddress;
	}
	
	public String getPassword() {
		return password;
	}
	
	public String getPasswordConfirm() {
		return passwordConfirm;
	}
}
