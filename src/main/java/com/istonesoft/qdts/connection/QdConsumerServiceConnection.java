package com.istonesoft.qdts.connection;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.istonesoft.qdts.context.QdConsumerContext;
import com.istonesoft.qdts.context.QdContextHolder;
import com.istonesoft.qdts.jdbc.QdJdbcTemplate;
import com.istonesoft.qdts.resource.QdResult;
/**
 * 消费方的connection
 * @author issuser
 *
 */
public class QdConsumerServiceConnection extends QdServiceConnection {

	public QdConsumerServiceConnection(Connection connection) {
		super(connection);
	}

	public void successHandle(QdJdbcTemplate jdbcTemplate, DataSource ds, QdResult result) throws SQLException {
		try {
			jdbcTemplate.executeWaitCommit(this, "update t_qd_consumer set status='SUCCESS',invoke_count=invoke_count+1,update_time=now() where group_id=?", new Object[] {QdContextHolder.getQdContext(true).getQdGroupId()});
			connection.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			connection.close();
		}
	}
	
	public void failHandle(QdJdbcTemplate jdbcTemplate, DataSource ds, QdResult result) throws SQLException {
		try {
			jdbcTemplate.executeImmediateCommit(ds, "update t_qd_consumer set exception=?,invoke_count=invoke_count+1,update_time=now() where group_id=?", new Object[] {result.getMsg(), QdContextHolder.getQdContext(true).getQdGroupId()});
			connection.rollback();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			connection.close();
		}
	}
	
}
