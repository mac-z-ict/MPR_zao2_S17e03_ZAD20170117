package domain.builders;

import domain.Role;
import domain.User;
import domain.UserRole;

public class UserRoleBuilder {

	private UserRole userRole;
	
	public UserRoleBuilder() {
		userRole = new UserRole();
	}
	
	public UserRoleBuilder withId(int id) {
		userRole.setId(id);
		return this;
	}
	
	public UserRoleBuilder withUser(User user) {
		userRole.setUser(user);
		return this;
	}
	
	public UserRoleBuilder withRole(Role role) {
		userRole.setRole(role);
		return this;
	}
	
	public UserRole build() {
		return userRole;
	}
}
