package dao.uow;

import domain.Entity;

public interface IUnitOfWork {

	public void saveChanges();
	public void undo();
	public void markAsNew(Entity entity, IUnitOfWorkRepository repository);
	public void markAsDeleted(Entity entity, IUnitOfWorkRepository repository);
	public void markAsChanged(Entity entity, IUnitOfWorkRepository repository);
	
}
