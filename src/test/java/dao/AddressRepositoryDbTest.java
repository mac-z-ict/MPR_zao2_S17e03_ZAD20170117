package dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import domain.Address;
import domain.Person;
import domain.builders.AddressBuilder;
import domain.builders.PersonBuilder;

public class AddressRepositoryDbTest extends AbstractDbTest {
	
	//@formatter:off
	private String textValue = "a";
	private int numericValue = 0;
	private Address entity = new AddressBuilder()
										.withStreetName(textValue)
										.withStreetNumber(numericValue)
										.withHouseNumber(textValue)
										.withCity(textValue)
										.withPostcode(textValue)
										.build();
	//@formatter:on
	
	private IAddressRepository testee;
	
	@Before
	public void before() throws SQLException {
		testee = rpc.addresses();
		assertFalse(hasEntries());
	}
	
	@Test
	public void create() throws SQLException {
		storeSimpleEntity();
	}
	
	@Test
	public void read() throws SQLException {
		storeSimpleEntity();
		
		Address entityRead = testee.get(numericValue);
		assertValues(entityRead, "a", 0);
	}
	
	@Test
	public void update() throws SQLException {
		storeSimpleEntity();
		
		Address entityRead = testee.get(numericValue);
		assertValues(entityRead, "a", 0);
		
		setValues(entity, "b", 0);
		testee.update(entity);
		uow.saveChanges();
		
		entityRead = testee.get(0);
		assertValues(entityRead, "b", 0);
	}
	
	@Test
	public void delete() throws SQLException {
		storeSimpleEntity();
		testee.delete(entity);
		uow.saveChanges();
		
		assertFalse(hasEntries());
	}
	
	@Test
	public void list() throws SQLException {
		Address entity1 = new AddressBuilder()
				.withStreetName(textValue)
				.withStreetNumber(numericValue)
				.withHouseNumber(textValue)
				.withCity(textValue)
				.withPostcode(textValue)
				.build();
		Address entity2 = new AddressBuilder()
				.withStreetName(textValue)
				.withStreetNumber(numericValue)
				.withHouseNumber(textValue)
				.withCity(textValue)
				.withPostcode(textValue)
				.build();
		setValues(entity1, "a", 0);
		setValues(entity2, "b", 0);
		
		testee.add(entity1);
		uow.saveChanges();
		testee.add(entity2);
		uow.saveChanges();
		
		List<Address> list = testee.getAll();
		assertEquals(2, list.size());
		assertValues(list.get(0), "a", 0);
		assertValues(list.get(1), "b", 0);
	}
	
	@Test
	public void byPerson() throws SQLException {
		Person p = new PersonBuilder()
				.withName(textValue)
				.withSurname(textValue)
				.withAge(numericValue)
				.build();
		entity.setPerson(p);
		rpc.people().add(p);
		uow.saveChanges();
		testee.add(entity);
		uow.saveChanges();
		
		List<Address> list = testee.byPerson(p);
		assertNotNull(list);
		assertEquals(1, list.size());
		assertValues(list.get(0), "a", 0);
	}

	private void assertValues(Address entity, String textValue, int numericValue) {
		assertNotNull(entity);
		assertEquals(textValue, entity.getStreetName());
		assertEquals(numericValue, entity.getStreetNumber());
		assertEquals(textValue, entity.getHouseNumber());
		assertEquals(textValue, entity.getCity());
		assertEquals(textValue, entity.getPostcode());
	}
	
	private void setValues(Address entity, String textValue, int numericValue) {
		entity.setCity(textValue);
		entity.setHouseNumber(textValue);
		entity.setPostcode(textValue);
		entity.setStreetName(textValue);
		entity.setStreetNumber(numericValue);
	}
	
	private void storeSimpleEntity() throws SQLException {
		testee.add(entity);
		uow.saveChanges();
		
		assertTrue(hasEntries());
	}
	
	private boolean hasEntries() throws SQLException {
		return super.hasEntries(AddressRepository.TABLE_NAME);
	}

}
