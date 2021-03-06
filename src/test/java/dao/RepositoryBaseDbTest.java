package dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import dao.mappers.IMapResultSetToEntity;
import domain.Entity;
import domain.IHaveId;

public class RepositoryBaseDbTest extends AbstractDbTest {
	
	private static final String TABLE_NAME = "testentity";
	private static final String CREATE_TABLE_QUERY = "CREATE TABLE " + TABLE_NAME + "("
			+ "id bigint GENERATED BY DEFAULT AS IDENTITY,"
			+ "dummy VARCHAR(2)"
			+ ")";
	private static final String INSERT_QUERY = "INSERT INTO " + TABLE_NAME + "(dummy) VALUES(?)";
	private static final String UPDATE_QUERY = "UPDATE " + TABLE_NAME + " SET dummy = ? WHERE id = ?";
	private static final String SELECT_ALL_QUERY = "SELECT * FROM " + TABLE_NAME;
	private static final String COUNT_QUERY = "SELECT COUNT(1) FROM " + TABLE_NAME;
	
	private RepositoryBase<TestEntity> testee;
	
	private IMapResultSetToEntity<TestEntity> mapper;
	
	@Before
	public void before() throws SQLException {
		prepareMapper();
		prepareRepository();
		assertFalse(hasEntries());
	}
	
	@Test
	public void create() throws SQLException {
		ResultSet rs = null;
		
		TestEntity entity = new TestEntityBuilder().withDummy("a").build();
		testee.add(entity);
		uow.saveChanges();
		
		assertTrue(hasEntries());
		
		rs = connection.createStatement().executeQuery(SELECT_ALL_QUERY);
		rs.next();
		
		assertEquals(0, rs.getInt("id"));
		assertEquals("a", rs.getString("dummy"));
	}
	
	@Test
	public void read() throws SQLException {
		TestEntity entity = new TestEntityBuilder().withDummy("a").build();
		testee.add(entity);
		uow.saveChanges();
		
		entity = null;
		
		assertTrue(hasEntries());
		
		entity = testee.get(0);
		assertNotNull(entity);
		assertEquals(0, entity.getId());
		assertEquals("a", entity.getDummy());
	}
	
	@Test
	public void update() throws SQLException {
		TestEntity entityWrite = new TestEntityBuilder().withDummy("a").build();
		testee.add(entityWrite);
		uow.saveChanges();
		
		assertTrue(hasEntries());
		
		TestEntity entityRead = testee.get(0);
		assertNotNull(entityRead);
		assertEquals(0, entityRead.getId());
		assertEquals("a", entityRead.getDummy());
		
		entityRead = null;
		
		entityWrite.setDummy("b");
		testee.update(entityWrite);
		uow.saveChanges();
		
		assertTrue(hasEntries());
		
		entityRead = testee.get(0);
		assertNotNull(entityRead);
		assertEquals(0, entityRead.getId());
		assertEquals("b", entityRead.getDummy());
	}
	
	@Test
	public void delete() throws SQLException {
		TestEntity entityWrite = new TestEntityBuilder().withDummy("a").build();
		testee.add(entityWrite);
		uow.saveChanges();
		
		assertTrue(hasEntries());
		
		testee.delete(entityWrite);
		uow.saveChanges();
		
		assertFalse(hasEntries());
	}
	
	@Test
	public void list() throws SQLException {
		TestEntity entityWrite1 = new TestEntityBuilder().withDummy("a").build();
		TestEntity entityWrite2 = new TestEntityBuilder().withDummy("b").build();
		testee.add(entityWrite1);
		uow.saveChanges();
		testee.add(entityWrite2);
		uow.saveChanges();
		
		assertTrue(hasEntries());
		
		List<TestEntity> list = testee.getAll();
		assertNotNull(list);
		assertEquals(2, list.size());
		assertEquals("a", list.get(0).getDummy());
		assertEquals("b", list.get(1).getDummy());
	}

	private void prepareRepository() {
		testee = new RepositoryBase<TestEntity>(connection, mapper, uow) {

			@Override
			protected void setUpdateQuery(TestEntity p) throws SQLException {
				update.setString(1, p.getDummy());
				update.setInt(2, p.getId());
			}

			@Override
			protected void setInsertQuery(TestEntity p) throws SQLException {
				insert.setString(1, p.getDummy());
			}

			@Override
			protected String tableName() {
				return TABLE_NAME;
			}

			@Override
			protected String createTableSql() {
				return CREATE_TABLE_QUERY;
			}

			@Override
			protected String insertSql() {
				return INSERT_QUERY;
			}

			@Override
			protected String updateSql() {
				return UPDATE_QUERY;
			}

		};
	}

	private void prepareMapper() {
		mapper = rs -> {
			TestEntity te = new TestEntity();
			te.setId(rs.getInt("id"));
			te.setDummy(rs.getString("dummy"));
			return te;
		};
	}
	
	private boolean hasEntries() throws SQLException {
		return super.hasEntries(TABLE_NAME);
	}
	
	class TestEntity extends Entity implements IHaveId {
		
		private int id;
		private String dummy;
		
		public TestEntity() {}
		
		public TestEntity(int id) {
			this.id = id;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}
		
		public String getDummy() {
			return dummy;
		}

		public void setDummy(String dummy) {
			this.dummy = dummy;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + id;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			TestEntity other = (TestEntity) obj;
			if (!getOuterType().equals(other.getOuterType())) {
				return false;
			}
			if (id != other.id) {
				return false;
			}
			return true;
		}

		private RepositoryBaseDbTest getOuterType() {
			return RepositoryBaseDbTest.this;
		}
	}
	
	class TestEntityBuilder {
		private TestEntity te;
		
		public TestEntityBuilder() {
			te = new TestEntity();
		}
		
		public TestEntityBuilder withId(int id) {
			te.setId(id);
			return this;
		}
		
		public TestEntityBuilder withDummy(String dummy) {
			te.setDummy(dummy);
			return this;
		}
		
		public TestEntity build() {
			return te;
		}
	}
}
