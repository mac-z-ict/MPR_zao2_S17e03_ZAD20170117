package dao.mappers;

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
import domain.Role;

@RunWith(MockitoJUnitRunner.class)
public class RoleMapperTest {
	
	private IMapResultSetToEntity<Role> testee;
	
	@Mock
	private ResultSet rs;
	
	@Mock
	private IRepositoryCatalog rpc;
	
	@Before
	public void before() {
		testee = new RoleMapper(rpc); 
	}
	
	@Test
	public void test() throws SQLException {
		given(rs.getInt("id")).willReturn(0);
		given(rs.getString("name")).willReturn("Name");
		
		Role r = testee.map(rs);
		assertThat(r, notNullValue());
		assertThat(r.getId(), is(equalTo(0)));
		assertThat(r.getName(), is(equalTo("Name")));
		assertThat(r.getPermissions(), notNullValue());
		assertThat(r.getPermissions(), is(Set.class));
		assertThat(r.getPermissions().isEmpty(), is(true));
		assertThat(r.getUsers(), notNullValue());
		assertThat(r.getUsers(), is(Set.class));
		assertThat(r.getUsers().isEmpty(), is(true));
	}
}
