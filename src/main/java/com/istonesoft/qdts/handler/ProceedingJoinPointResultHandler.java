package com.istonesoft.qdts.handler;

import java.sql.SQLException;

import javax.sql.DataSource;

import com.istonesoft.qdts.jdbc.QdJdbcTemplate;
import com.istonesoft.qdts.resource.QdResult;
/**
 * controller返回结果处理器
 * @author Administrator
 *
 */
public interface ProceedingJoinPointResultHandler {

	public abstract void successHandle(QdJdbcTemplate jdbcTemplate, DataSource ds, QdResult result) throws SQLException;
	
	public abstract void failHandle(QdJdbcTemplate jdbcTemplate, DataSource ds, QdResult result) throws SQLException;
	
}
