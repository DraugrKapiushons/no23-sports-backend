package org.no23sports.authservice.service;


import org.no23sports.authservice.model.User;
import org.no23sports.authservice.model.UserPrincipal;
import org.no23sports.authservice.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class NO23UserDetailsService implements UserDetailsService {
	
	@Autowired
	UserRepo repo;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = repo.findByEmailAddress(email).orElse(null);
		if (user==null) {
			throw new UsernameNotFoundException("User not found");
		}
		return new UserPrincipal(user);
	}

}
