package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;

import dao.uow.IUnitOfWork;
import dao.uow.UnitOfWork;

public abstract class AbstractDbTest {
	
	private static final String CONNECTION_STRING = "jdbc:hsqldb:mem:.";
//	private static final String CONNECTION_STRING = "jdbc:hsqldb:hsql://localhost/workdb";
	
	protected Connection connection;
	protected RepositoryCatalog rpc;
	protected IUnitOfWork uow;
	
	@Before
	public void superBefore() throws SQLException {
		connection = DriverManager.getConnection(CONNECTION_STRING);
		uow = new UnitOfWork(connection);
		rpc = new RepositoryCatalog(connection, uow);
	}
	
	@After
	public void clearDataFromDatabase() throws SQLException {
	    //Start transaction, based on your transaction manager
		connection.setAutoCommit(false);
	    connection.createStatement().executeQuery("TRUNCATE SCHEMA PUBLIC RESTART IDENTITY AND COMMIT NO CHECK");
	    connection.commit();
	    connection.setAutoCommit(true);
	    connection.close();
	    //Commit transaction
	}
	
	protected boolean hasEntries(String tableName) throws SQLException {
		ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM " + tableName);
		return rs.next();
	}

}
