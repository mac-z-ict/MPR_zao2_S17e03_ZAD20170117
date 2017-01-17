package dao.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import dao.IRepositoryCatalog;
import domain.Address;
import domain.builders.AddressBuilder;

public class AddressMapper extends AbstractMapper implements IMapResultSetToEntity<Address> {

	public AddressMapper(IRepositoryCatalog rpc) {
		super(rpc);
	}

	public Address map(ResultSet rs) throws SQLException {
		Address a = new AddressBuilder()
				.withId(rs.getInt("id"))
				.withStreetName(rs.getString("streetName"))
				.withStreetNumber(rs.getInt("streetNumber"))
				.withHouseNumber(rs.getString("houseNumber"))
				.withCity(rs.getString("city"))
				.withPostcode(rs.getString("postcode"))
				.build();
		
		return a;
	}

}
