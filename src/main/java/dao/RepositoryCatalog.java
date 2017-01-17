package dao;

import java.sql.Connection;
import java.sql.SQLException;

import dao.mappers.AddressMapper;
import dao.mappers.PermissionMapper;
import dao.mappers.PersonMapper;
import dao.mappers.RoleMapper;
import dao.mappers.RolePermissionMapper;
import dao.mappers.UserMapper;
import dao.mappers.UserRoleMapper;
import dao.uow.IUnitOfWork;
import dao.uow.UnitOfWork;

public class RepositoryCatalog implements IRepositoryCatalog {
	
	private final IPersonRepository peopleRepository;
	private final IAddressRepository addressRepository;
	
	private final IUserRepository userRepository;
	private final IRoleRepository roleRepository;
	private final IPermissionRepository permissionRepository;
	
	private final IUserRoleRepository usersRolesRepository;
	private final IRolePermissionRepository rolesPermissionsRepository;
	
	private final IUnitOfWork uow;
	private final Connection connection;
	
	public RepositoryCatalog(Connection connection) throws SQLException {
		this(connection, new UnitOfWork(connection));
	}
	
	RepositoryCatalog(Connection connection, IUnitOfWork uow) throws SQLException {
		this.connection = connection;
		this.uow = uow;
		
		rolesPermissionsRepository = new RolePermissionRepository(connection, new RolePermissionMapper(this), uow);
		usersRolesRepository = new UserRoleRepository(connection, new UserRoleMapper(this), uow);
		
		userRepository = new UserRepository(connection, new UserMapper(this), uow);
		peopleRepository = new PersonRepository(connection, new PersonMapper(this), uow);
		addressRepository = new AddressRepository(connection, new AddressMapper(this), uow);
		
		permissionRepository = new PermissionRepository(connection, new PermissionMapper(this), uow);
		roleRepository = new RoleRepository(connection, new RoleMapper(this), uow);
	}
	
	@Override
	public IPersonRepository people() {
		return peopleRepository;
	}

	@Override
	public IAddressRepository addresses() {
		return addressRepository;
	}
	
	@Override
	public IUserRepository users() {
		return userRepository;
	}

	@Override
	public IRoleRepository roles() {
		return roleRepository;
	}
	
	@Override
	public IPermissionRepository permissions() {
		return permissionRepository;
	}

	@Override
	public IUserRoleRepository usersRoles() {
		return usersRolesRepository;
	}
	
	@Override
	public IRolePermissionRepository rolesPermissions() {
		return rolesPermissionsRepository;
	}
	
	/*
	 * Poniewaz ustawilem wszystkie pola tej klasy jako finalne to nie mozna zanullowac obiektu connection.
	 * Skoro nie mozna tego zrobic to connection = null; usunalem.
	 */
	public void save() throws SQLException {
		uow.saveChanges();
	}
	
	public void close() throws SQLException {
		connection.close();		
	}
	
	public void saveAndClose() throws SQLException {
		save();
		close();
	}
}
