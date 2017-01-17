package dao;

import java.util.List;

import domain.Role;
import domain.User;
import domain.UserRole;

public interface IUserRoleRepository extends IRepository<UserRole> {

	public List<User> byRole(Role role);
	public List<Role> byUser(User user);
	
}
