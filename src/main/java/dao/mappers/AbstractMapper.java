package dao.mappers;

import dao.IRepositoryCatalog;

public abstract class AbstractMapper {
	
	protected IRepositoryCatalog rpc;
	
	public AbstractMapper(IRepositoryCatalog rpc) {
		this.rpc = rpc;
	}

}
