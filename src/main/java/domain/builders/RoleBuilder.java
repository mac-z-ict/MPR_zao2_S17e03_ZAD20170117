package domain.builders;

import java.util.Set;

import domain.Permission;
import domain.Role;
import domain.User;

public class RoleBuilder {

	private Role role;
	
	public RoleBuilder() {
		role = new Role();
	}
	
	public RoleBuilder withId(int id) {
		role.setId(id);
		return this;
	}
	
	public RoleBuilder withName(String name) {
		role.setName(name);
		return this;
	}
	
	public RoleBuilder withPermissions(Set<Permission> permissions) {
		role.setPermissions(permissions);
		return this;
	}
	
	public RoleBuilder withUsers(Set<User> users) {
		role.setUsers(users);
		return this;
	}
	
	public Role build() {
		return role;
	}
}
