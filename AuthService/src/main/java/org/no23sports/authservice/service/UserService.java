package org.no23sports.authservice.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.no23sports.authservice.model.Role;
import org.no23sports.authservice.model.User;
import org.no23sports.authservice.model.UserRegistration;
import org.no23sports.authservice.model.UserRequest;
import org.no23sports.authservice.model.UserResponse;
import org.no23sports.authservice.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
	
	private static final int MAX_FAILED_ATTEMPTS = 5;

	@Autowired
	private UserRepo repo;
	
	@Autowired
	private PasswordEncoder encoder;
	
	@Autowired
	private AuthenticationManager authManager;
	
	@Autowired
	private JWTService jwtService;
	
	public UserResponse register(UserRegistration registration) {
		if (repo.existsByEmailAddress(registration.getEmailAddress())) {
			return null;
		}
		User user = new User(registration.getNameSurname(), registration.getEmailAddress(), encoder.encode(registration.getPassword()), Role.MEMBER);
		repo.save(user);
		return user.toResponse(jwtService.generateToken(user.getEmailAddress(), 
				user.getRole().name(), 
				user.getNameSurname()));
	}
	public UserResponse login(UserRequest request) {
		 try {
		        Authentication authentication = authManager.authenticate(
		            new UsernamePasswordAuthenticationToken(
		                request.getEmailAddress(),
		                request.getPassword()));
		 } catch (LockedException | DisabledException e) {
		        return null;
		 } catch (BadCredentialsException e) {
				registerFailedAttempt(request.getEmailAddress());
				return null;
		 }
		 User user = repo.findByEmailAddress(request.getEmailAddress()).orElse(null);
		 resetFailedAttempts(user);
		 return user.toResponse(jwtService.generateToken(user.getEmailAddress(), 
					user.getRole().name(), 
					user.getNameSurname()));
	}
	
	private void registerFailedAttempt(String emailAddress) {
		repo.findByEmailAddress(emailAddress).ifPresent(user -> {
			user.setFailedLoginAttempts(user.getFailedLoginAttempts() + 1);
			if (user.getFailedLoginAttempts() >= MAX_FAILED_ATTEMPTS) {
				user.setAccountLocked(true);
			}
			repo.save(user);
		});
	}

	private void resetFailedAttempts(User user) {
		if (user.getFailedLoginAttempts() != 0) {
			user.setFailedLoginAttempts(0);
			repo.save(user);
		}
	}

	// ---- Admin panel: Üye yönetimi ----

	public List<UserResponse> getAllUsers() {
		return repo.findAll().stream()
				.map(u -> new UserResponse(u.getUuid(), null, u.getNameSurname(), u.getEmailAddress(), u.getRole().name(),
						u.isEnabled(), u.isAccountLocked(), u.getCreatedAt()))
				.collect(Collectors.toList());
	}

	public UserResponse getUser(UUID id) {
		User u = repo.findById(id).orElse(null);
		if (u == null) return null;
		return new UserResponse(u.getUuid(), null, u.getNameSurname(), u.getEmailAddress(), u.getRole().name(),
				u.isEnabled(), u.isAccountLocked(), u.getCreatedAt());
	}

	public UserResponse updateRole(UUID id, Role role) {
		User u = repo.findById(id).orElse(null);
		if (u == null) return null;
		u.setRole(role);
		repo.save(u);
		return new UserResponse(u.getUuid(), null, u.getNameSurname(), u.getEmailAddress(), u.getRole().name(),
				u.isEnabled(), u.isAccountLocked(), u.getCreatedAt());
	}

	public UserResponse setEnabled(UUID id, boolean enabled) {
		User u = repo.findById(id).orElse(null);
		if (u == null) return null;
		u.setEnabled(enabled);
		repo.save(u);
		return new UserResponse(u.getUuid(), null, u.getNameSurname(), u.getEmailAddress(), u.getRole().name(),
				u.isEnabled(), u.isAccountLocked(), u.getCreatedAt());
	}

	public UserResponse unlock(UUID id) {
		User u = repo.findById(id).orElse(null);
		if (u == null) return null;
		u.setAccountLocked(false);
		u.setFailedLoginAttempts(0);
		repo.save(u);
		return new UserResponse(u.getUuid(), null, u.getNameSurname(), u.getEmailAddress(), u.getRole().name(),
				u.isEnabled(), u.isAccountLocked(), u.getCreatedAt());
	}
}
