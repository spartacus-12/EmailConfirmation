package org.sesame.entities;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class AppUser implements Serializable{
	@Id
	@GeneratedValue
private Long id ;
private String username ;
private String password ;
private String email ;
private boolean enabled ;
private String confirmationToken ;
@ManyToMany(fetch=FetchType.EAGER)
private Collection<Role> roles= new ArrayList<>() ;
public Long getId() {
	return id;
}
public void setId(Long id) {
	this.id = id;
}
public String getUsername() {
	return username;
}
public void setUsername(String username) {
	this.username = username;
}
public String getPassword() {
	return password;
}
public void setPassword(String password) {
	this.password = password;
}
public AppUser(String username, String password) {
	super();
	this.username = username;
	this.password = password;
}
public AppUser() {
	super();
	// TODO Auto-generated constructor stub
}
public Collection<Role> getRoles() {
	return roles;
}
public void setRoles(Collection<Role> roles) {
	this.roles = roles;
}
public AppUser(String username, String password, Collection<org.sesame.entities.Role> roles) {
	super();
	this.username = username;
	this.password = password;
	this.roles = roles;
}
public String getEmail() {
	return email;
}
public void setEmail(String email) {
	this.email = email;
}
public boolean isEnabled() {
	return enabled;
}
public void setEnabled(boolean enabled) {
	this.enabled = enabled;
}

public String getConfirmationToken() {
	return confirmationToken;
}
public AppUser(String username, String password, String email, boolean enabled, String confirmationToken,
		Collection<Role> roles) {
	super();
	this.username = username;
	this.password = password;
	this.email = email;
	this.enabled = enabled;
	this.confirmationToken = confirmationToken;
	this.roles = roles;
}
public void setConfirmationToken(String confirmationToken) {
	this.confirmationToken = confirmationToken;
}

}
