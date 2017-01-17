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

import dao.IRepositoryCatalog;
import domain.User;

@RunWith(MockitoJUnitRunner.class)
public class UserMapperTest {
	
	private IMapResultSetToEntity<User> testee;
	
	@Mock
	private ResultSet rs;
	
	@Mock
	private IRepositoryCatalog rpc;
	
	@Before
	public void before() {
		testee = new UserMapper(rpc); 
	}
	
	@Test
	public void test() throws SQLException {
		given(rs.getInt("id")).willReturn(0);
		given(rs.getString("login")).willReturn("Login");
		given(rs.getString("password")).willReturn("Password");
		
		User u = testee.map(rs);
		assertThat(u, notNullValue());
		assertThat(u.getId(), is(equalTo(0)));
		assertThat(u.getLogin(), is(equalTo("Login")));
		assertThat(u.getPassword(), is(equalTo("Password")));
		assertThat(u.getRoles(), notNullValue());
		assertThat(u.getRoles(), any(Set.class));
		assertThat(u.getRoles().isEmpty(), is(true));
		assertThat(u.getPermissions(), notNullValue());
		assertThat(u.getPermissions(), any(Set.class));
		assertThat(u.getPermissions().isEmpty(), is(true));
		
	}
}
