package dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.sql.SQLException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import domain.Role;
import domain.User;
import domain.UserRole;
import domain.builders.RoleBuilder;
import domain.builders.UserBuilder;
import domain.builders.UserRoleBuilder;

public class UserRoleRepositoryDbTest extends AbstractDbTest {
	
	private IUserRoleRepository testee;
	private User user;
	private Role role;
	private UserRole entity;
	
	@Before
	public void before() throws SQLException {
		testee = rpc.usersRoles();
		user = new UserBuilder().withLogin("a").withPassword("a").build();
		role = new RoleBuilder().withName("a").build();
		user.getRoles().add(role);
		role.getUsers().add(user);
		entity = new UserRoleBuilder().withUser(user).withRole(role).build();
		
		assertFalse(hasEntries());
	}
	
	@Test
	public void byRole() throws SQLException {
		storeExampleEntity();
		
		List<User> list = testee.byRole(role);
		assertNotNull(list);
		assertEquals(1, list.size());
		assertEquals(user, list.get(0));
	}
	
	@Test
	public void byUser() throws SQLException {
		storeExampleEntity();
		
		List<Role> list = testee.byUser(user);
		assertNotNull(list);
		assertEquals(1, list.size());
		assertEquals(role, list.get(0));
	}
	
	private void storeExampleEntity() throws SQLException {
		rpc.users().add(user);
		rpc.roles().add(role);
		testee.add(entity);
		uow.saveChanges();
		
		assertTrue(hasEntries());
	}
	
	private boolean hasEntries() throws SQLException {
		return super.hasEntries(UserRoleRepository.TABLE_NAME);
	}

}
