package domain;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import dao.IRepositoryCatalog;
import dao.RepositoryCatalog;
import domain.builders.PermissionBuilder;
import domain.builders.PersonBuilder;
import domain.builders.RoleBuilder;
import domain.builders.RolePermissionBuilder;
import domain.builders.UserBuilder;
import domain.builders.UserRoleBuilder;

public class App 
{
	private static final String CONNECTION_STRING = "jdbc:hsqldb:hsql://localhost/workdb";
//	private static final String CONNECTION_STRING = "jdbc:hsqldb:mem:.";
	
	private static IRepositoryCatalog rpc;
	
    public static void main(String[] args) {
    	try {
    		Connection connection = DriverManager.getConnection(CONNECTION_STRING);
    		rpc = new RepositoryCatalog(connection);
			createData();
    		rpc.saveAndClose();
    		
    		connection = DriverManager.getConnection(CONNECTION_STRING);
    		rpc = new RepositoryCatalog(connection);
    		fetchData();
    		rpc.saveAndClose();
    		
    		connection = DriverManager.getConnection(CONNECTION_STRING);
    		rpc = new RepositoryCatalog(connection);
    		updateData();
    		rpc.saveAndClose();
    		
    		connection = DriverManager.getConnection(CONNECTION_STRING);
    		rpc = new RepositoryCatalog(connection);
    		fetchData();
    		rpc.saveAndClose();
    		
    		connection = DriverManager.getConnection(CONNECTION_STRING);
    		rpc = new RepositoryCatalog(connection);
    		create2Users();
    		rpc.saveAndClose();
    		
    		connection = DriverManager.getConnection(CONNECTION_STRING);
    		rpc = new RepositoryCatalog(connection);
    		deleteData();
    		rpc.saveAndClose();
    		
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	
        System.out.println("koniec!");
    }

	private static void createData() {
		Person person = new PersonBuilder().withName("Janek").withSurname("Kowalski").withAge(30).build();
		
		User u = new UserBuilder().withLogin("adam").withPassword("abc").build();
		Set<User> users = new HashSet<>();
		users.add(u);
		
		Role r = new RoleBuilder().withName("admin").build();
		Set<Role> roles = new HashSet<>();
		roles.add(r);
		
		Permission p = new PermissionBuilder().withName("full").withRoles(roles).build();
		Set<Permission> permissions = new HashSet<>();
		permissions.add(p);
		
		u.setPermissions(permissions);
		u.setRoles(roles);
		u.setPerson(person);
		r.setUsers(users);
		
		rpc.people().add(person);
		rpc.permissions().add(p);
		rpc.users().add(u);
		rpc.roles().add(r);
		rpc.usersRoles().add(new UserRoleBuilder().withUser(u).withRole(r).build());
		rpc.rolesPermissions().add(new RolePermissionBuilder().withRole(r).withPermission(p).build());
	}
	
	private static void fetchData() {
		// zobacz dlugi opis w RepositoryBase na temat obiektow zarzadzanych 
		// by zrozumiec dlaczego relacje sie propaguja w obiektach automatycznie.
		User user = rpc.users().get(0);
		Person person = rpc.people().byUser(user);
		
		rpc.roles().byUser(user);
		user.getRoles().stream()
						.forEach(role -> rpc.permissions().byRole(role));
		rpc.permissions().byUser(user);
		
		rpc.users().getAll();
		
	}
	
	private static void updateData() {
		User user = rpc.users().get(0);
		user.setLogin("Zdam");
		user.setPassword("Napewno");
		rpc.users().update(user);
	}
	
	private static void deleteData() {
		User user = rpc.users().withLogin("TestUser 1");
		rpc.users().delete(user);
	}
	
	private static void create2Users() throws SQLException {
		//@formatter:off
			rpc.users()
				.add(new UserBuilder()
						.withLogin("TestUser 1")
						.withPassword("Password 1")
						.build()
				);
			
			rpc.save();
			
			rpc.users()
			.add(new UserBuilder()
					.withLogin("TestUser 2")
					.withPassword("Password 2")
					.build()
			);
			
			rpc.save();
		//@formatter:on
	}
}
