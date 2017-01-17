package domain.builders;

import java.util.Set;

import domain.Permission;
import domain.Person;
import domain.Role;
import domain.User;

public class UserBuilder {
	
	private User user;
	
	public UserBuilder() {
		user = new User();
	}
	
	public UserBuilder withId(int id) {
		user.setId(id);
		return this;
	}
	
	public UserBuilder withLogin(String login) {
		user.setLogin(login);
		return this;
	}
	
	public UserBuilder withPassword(String password) {
		user.setPassword(password);
		return this;
	}
	
	public UserBuilder withPerson(Person person) {
		user.setPerson(person);
		return this;
	}
	
	public UserBuilder withRoles(Set<Role> roles) {
		user.setRoles(roles);
		return this;
	}
	
	public UserBuilder withPermissions(Set<Permission> permissions) {
		user.setPermissions(permissions);
		return this;
	}
	
	public User build() {
		return user;
	}

}
