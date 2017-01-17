package dao;

import java.util.List;

import domain.IHaveId;

public interface IRepository<TEntity extends IHaveId> {

	public void delete(TEntity entity);

	public void update(TEntity entity);

	public void add(TEntity entity);

	public List<TEntity> getAll();

	public TEntity get(int id);

}