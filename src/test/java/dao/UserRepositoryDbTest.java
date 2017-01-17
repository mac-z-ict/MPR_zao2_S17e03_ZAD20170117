package dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import domain.Person;
import domain.User;
import domain.builders.PersonBuilder;
import domain.builders.UserBuilder;


public class UserRepositoryDbTest extends AbstractDbTest {
	
	private IUserRepository testee;
	
	private User entity = new UserBuilder()
								.withLogin("a")
								.withPassword("a")
								.build();
	
	@Before
	public void before() throws SQLException {
		testee = rpc.users();
		assertFalse(hasEntries());
	}
	
	@Test
	public void create() throws SQLException {
		storeExampleEntity();
	}
	
	@Test
	public void read() throws SQLException {
		storeExampleEntity();
		
		User readEntity = testee.get(0);
		assertValues(readEntity, "a");
	}
	
	@Test
	public void update() throws SQLException {
		storeExampleEntity();
		
		User readEntity = testee.get(0);
		assertValues(readEntity, "a");
		
		setValues(entity, "b");
		testee.update(entity);
		uow.saveChanges();
		
		readEntity = testee.get(0);
		assertValues(readEntity, "b");
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
		User entity1 = new UserBuilder()
				.withLogin("a")
				.withPassword("a")
				.build();
		User entity2 = new UserBuilder()
				.withLogin("a")
				.withPassword("a")
				.build();
		
		setValues(entity2, "b");
		
		testee.add(entity1);
		uow.saveChanges();
		testee.add(entity2);
		uow.saveChanges();
		
		List<User> list = testee.getAll();
		assertNotNull(list);
		assertEquals(2, list.size());
		
		assertValues(list.get(0), "a");
		assertValues(list.get(1), "b");
	}
	
	@Test
	public void withLogin() throws SQLException {
		storeExampleEntity();
		
		User readEntity = testee.withLogin("b");
		assertNull(readEntity);
		
		readEntity = testee.withLogin("a");
		assertNotNull(readEntity);
		assertValues(readEntity, "a");
	}
	
	@Test
	public void withLoginAndPassword() throws SQLException {
		storeExampleEntity();
		
		User readEntity = testee.withLoginAndPassword("a", "b");
		assertNull(readEntity);
		
		readEntity = testee.withLoginAndPassword("a", "a");
		assertNotNull(readEntity);
		assertValues(readEntity, "a");
	}
	
	private void assertValues(User person, String stringValue) {
		assertNotNull(person);
		assertEquals(stringValue, person.getLogin());
		assertEquals(stringValue, person.getPassword());
	}
	
	private void setValues(User entity, String stringValue) {
		entity.setLogin(stringValue);
		entity.setPassword(stringValue);
	}
	
	private void storeExampleEntity() throws SQLException {
		testee.add(entity);
		uow.saveChanges();
		
		assertTrue(hasEntries());
	}
	
	private boolean hasEntries() throws SQLException {
		return super.hasEntries(UserRepository.TABLE_NAME);
	}

}
