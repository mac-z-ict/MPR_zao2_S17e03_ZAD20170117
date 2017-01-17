package dao;

import java.util.List;

import domain.Permission;
import domain.Role;
import domain.User;

public interface IPermissionRepository extends IRepository<Permission> {
	public List<Permission> byRole(Role role);
	public List<Permission> byUser(User user);

}
