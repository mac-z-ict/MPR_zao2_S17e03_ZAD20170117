package dao.mappers;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
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
import domain.Address;


@RunWith(MockitoJUnitRunner.class)
public class AddressMapperTest {
	
	private IMapResultSetToEntity<Address> testee;
	
	@Mock
	private ResultSet rs;
	
	@Mock
	private IRepositoryCatalog rpc;
	
	@Before
	public void before() {
		testee = new AddressMapper(rpc); 
	}
	
	@Test
	public void test() throws SQLException {
		given(rs.getInt("id")).willReturn(0);
		given(rs.getString("streetName")).willReturn("StreetName");
		given(rs.getInt("streetNumber")).willReturn(0);
		given(rs.getString("houseNumber")).willReturn("HouseNumber");
		given(rs.getString("city")).willReturn("City");
		given(rs.getString("postcode")).willReturn("PostCode");
		
		Address a = testee.map(rs);
		assertThat(a, notNullValue());
		assertThat(a.getId(), is(equalTo(0)));
		assertThat(a.getStreetName(), is(equalTo("StreetName")));
		assertThat(a.getStreetNumber(), is(equalTo(0)));
		assertThat(a.getHouseNumber(), is(equalTo("HouseNumber")));
		assertThat(a.getCity(), is(equalTo("City")));
		assertThat(a.getPostcode(), is(equalTo("PostCode")));
		assertThat(a.getPerson(), nullValue());
	}

}
