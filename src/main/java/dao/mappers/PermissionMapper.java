package dao.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;

import dao.IRepositoryCatalog;
import domain.Permission;
import domain.builders.PermissionBuilder;

public class PermissionMapper extends AbstractMapper implements IMapResultSetToEntity<Permission> {

	public PermissionMapper(IRepositoryCatalog rpc) {
		super(rpc);
	}

	@Override
	public Permission map(ResultSet rs) throws SQLException {
		Permission permission = new PermissionBuilder()
				.withId(rs.getInt("id"))
				.withName(rs.getString("name"))
				.withRoles(new HashSet<>())
				.build();
		
		return permission;
	}

}
