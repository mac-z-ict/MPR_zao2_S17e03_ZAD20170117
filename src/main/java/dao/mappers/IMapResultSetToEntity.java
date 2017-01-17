package dao.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface IMapResultSetToEntity<TEntity> {
	public TEntity map(ResultSet rs) throws SQLException;
}
