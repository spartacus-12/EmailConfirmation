package org.sesame.service;

import org.sesame.dao.RoleRepository;
import org.sesame.dao.UserRepository;
import org.sesame.entities.AppUser;
import org.sesame.entities.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@Transactional
public class AccountServiceImpl implements AccountService {
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder ;
	@Autowired
	private UserRepository userRepository ;
	@Autowired
	private RoleRepository roleRepository ;
	@Override
	public AppUser saveUser(AppUser user) {
	//	String hashPW=bCryptPasswordEncoder.encode(user.getPassword());
		//user.setPassword(hashPW);
		return userRepository.save(user);
	}

	@Override
	public Role saveRole(Role role) {
		return roleRepository.save(role);
	}

	@Override
	public void addRoleToUser(String username, String rolename) {
		Role role =roleRepository.findByRolename(rolename);
		AppUser user = userRepository.findByUsername(username);
		user.getRoles().add(role);
		
	}

	@Override
	public AppUser findUserByUsername(String username) {
		return userRepository.findByUsername(username);

	}

	@Override
	public AppUser findByEmail(String email) {
		
		return userRepository.findByEmail(email);
	}

	@Override
	public AppUser findByConfirmationToken(String token) {
		return userRepository.findByConfirmationToken(token);

	}

}
