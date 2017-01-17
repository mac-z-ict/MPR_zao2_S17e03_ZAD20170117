package dao;

import java.util.List;

import domain.Role;
import domain.User;

public interface IRoleRepository extends IRepository<Role> {
	
	public List<Role> byUser(User user);

}
