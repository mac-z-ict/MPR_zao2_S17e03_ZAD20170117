package dao.mappers;

import static org.hamcrest.CoreMatchers.any;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
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
import domain.Person;

@RunWith(MockitoJUnitRunner.class)
public class PersonMapperTest {
	
	private IMapResultSetToEntity<Person> testee;
	
	@Mock
	private ResultSet rs;
	
	@Mock
	private IRepositoryCatalog rpc;
	
	@Before
	public void before() {
		testee = new PersonMapper(rpc); 
	}
	
	@Test
	public void test() throws SQLException {
		given(rs.getInt("id")).willReturn(0);
		given(rs.getString("name")).willReturn("Name");
		given(rs.getString("surname")).willReturn("Surname");
		given(rs.getInt("age")).willReturn(0);
		
		Person p = testee.map(rs);
		assertThat(p, notNullValue());
		assertThat(p.getId(), is(equalTo(0)));
		assertThat(p.getName(), is(equalTo("Name")));
		assertThat(p.getSurname(), is(equalTo("Surname")));
		assertThat(p.getAge(), is(equalTo(0)));
		assertThat(p.getUser(), nullValue());
		assertThat(p.getAddresses(), notNullValue());
		assertThat(p.getAddresses(), any(Set.class));
		assertThat(p.getAddresses().isEmpty(), is(true));
	}
}
