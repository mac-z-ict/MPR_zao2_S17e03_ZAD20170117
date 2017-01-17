package dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import domain.Person;
import domain.User;
import domain.builders.PersonBuilder;
import domain.builders.UserBuilder;

public class PersonRepositoryDbTest extends AbstractDbTest {
	
	private IPersonRepository testee;
	
	private Person entity = new PersonBuilder()
									.withName("a")
									.withSurname("a")
									.withAge(0)
									.build();
	
	@Before
	public void before() throws SQLException {
		testee = rpc.people();
		assertFalse(hasEntries());
	}
	
	@Test
	public void create() throws SQLException {
		storeExampleEntity();
	}
	
	@Test
	public void read() throws SQLException {
		storeExampleEntity();
		
		Person readEntity = testee.get(0);
		assertValues(readEntity, "a", 0);
	}
	
	@Test
	public void update() throws SQLException {
		storeExampleEntity();
		
		Person readEntity = testee.get(0);
		assertValues(readEntity, "a", 0);
		
		setValues(entity, "b", 0);
		testee.update(entity);
		uow.saveChanges();
		
		readEntity = testee.get(0);
		assertValues(readEntity, "b", 0);
	}
	
	@Test
	public void delete() throws SQLException {
		storeExampleEntity();
		testee.delete(entity);
		uow.saveChanges();
		
		assertFalse(hasEntries());
	}
	
	@Test
	public void list() throws SQLException {
		Person entity1 = new PersonBuilder()
				.withName("a")
				.withSurname("a")
				.withAge(0)
				.build();
		Person entity2 = new PersonBuilder()
				.withName("a")
				.withSurname("a")
				.withAge(0)
				.build();
		setValues(entity2, "b", 0);
		
		testee.add(entity1);
		uow.saveChanges();
		testee.add(entity2);
		uow.saveChanges();
		
		List<Person> list = testee.getAll();
		assertNotNull(list);
		assertEquals(2, list.size());
		
		assertValues(list.get(0), "a", 0);
		assertValues(list.get(1), "b", 0);
	}
	
	@Test
	public void withName() throws SQLException {
		storeExampleEntity();
		
		List<Person> list = testee.withName("b");
		assertNotNull(list);
		assertEquals(true, list.isEmpty());
		
		list = testee.withName("a");
		assertNotNull(list);
		assertEquals(1, list.size());
		assertValues(list.get(0), "a", 0);
	}
	
	@Test
	public void byUser() throws SQLException {
		User u = new UserBuilder().withLogin("a").withPassword("a").build();
		entity.setUser(u);
		u.setPerson(entity);
		rpc.users().add(u);
		testee.add(entity);
		uow.saveChanges();
		
		Person p = testee.byUser(u);
		assertValues(p, "a", 0);
	}
	
	private void assertValues(Person entity, String stringValue, int numericValue) {
		assertNotNull(entity);
		assertEquals(numericValue, entity.getAge());
		assertEquals(stringValue, entity.getName());
		assertEquals(stringValue, entity.getSurname());
	}
	
	private void setValues(Person entity, String stringValue, int numericValue) {
		entity.setName(stringValue);
		entity.setSurname(stringValue);
		entity.setAge(numericValue);
	}
	
	private void storeExampleEntity() throws SQLException {
		testee.add(entity);
		uow.saveChanges();
		
		assertTrue(hasEntries());
	}
	
	private boolean hasEntries() throws SQLException {
		return super.hasEntries(PersonRepository.TABLE_NAME);
	}

}
