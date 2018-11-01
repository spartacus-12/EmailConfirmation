package org.sesame.dao;

import org.sesame.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long>{
	public Role findByRolename(String rolename);


}
