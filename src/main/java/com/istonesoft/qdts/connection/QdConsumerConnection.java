package com.istonesoft.qdts.connection;

import java.sql.Connection;
import java.sql.SQLException;

import com.istonesoft.qdts.jdbc.QdJdbcTemplate;
import com.istonesoft.qdts.resource.NameThreadLocal;
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

	public void realCommit(QdJdbcTemplate jdbcTemplate, Object result) throws SQLException {
		try {
			jdbcTemplate.executeSqlNoCommit(this, "update t_qd_consumer set status='SUCCESS' where group_id=?", new Object[] {NameThreadLocal.get("qdGroupId")});
			connection.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			connection.close();
		}
		
		
	}
	
	public void realRollback(QdJdbcTemplate jdbcTemplate, Connection conn, QdResult result) throws SQLException {
		try {
			jdbcTemplate.executeSql(conn, "update t_qd_consumer set exception=? where group_id=?", new Object[] {result.getMsg(), NameThreadLocal.get("qdGroupId")});
			connection.rollback();
		} catch (SQLException e1) {
			e1.printStackTrace();
		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			connection.close();
		}
	}
	

}
