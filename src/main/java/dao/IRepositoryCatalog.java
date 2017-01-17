package dao;

import java.sql.SQLException;

public interface IRepositoryCatalog {

	public IPersonRepository people();
	public IAddressRepository addresses();
	public IUserRepository users();
	public IRoleRepository roles();
	public IPermissionRepository permissions();
	public IUserRoleRepository usersRoles();
	public IRolePermissionRepository rolesPermissions();
	
	public void save() throws SQLException;
	public void close() throws SQLException;
	public void saveAndClose() throws SQLException;
	
}
