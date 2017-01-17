package domain;

public abstract class Entity implements IHaveId {
	
	private EntityState state;
	
	public EntityState getState() {
		return state;
	}
	
	public void setState(EntityState state) {
		this.state = state;
	}
	
	public enum EntityState{
		New, Modified, UnChanged, Deleted, Unknown
	}
	
	

}
