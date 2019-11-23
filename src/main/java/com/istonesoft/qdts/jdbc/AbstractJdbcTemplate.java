package com.istonesoft.qdts.jdbc;

import java.util.List;

import javax.sql.DataSource;

import com.istonesoft.qdts.connection.QdServiceConnection;

public abstract class AbstractJdbcTemplate {
	
	public abstract <T> T selectOne(DataSource ds, String sql, Object[] params, RowHandler<T> handler) throws Exception;
	
	public abstract <T> List<T> selectList(DataSource ds, String sql, Object[] params, RowHandler<T> handler) throws Exception;
	
	public abstract void executeImmediateCommit(DataSource ds, String sql, Object[] params) throws Exception;
	
	public abstract void executeWaitCommit(QdServiceConnection conn, String sql, Object[] params) throws Exception;
	
}
