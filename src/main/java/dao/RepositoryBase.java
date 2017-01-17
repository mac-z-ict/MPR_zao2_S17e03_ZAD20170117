package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import dao.mappers.IMapResultSetToEntity;
import dao.uow.IUnitOfWork;
import dao.uow.IUnitOfWorkRepository;
import domain.Entity;
import domain.IHaveId;
import domain.Person;

public abstract class RepositoryBase<TEntity extends IHaveId>
	implements IUnitOfWorkRepository, IRepository<TEntity>{

	protected Connection connection;
	protected Statement createTable;
	protected PreparedStatement insert;
	protected PreparedStatement delete;
	protected PreparedStatement update;
	protected PreparedStatement get;
	protected PreparedStatement list;
	protected IMapResultSetToEntity<TEntity> mapper;
	protected IUnitOfWork uow;
	
	
	private Set<TEntity> managedEntities = new HashSet<>();
	
	public RepositoryBase(Connection connection,
			IMapResultSetToEntity<TEntity> mapper,
			IUnitOfWork uow) {

		try {
			this.uow = uow;
			this.mapper = mapper;
			this.connection = connection;
			createTable = connection.createStatement();
			
			
			createTable(tableName(), createTableSql());

			insert = connection.prepareStatement(insertSql());
			delete = connection.prepareStatement(deleteSql());
			update = connection.prepareStatement(updateSql());
			get = connection.prepareStatement(getSql());
			list = connection.prepareStatement(listSql());

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	protected void createTable(String tableName, String createTableQuery) throws SQLException {
		ResultSet rs = connection.getMetaData().getTables(null, null, null, null);
		boolean tableExists = false;
		while (rs.next()) {
//			if (!"INFORMATION_SCHEMA".equals(rs.getString("TABLE_SCHEM")) && !"SYSTEM_LOBS".equals(rs.getString("TABLE_SCHEM"))) {
//				System.out.println("Processing: "+rs.getString("TABLE_NAME"));
//			}
//			System.out.println("Processing: "+rs.getString("TABLE_NAME"));
			if (tableName.equalsIgnoreCase(rs.getString("TABLE_NAME"))) {
				tableExists = true;
				break;
			}
		}
		
//		System.out.println("Table: "+tableName+" | Found: "+ tableExists);
		
		if (!tableExists) {
			connection.createStatement().executeUpdate(createTableQuery);
		}
	}

	/* (non-Javadoc)
	 * @see dao.IRepository#persistDelete(domain.Entity)
	 */
	public void persistDelete(Entity p) {
		try {
			delete.setInt(1, p.getId());
			delete.executeUpdate();
			unmanage((TEntity) p); // to specjalna metoda wylaczajaca spod zarzadzania obiekt
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see dao.IRepository#delete(TEntity)
	 */
	public void delete(TEntity entity) {
		uow.markAsDeleted((Entity)entity, this);
	}

	/* (non-Javadoc)
	 * @see dao.IRepository#persistUpdate(domain.Entity)
	 */
	public void persistUpdate(Entity p) {
		try {
			setUpdateQuery((TEntity)p);
			update.executeUpdate();
			
			addManage((TEntity)p);
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
	/* (non-Javadoc)
	 * @see dao.IRepository#update(TEntity)
	 */
	public void update(TEntity entity) {
		uow.markAsChanged((Entity)entity, this);
	}

	/* (non-Javadoc)
	 * @see dao.IRepository#persistAdd(domain.Entity)
	 */
	public void persistAdd(Entity p) {
		try {
			setInsertQuery((TEntity)p);
			insert.executeUpdate();
			
			getManaged(get(p.getId())); // dodaj do obiektow zarzadzanych nowo zapisany obiekt w bazie danych tzn pobierz go z bazy (by mial id) i zapisz go w obiektach zarzadzanych
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
	/* (non-Javadoc)
	 * @see dao.IRepository#add(TEntity)
	 */
	public void add(TEntity entity) {
		uow.markAsNew((Entity)entity, this);
	}

	/* (non-Javadoc)
	 * @see dao.IRepository#getAll()
	 */
	public List<TEntity> getAll() {
		List<TEntity> persons = new ArrayList<TEntity>();

		try {
			ResultSet rs = list.executeQuery();

			while (rs.next()) {
				TEntity entity = mapper.map(rs);
				entity = getManaged(entity);
				persons.add(entity);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return persons;
	}

	/* (non-Javadoc)
	 * @see dao.IRepository#get(int)
	 */
	public TEntity get(int id) {
		try {
			get.setInt(1, id);
			ResultSet rs = get.executeQuery();
			boolean hasRows = rs.next();
			if (hasRows) {
				TEntity entity = mapper.map(rs);
				entity = getManaged(entity); 
	// uzywaj obiektow zarzadzanych jesli mozna bo zarzadzane maja odtworzone relacje w runtimie
				return entity;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private void addManage(TEntity entity) {
		managedEntities.remove(entity);
		managedEntities.add(entity);
	}
	
	// jesli obiekt jest w obiektach zarzadzanych to zwroc ten zarzadzany. jesli nie to dodaj ten z argumentu i zwroc go po dodaniu juz jako zarzadany
	private TEntity getManaged(TEntity entity) {
		if (managedEntities.contains(entity)) {
			return managedEntities.stream().filter(e -> e.equals(entity)).findFirst().orElse(entity);
		} else {
			addManage(entity);
		}
		return entity;
	}
	
	// przestan zarzadzac obiektem podanym w argumencie. przestanie miec aktualizowane relacje w runtimie
	private TEntity unmanage(TEntity entity) {
		if (managedEntities.contains(entity)) {
			TEntity managedEntity = getManaged(entity);
			managedEntities.remove(managedEntity);
			return managedEntity;
		}
		return entity;
	}

	protected String deleteSql() {
		return "DELETE FROM " + tableName() + " WHERE id=?";
	}

	protected String getSql() {
		return "SELECT * FROM " + tableName() + " WHERE id = ?";
	}

	protected String listSql() {
		return "SELECT * FROM " + tableName();
	}

	protected abstract void setUpdateQuery(TEntity p) throws SQLException;

	protected abstract void setInsertQuery(TEntity p) throws SQLException;

	protected abstract String tableName();

	protected abstract String createTableSql();

	protected abstract String insertSql();

	protected abstract String updateSql();

}
