package dao.uow;

import domain.Entity;

public interface IUnitOfWorkRepository {

	public void persistAdd(Entity entity);
	public void persistDelete(Entity entity);
	public void persistUpdate(Entity entity);
	
}
