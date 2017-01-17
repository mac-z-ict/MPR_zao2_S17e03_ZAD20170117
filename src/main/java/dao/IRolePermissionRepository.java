package dao;

import java.util.List;

import domain.Permission;
import domain.Role;
import domain.RolePermission;

public interface IRolePermissionRepository extends IRepository<RolePermission> {
	public List<Role> byPermission(Permission permission);
	public List<Permission> byRole(Role role);
}
