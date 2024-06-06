package org.pharmac.services;

import org.pharmac.config.Role;
import org.pharmac.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleService {
	@Autowired
	private RoleRepository roleRepository;

	public Role getRoleByName(String roleName) {
		return roleRepository.findByRoleName(roleName);
	}

	public List<Role> getRolesByName(String... roleNames) {
		List<Role> roles = new ArrayList<>();
		for (String roleName : roleNames) {
			Role role = roleRepository.findByRoleName(roleName);
			System.out.println(role);
			roles.add(role);
		}
		return roles;
	}

	public List<Role> getRoles() {
		return roleRepository.findAll();
	}
}
