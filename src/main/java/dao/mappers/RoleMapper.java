package dao.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;

import dao.IRepositoryCatalog;
import domain.Role;
import domain.builders.RoleBuilder;

public class RoleMapper extends AbstractMapper implements IMapResultSetToEntity<Role> {

	public RoleMapper(IRepositoryCatalog rpc) {
		super(rpc);
	}

	@Override
	public Role map(ResultSet rs) throws SQLException {
		Role role = new RoleBuilder()
				.withId(rs.getInt("id"))
				.withName(rs.getString("name"))
				.withUsers(new HashSet<>())
				.withPermissions(new HashSet<>())
				.build();
		
		return role;
	}

}
