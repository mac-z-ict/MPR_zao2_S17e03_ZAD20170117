package domain.builders;

import java.util.Set;

import domain.Permission;
import domain.Role;

public class PermissionBuilder {

	private Permission permission;
	
	public PermissionBuilder() {
		permission = new Permission();
	}
	
	public PermissionBuilder withId(int id) {
		permission.setId(id);
		return this;
	}
	
	public PermissionBuilder withName(String name) {
		permission.setName(name);
		return this;
	}
	
	public PermissionBuilder withRoles(Set<Role> roles) {
		permission.setRoles(roles);
		return this;
	}
	
	public Permission build() {
		return permission;
	}
}
