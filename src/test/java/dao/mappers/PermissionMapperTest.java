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
import domain.Permission;

@RunWith(MockitoJUnitRunner.class)
public class PermissionMapperTest {
	
	private IMapResultSetToEntity<Permission> testee;
	
	@Mock
	private ResultSet rs;
	
	@Mock
	private IRepositoryCatalog rpc;
	
	@Before
	public void before() {
		testee = new PermissionMapper(rpc); 
	}
	
	@Test
	public void test() throws SQLException {
		given(rs.getInt("id")).willReturn(0);
		given(rs.getString("name")).willReturn("Name");
		
		Permission p = testee.map(rs);
		assertThat(p, notNullValue());
		assertThat(p.getId(), is(equalTo(0)));
		assertThat(p.getName(), is(equalTo("Name")));
		assertThat(p.getRoles(), notNullValue());
		assertThat(p.getRoles(), any(Set.class));
		assertThat(p.getRoles().isEmpty(), is(true));
	}
}
