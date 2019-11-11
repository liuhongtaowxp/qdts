package com.istonesoft.qdts.jdbc;

import java.sql.ResultSet;

public interface RowHandler<T> {

	public T handle(ResultSet rs);
	
}
