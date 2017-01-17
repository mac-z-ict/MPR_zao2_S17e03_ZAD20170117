package dao.uow;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

import domain.Entity;
import domain.Entity.EntityState;

public class UnitOfWork implements IUnitOfWork{

	private Connection connection;
	public UnitOfWork(Connection connection) throws SQLException {
		this.connection = connection;
		this.connection.setAutoCommit(false);
	}

	private Map<Entity, IUnitOfWorkRepository> entities=
			new LinkedHashMap<Entity, IUnitOfWorkRepository>();
	
	
	public void saveChanges() {
		
		for(Entity entity : entities.keySet()){
			
			switch(entity.getState()){
			case Deleted:
				entities.get(entity).persistDelete(entity);
				break;
			case Modified:
				entities.get(entity).persistUpdate(entity);
				break;
			case New:
				entities.get(entity).persistAdd(entity);
				break;
			default:
				break;
			}
		}
		
		try {
			connection.commit();
			entities.clear();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	public void undo() {
		try {
			connection.rollback();
			entities.clear();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	public void markAsNew(Entity entity, IUnitOfWorkRepository repository) {
		entity.setState(EntityState.New);
		entities.put(entity, repository);
	}

	public void markAsDeleted(Entity entity, IUnitOfWorkRepository repository) {
		entity.setState(EntityState.Deleted);
		entities.put(entity, repository);
	}

	public void markAsChanged(Entity entity, IUnitOfWorkRepository repository) {
		entity.setState(EntityState.Modified);
		entities.put(entity, repository);
	}
	
	/*
	protected void markAs(Entity entity, IUnitOfWorkRepository repository, EntityState state) {
		entity.setState(state);
		entities.put(entity, repository);
	}

	public void markAsNew(Entity entity, IUnitOfWorkRepository repository) {
		markAs(entity, repository, EntityState.New);
	}

	public void markAsDeleted(Entity entity, IUnitOfWorkRepository repository) {
		markAs(entity, repository, EntityState.Deleted);
	}

	public void markAsChanged(Entity entity, IUnitOfWorkRepository repository) {
		markAs(entity, repository, EntityState.Modified);
	}
	*/
}
