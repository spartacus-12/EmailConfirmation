package org.sesame.service;

import java.util.ArrayList;

import java.util.Collection;
import org.sesame.dao.UserRepository;
import org.sesame.entities.AppUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	
    private UserRepository applicationUserRepository;


	public UserDetailsServiceImpl(UserRepository applicationUserRepository) {
		this.applicationUserRepository = applicationUserRepository;
	}


	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		   AppUser applicationUser = applicationUserRepository.findByUsername(username);
	        if (applicationUser == null) {
	            throw new UsernameNotFoundException(username);
	        }
	    	Collection<GrantedAuthority>authorities = new ArrayList<>();
	    	applicationUser.getRoles().forEach(r->{
	    		authorities.add(new SimpleGrantedAuthority(r.getRolename()));
	    	});
	        return new User(applicationUser.getUsername(), applicationUser.getPassword(),authorities);
	}
	}


