package dao.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import dao.IRepositoryCatalog;
import domain.Permission;
import domain.Role;
import domain.RolePermission;
import domain.builders.RolePermissionBuilder;

public class RolePermissionMapper extends AbstractMapper implements IMapResultSetToEntity<RolePermission> {

	public RolePermissionMapper(IRepositoryCatalog rpc) {
		super(rpc);
	}

	@Override
	public RolePermission map(ResultSet rs) throws SQLException {
		Role role = rpc.roles().get(rs.getInt("role_id"));
		Permission permission = rpc.permissions().get(rs.getInt("permission_id"));
		
		role.getPermissions().add(permission);
		permission.getRoles().add(role);
		
		RolePermission rp = new RolePermissionBuilder()
				.withId(rs.getInt("id"))
				.withPermission(permission)
				.withRole(role)
				.build();
		
		return rp;
	}

}
