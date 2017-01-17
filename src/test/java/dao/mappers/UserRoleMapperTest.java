package dao.mappers;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import dao.IRepositoryCatalog;
import dao.IRoleRepository;
import dao.IUserRepository;
import domain.Role;
import domain.User;
import domain.UserRole;

@RunWith(MockitoJUnitRunner.class)
public class UserRoleMapperTest {
	
	private IMapResultSetToEntity<UserRole> testee;
	
	@Mock
	private ResultSet rs;
	
	@Mock
	private IRepositoryCatalog rpc;
	
	@Mock
	private IRoleRepository roleRepo;
	
	@Mock
	private IUserRepository userRepo;
	
	@Before
	public void before() {
		testee = new UserRoleMapper(rpc);
	}
	
	@Test
	public void test() throws SQLException {
		mockRepositoryCatalog();
		given(rs.getInt("id")).willReturn(0);
		given(rs.getInt("role_id")).willReturn(0);
		given(rs.getInt("user_id")).willReturn(0);
		
		UserRole r = testee.map(rs);
		assertThat(r, notNullValue());
		assertThat(r.getId(), is(equalTo(0)));
		assertThat(r.getRole(), notNullValue());
		assertThat(r.getRole().getUsers(), notNullValue());
		assertThat(r.getRole().getUsers().size(), is(1));
		assertThat(r.getUser(), notNullValue());
		assertThat(r.getUser().getRoles(), notNullValue());
		assertThat(r.getUser().getRoles().size(), is(1));
	}
	
	private void mockRepositoryCatalog() {
		Role r = new Role();
		User u = new User();
		r.getUsers().add(u);
		u.getRoles().add(r);
		given(roleRepo.get(0)).willReturn(r);
		given(userRepo.get(0)).willReturn(u);
		
		given(rpc.roles()).willReturn(roleRepo);
		given(rpc.users()).willReturn(userRepo);
	}
}
