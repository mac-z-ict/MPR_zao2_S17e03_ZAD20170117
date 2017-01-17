package dao.uow;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import dao.AddressRepository;
import dao.IAddressRepository;
import dao.IPersonRepository;
import dao.PersonRepository;
import domain.Address;
import domain.Entity;
import domain.Entity.EntityState;

@RunWith(MockitoJUnitRunner.class)
public class UnitOfWorkTest {
	
	private IUnitOfWork testee;
	
	@Mock
	private Connection conn;
	
	@Mock
	private AddressRepository addressRepo;
	
	@Mock
	private PersonRepository personRepo;
	
	@Before
	public void before() throws SQLException {
		testee = new UnitOfWork(conn);
	}
	
	@Test
	public void persistAdd() throws SQLException {
		Address address = new Address();
		testee.markAsNew(address, addressRepo);
		assertThat(address.getState(), is(equalTo(EntityState.New)));
		testee.saveChanges();
		verify(addressRepo, times(1)).persistAdd(address);
		verify(conn, times(1)).commit();
	}
	
	@Test
	public void persistUpdate() throws SQLException {
		Address address = new Address();
		testee.markAsChanged(address, addressRepo);
		assertThat(address.getState(), is(equalTo(EntityState.Modified)));
		testee.saveChanges();
		verify(addressRepo, times(1)).persistUpdate(address);
		verify(conn, times(1)).commit();
	}
	
	@Test
	public void persistDelete() throws SQLException {
		Address address = new Address();
		testee.markAsDeleted(address, addressRepo);
		assertThat(address.getState(), is(equalTo(EntityState.Deleted)));
		testee.saveChanges();
		verify(addressRepo, times(1)).persistDelete(address);
		verify(conn, times(1)).commit();
	}
	
	@Test
	public void undo() throws SQLException {
		testee.undo();
		verify(conn, times(1)).rollback();
	}

}
