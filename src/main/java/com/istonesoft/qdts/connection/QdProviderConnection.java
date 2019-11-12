package com.istonesoft.qdts.connection;

import java.sql.Connection;
import java.sql.SQLException;

import com.alibaba.fastjson.JSON;
import com.istonesoft.qdts.jdbc.QdJdbcTemplate;
import com.istonesoft.qdts.resource.NameThreadLocal;
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

	public void realCommit(QdJdbcTemplate jdbcTemplate, Object result) throws SQLException {
		try {
			jdbcTemplate.executeSqlNoCommit(this, "update t_qd_provider set status='SUCCESS',invoke_count=invoke_count+1,update_time=now(), result=? where group_id=?", new Object[] {JSON.toJSONString(result), NameThreadLocal.get("qdGroupId")});
			connection.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			connection.close();
		}
		
		
	}
	
	public void realRollback(QdJdbcTemplate jdbcTemplate, Connection conn, QdResult result) throws SQLException {
		try {
			jdbcTemplate.executeSql(conn, "update t_qd_provider set exception=?,invoke_count=invoke_count+1,update_time=now()  where group_id=?", new Object[] {result.getMsg(), NameThreadLocal.get("qdGroupId")});
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
