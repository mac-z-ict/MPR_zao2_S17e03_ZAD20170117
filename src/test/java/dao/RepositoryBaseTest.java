package dao;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import dao.mappers.IMapResultSetToEntity;
import dao.uow.IUnitOfWork;
import domain.Entity;
import domain.IHaveId;

@RunWith(MockitoJUnitRunner.class)
public class RepositoryBaseTest {

	@Mock
	private Connection conn;

	@Mock
	private IMapResultSetToEntity<IHaveId> mapper;

	@Mock
	private IUnitOfWork uow;

	private RepositoryBase<IHaveId> testee;

	@Mock
	private IHaveId mappedEntity;
	
	@Mock
	private Entity entity;

	@Before
	public void before() throws SQLException {
		prepareMocks();
		testee = new RepositoryBase<IHaveId>(conn, mapper, uow) {

			@Override
			protected void setUpdateQuery(IHaveId p) throws SQLException {
			}

			@Override
			protected void setInsertQuery(IHaveId p) throws SQLException {
			}

			@Override
			protected String tableName() {
				return "test";
			}

			@Override
			protected String createTableSql() {
				return "create";
			}

			@Override
			protected String insertSql() {
				return "insert";
			}

			@Override
			protected String updateSql() {
				return "update";
			}
		};
	}

	protected void prepareMocks() throws SQLException {
		prepareConnectionMocks();
		prepareMapperMocks();
		
		when(entity.getId()).thenReturn(0);
	}

	protected void prepareConnectionMocks() throws SQLException {
		DatabaseMetaData dbMetadata = mock(DatabaseMetaData.class);
		ResultSet rs = mock(ResultSet.class);
		Statement s = mock(Statement.class);
		PreparedStatement ps = mock(PreparedStatement.class);

		when(conn.getMetaData()).thenReturn(dbMetadata);
		when(dbMetadata.getTables(any(), any(), any(), any())).thenReturn(rs);

		when(conn.getMetaData().getTables(null, null, null, null)).thenReturn(rs);
		when(conn.createStatement()).thenReturn(s);
		when(s.executeUpdate(any(String.class))).thenReturn(0);

		when(conn.prepareStatement(any(String.class))).thenReturn(ps);
		when(ps.executeQuery()).thenReturn(rs);
	}

	protected void prepareMapperMocks() throws SQLException {
		when(mapper.map(any(ResultSet.class))).thenReturn(mappedEntity);
	}

	@Test
	public void initializationTest() {
		assertThat(testee.createTable, notNullValue());
		assertThat(testee.insert, notNullValue());
		assertThat(testee.delete, notNullValue());
		assertThat(testee.update, notNullValue());
		assertThat(testee.get, notNullValue());
		assertThat(testee.list, notNullValue());
	}

	@Test
	public void createTable() throws SQLException {
		Answer<Boolean> answer = new Answer<Boolean>() {
			
			private boolean result = true;

			@Override
			public Boolean answer(InvocationOnMock invocation) throws Throwable {
				boolean result = this.result;
				this.result = !result;
				return result;
			}
		};
		ResultSet tablesRs = conn.getMetaData().getTables(null, null, null, null);
		verify(tablesRs, times(1)).next();
		when(tablesRs.next()).thenAnswer(answer);
		
		testee.createTable(testee.tableName(), testee.createTableSql());
		verify(tablesRs, times(3)).next();
		
		verify(testee.createTable, times(2)).executeUpdate(testee.createTableSql());
		verifyNoMoreInteractions(testee.createTable);
	}
	
	@Test
	public void persistDelete() throws SQLException {
		testee.persistDelete(entity);
		verify(testee.delete, times(1)).executeUpdate();
	}
	
	@Test
	public void delete() {
		testee.delete(entity);
		verify(uow, times(1)).markAsDeleted(entity, testee);
	}
	
	@Test
	public void persistUpdate() throws SQLException {
		testee.persistUpdate(entity);
		verify(testee.update, times(1)).executeUpdate();
	}
	
	@Test
	public void update() {
		testee.update(entity);
		verify(uow, times(1)).markAsChanged(entity, testee);
	}
	
	@Test
	public void persistAdd() throws SQLException {
		testee.persistAdd(entity);
		verify(testee.insert, times(1)).executeUpdate();
	}
	
	@Test
	public void add() {
		testee.add(entity);
		verify(uow, times(1)).markAsNew(entity, testee);
	}
	
	
}
