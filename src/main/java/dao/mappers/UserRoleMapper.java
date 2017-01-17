package dao.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import dao.IRepositoryCatalog;
import domain.Role;
import domain.User;
import domain.UserRole;
import domain.builders.UserRoleBuilder;

public class UserRoleMapper extends AbstractMapper implements IMapResultSetToEntity<UserRole> {
	
	public UserRoleMapper(IRepositoryCatalog rpc) {
		super(rpc);
	}

	@Override
	public UserRole map(ResultSet rs) throws SQLException {
		User user = rpc
				.users()
				.get(rs.getInt("user_id"));
		
		Role role = rpc
				.roles()
				.get(rs.getInt("role_id"));
		
		user.getRoles().add(role);
		role.getUsers().add(user);
		
		UserRole ur = new UserRoleBuilder()
				.withId(rs.getInt("id"))
				.withUser(user)
				.withRole(role)
				.build();
		
		return ur;
	}

}
