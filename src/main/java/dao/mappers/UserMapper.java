package dao.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;

import dao.IRepositoryCatalog;
import domain.User;
import domain.builders.UserBuilder;

public class UserMapper extends AbstractMapper implements IMapResultSetToEntity<User> {

	public UserMapper(IRepositoryCatalog rpc) {
		super(rpc);
	}

	@Override
	public User map(ResultSet rs) throws SQLException {
		User user = new UserBuilder()
				.withId(rs.getInt("id"))
				.withLogin(rs.getString("login"))
				.withPassword(rs.getString("password"))
				.withRoles(new HashSet<>())
				.withPermissions(new HashSet<>())
				.build();
		
		return user;
	}

}
