package com.istonesoft.qdts.connection;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.istonesoft.qdts.context.QdConsumerContext;
import com.istonesoft.qdts.jdbc.QdJdbcTemplate;
import com.istonesoft.qdts.resource.QdResult;
/**
 * 消费方的connection
 * @author issuser
 *
 */
public class QdConsumerConnection extends QdConnection {

	public QdConsumerConnection(Connection connection) {
		super(connection);
	}

	public void realCommit(QdJdbcTemplate jdbcTemplate, QdResult result) throws SQLException {
		try {
			jdbcTemplate.executeSqlNoCommit(this, "update t_qd_consumer set status='SUCCESS',invoke_count=invoke_count+1,update_time=now() where group_id=?", new Object[] {QdConsumerContext.getQdGroupId()});
			connection.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			connection.close();
		}
	}
	
	public void realRollback(QdJdbcTemplate jdbcTemplate, DataSource ds, QdResult result) throws SQLException {
		try {
			jdbcTemplate.executeSql(ds, "update t_qd_consumer set exception=?,invoke_count=invoke_count+1,update_time=now() where group_id=?", new Object[] {result.getMsg(), QdConsumerContext.getQdGroupId()});
			connection.rollback();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			connection.close();
		}
	}
	
}
