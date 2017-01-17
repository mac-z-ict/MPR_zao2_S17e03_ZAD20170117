package dao.mappers;

import static org.hamcrest.CoreMatchers.any;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.mock;

import dao.AbstractDbTest;
import dao.IPermissionRepository;
import dao.IRepositoryCatalog;
import dao.IRoleRepository;
import domain.Permission;
import domain.Role;
import domain.RolePermission;

@RunWith(MockitoJUnitRunner.class)
public class RolePermissionMapperTest {
	
	private IMapResultSetToEntity<RolePermission> testee;
	
	@Mock
	private ResultSet rs;
	
	@Mock
	private IRepositoryCatalog rpc;
	
	@Mock
	private IRoleRepository roleRepo;
	
	@Mock
	private IPermissionRepository permRepo;
	
	@Before
	public void before() {
		testee = new RolePermissionMapper(rpc);
	}
	
	@Test
	public void test() throws SQLException {
		mockRepositoryCatalog();
		given(rs.getInt("id")).willReturn(0);
		given(rs.getInt("role_id")).willReturn(0);
		given(rs.getInt("permission_id")).willReturn(0);
		
		RolePermission r = testee.map(rs);
		assertThat(r, notNullValue());
		assertThat(r.getId(), is(equalTo(0)));
		assertThat(r.getRole(), notNullValue());
		assertThat(r.getRole().getPermissions(), notNullValue());
		assertThat(r.getRole().getPermissions().size(), is(1));
		assertThat(r.getPermission(), notNullValue());
		assertThat(r.getPermission().getRoles(), notNullValue());
		assertThat(r.getPermission().getRoles().size(), is(1));
	}
	
	private void mockRepositoryCatalog() {
		Role r = new Role();
		Permission p = new Permission();
		r.getPermissions().add(p);
		p.getRoles().add(r);
		given(roleRepo.get(0)).willReturn(r);
		given(permRepo.get(0)).willReturn(p);
		
		given(rpc.roles()).willReturn(roleRepo);
		given(rpc.permissions()).willReturn(permRepo);
	}
}
