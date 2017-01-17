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
import domain.builders.PermissionBuilder;
import domain.builders.RoleBuilder;
import domain.builders.RolePermissionBuilder;

public class RolePermissionRepositoryDbTest extends AbstractDbTest {
	
	private IRolePermissionRepository testee;
	private Permission permission;
	private Role role;
	private RolePermission entity;
	
	@Before
	public void before() throws SQLException {
		testee = rpc.rolesPermissions();
		permission = new PermissionBuilder().withName("a").build();
		role = new RoleBuilder().withName("a").build();
		role.getPermissions().add(permission);
		permission.getRoles().add(role);
		entity = new RolePermissionBuilder().withRole(role).withPermission(permission).build();
		
		assertFalse(hasEntries());
	}
	
	@Test
	public void byPermission() throws SQLException {
		storeExampleEntity();
		
		List<Role> list = testee.byPermission(permission);
		assertNotNull(list);
		assertEquals(1, list.size());
		assertEquals(role, list.get(0));
	}
	
	@Test
	public void byRole() throws SQLException {
		storeExampleEntity();
		
		List<Permission> list = testee.byRole(role);
		assertNotNull(list);
		assertEquals(1, list.size());
		assertEquals(permission, list.get(0));
	}
	
	private void storeExampleEntity() throws SQLException {
		rpc.permissions().add(permission);
		rpc.roles().add(role);
		testee.add(entity);
		uow.saveChanges();
		
		assertTrue(hasEntries());
	}
	
	private boolean hasEntries() throws SQLException {
		return super.hasEntries(RolePermissionRepository.TABLE_NAME);
	}

}
