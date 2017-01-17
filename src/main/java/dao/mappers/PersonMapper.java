package dao.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;

import dao.IRepositoryCatalog;
import domain.Person;
import domain.builders.PersonBuilder;

public class PersonMapper extends AbstractMapper implements IMapResultSetToEntity<Person>{

	public PersonMapper(IRepositoryCatalog rpc) {
		super(rpc);
	}

	public Person map(ResultSet rs) throws SQLException {
		Person p = new PersonBuilder()
				.withId(rs.getInt("id"))
				.withName(rs.getString("name"))
				.withSurname(rs.getString("surname"))
				.withAge(rs.getInt("age"))
				.withAddresses(new HashSet<>())
				.build();
		return p;
	}

}
