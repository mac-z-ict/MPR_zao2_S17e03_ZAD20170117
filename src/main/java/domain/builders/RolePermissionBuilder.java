package domain.builders;

import domain.Permission;
import domain.Role;
import domain.RolePermission;

public class RolePermissionBuilder {
	
	private RolePermission rolePermission;
	
	public RolePermissionBuilder() {
		rolePermission = new RolePermission();
	}
	
	public RolePermissionBuilder withId(int id) {
		rolePermission.setId(id);
		return this;
	}
	
	public RolePermissionBuilder withRole(Role role) {
		rolePermission.setRole(role);
		return this;
	}
	
	public RolePermissionBuilder withPermission(Permission permission) {
		rolePermission.setPermission(permission);
		return this;
	}
	
	public RolePermission build() {
		return rolePermission;
	}

}
