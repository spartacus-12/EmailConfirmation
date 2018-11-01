package org.sesame.service;

import org.sesame.entities.AppUser;
import org.sesame.entities.Role;

public interface AccountService {
	public AppUser saveUser(AppUser user);
	public Role saveRole(Role role);
	public void addRoleToUser(String username,String rolename);
	public AppUser findUserByUsername(String username);
	public AppUser findByEmail(String email);
	public AppUser findByConfirmationToken(String token);
}
