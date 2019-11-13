package com.istonesoft.qdts.connection;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.alibaba.fastjson.JSON;
import com.istonesoft.qdts.context.QdProviderContext;
import com.istonesoft.qdts.jdbc.QdJdbcTemplate;
import com.istonesoft.qdts.resource.QdResult;
/**
 * 提供方的connection
 * @author issuser
 *
 */
public class QdProviderConnection extends QdConnection {

	public QdProviderConnection(Connection connection) {
		super(connection);
	}

	public void realCommit(QdJdbcTemplate jdbcTemplate, QdResult result) throws SQLException {
		try {
			jdbcTemplate.executeSqlNoCommit(this, "update t_qd_provider set status='SUCCESS',invoke_count=invoke_count+1,update_time=now(), result=? where group_id=?", new Object[] {JSON.toJSONString(result), QdProviderContext.getQdGroupId()});
			connection.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			connection.close();
		}
	}
	
	public void realRollback(QdJdbcTemplate jdbcTemplate, DataSource ds, QdResult result) throws SQLException {
		try {
			jdbcTemplate.executeSql(ds, "update t_qd_provider set exception=?,invoke_count=invoke_count+1,update_time=now()  where group_id=?", new Object[] {result.getMsg(), QdProviderContext.getQdGroupId()});
			connection.rollback();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			connection.close();
		}
	}
	
}
