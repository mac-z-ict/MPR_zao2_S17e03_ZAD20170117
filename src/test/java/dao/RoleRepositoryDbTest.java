package dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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

public class RoleRepositoryDbTest extends AbstractDbTest {
	
	private IRoleRepository testee;
	
	private Role entity = new RoleBuilder().withName("a").build();
	
	@Before
	public void before() throws SQLException {
		testee = rpc.roles();
		assertFalse(hasEntries());
	}
	
	@Test
	public void create() throws SQLException {
		storeExampleEntity();
	}
	
	@Test
	public void read() throws SQLException {
		storeExampleEntity();
		
		Role readEntity = testee.get(0);
		assertValues(readEntity, "a");
	}
	
	@Test
	public void update() throws SQLException {
		storeExampleEntity();
		
		Role readEntity = testee.get(0);
		assertValues(readEntity, "a");
		
		setValues(entity, "b");
		testee.update(entity);
		uow.saveChanges();
		
		readEntity = testee.get(0);
		assertValues(readEntity, "b");
	}
	
	@Test
	public void delete() throws SQLException {
		storeExampleEntity();
		testee.delete(entity);
		uow.saveChanges();
		
		assertFalse(hasEntries());
	}
	
	@Test
	public void list() throws SQLException {
		Role entity1 = new RoleBuilder()
				.withName("a")
				.build();
		Role entity2 = new RoleBuilder()
				.withName("a")
				.build();
		setValues(entity2, "b");
		
		testee.add(entity1);
		uow.saveChanges();
		testee.add(entity2);
		uow.saveChanges();
		
		List<Role> list = testee.getAll();
		assertNotNull(list);
		assertEquals(2, list.size());
		
		assertValues(list.get(0), "a");
		assertValues(list.get(1), "b");
	}
	
	@Test
	public void byUser() throws SQLException {
		User u = new UserBuilder().withLogin("a").withPassword("a").build();
		entity.getUsers().add(u);
		u.getRoles().add(entity);
		rpc.users().add(u);
		testee.add(entity);
		UserRole ur = new UserRoleBuilder().withUser(u).withRole(entity).build();
		rpc.usersRoles().add(ur);
		uow.saveChanges();
		
		List<Role> list = testee.byUser(u);
		assertNotNull(list);
		assertEquals(1, list.size());
		assertValues(list.get(0), "a");
	}
	
	private void assertValues(Role entity, String stringValue) {
		assertNotNull(entity);
		assertEquals(stringValue, entity.getName());
	}
	
	private void setValues(Role entity, String stringValue) {
		entity.setName(stringValue);
	}
	
	private void storeExampleEntity() throws SQLException {
		testee.add(entity);
		uow.saveChanges();
		
		assertTrue(hasEntries());
	}
	
	private boolean hasEntries() throws SQLException {
		return super.hasEntries(RoleRepository.TABLE_NAME);
	}
	
	

}
