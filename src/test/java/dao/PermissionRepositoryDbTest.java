package dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import domain.Permission;
import domain.Role;
import domain.RolePermission;
import domain.User;
import domain.UserRole;
import domain.builders.PermissionBuilder;
import domain.builders.RoleBuilder;
import domain.builders.RolePermissionBuilder;
import domain.builders.UserBuilder;
import domain.builders.UserRoleBuilder;

public class PermissionRepositoryDbTest extends AbstractDbTest {
	
	private IPermissionRepository testee;

	private Permission entity = new PermissionBuilder().withName("a").build(); 
	
	@Before
	public void before() throws SQLException {
		testee = rpc.permissions();
		assertFalse(hasEntries());
	}
	
	@Test
	public void create() throws SQLException {
		storeExampleEntity();
	}
	
	@Test
	public void read() throws SQLException {
		storeExampleEntity();
		
		Permission readEntity = testee.get(0);
		assertValues(readEntity, "a");
	}
	
	@Test
	public void update() throws SQLException {
		storeExampleEntity();
		
		Permission readEntity = testee.get(0);
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
		Permission entity1 = new PermissionBuilder()
				.withName("a")
				.build();
		Permission entity2 = new PermissionBuilder()
				.withName("a")
				.build();
		setValues(entity2, "b");
		
		testee.add(entity1);
		uow.saveChanges();
		testee.add(entity2);
		uow.saveChanges();
		
		List<Permission> list = testee.getAll();
		assertNotNull(list);
		assertEquals(2, list.size());
		
		assertValues(list.get(0), "a");
		assertValues(list.get(1), "b");
	}
	
	@Test
	public void byRole() throws SQLException {
		Role r = new RoleBuilder().withName("a").build();
		entity.getRoles().add(r);
		r.getPermissions().add(entity);
		RolePermission rp = new RolePermissionBuilder().withRole(r).withPermission(entity).build();
		
		rpc.roles().add(r);
		testee.add(entity);
		rpc.rolesPermissions().add(rp);
		uow.saveChanges();
		
		List<Permission> list = testee.byRole(r);
		assertNotNull(list);
		assertEquals(1, list.size());
		assertValues(list.get(0), "a");
	}
	
	@Test
	public void byUser() throws SQLException {
		User u = new UserBuilder().withLogin("a").withPassword("a").build();
		Role r = new RoleBuilder().withName("a").build();
		u.getPermissions().add(entity);
		u.getRoles().add(r);
		r.getUsers().add(u);
		r.getPermissions().add(entity);
		entity.getRoles().add(r);
		RolePermission rp = new RolePermissionBuilder().withRole(r).withPermission(entity).build();
		UserRole ur = new UserRoleBuilder().withUser(u).withRole(r).build();
		
		rpc.users().add(u);
		rpc.roles().add(r);
		testee.add(entity);
		rpc.usersRoles().add(ur);
		rpc.rolesPermissions().add(rp);
		uow.saveChanges();
		
		List<Permission> list = testee.byUser(u);
		assertNotNull(list);
		assertEquals(1, list.size());
		assertValues(list.get(0), "a");
	}
	
	private void assertValues(Permission entity, String stringValue) {
		assertNotNull(entity);
		assertEquals(stringValue, entity.getName());
	}
	
	private void setValues(Permission entity, String stringValue) {
		entity.setName(stringValue);
	}
	
	private void storeExampleEntity() throws SQLException {
		testee.add(entity);
		uow.saveChanges();
		
		assertTrue(hasEntries());
	}
	
	private boolean hasEntries() throws SQLException {
		return super.hasEntries(PermissionRepository.TABLE_NAME);
	}
}
