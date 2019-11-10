package com.istonesoft.qdts.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.springframework.stereotype.Component;
import com.istonesoft.qdts.connection.QdConnection;
/**
 * jdbc执行模板
 * @author issuser
 *
 */
@Component
public class QdJdbcTemplate {
	/**
	 * 马上commit
	 * @param conn
	 * @param sql
	 * @param params
	 * @throws Exception
	 */
	public void executeSql(Connection conn, String sql, Object[] params) throws Exception {
		PreparedStatement prepareStatement = null;
		conn.setAutoCommit(false);
		try {
			prepareStatement = conn.prepareStatement(sql);
			int index = 0;
			for (Object param : params) {
				prepareStatement.setObject(++index, param);
			}
			prepareStatement.execute();
			conn.commit();
		} catch (SQLException e) {
			conn.rollback();
			throw e;
		} finally {
			try {
				if (prepareStatement != null) {
					prepareStatement.close();
				}
				if (conn != null) {
					conn.close();
				}
				
			} catch (SQLException e1) {
				throw e1;
			}
		}
		
	}
	/**
	 * 不需要commit，等事务管理commit
	 * @param conn
	 * @param sql
	 * @param params
	 * @throws Exception
	 */
	public void executeSqlNoCommit(QdConnection conn, String sql, Object[] params) throws Exception {
		Connection pureConnection = conn.getConnection();
		PreparedStatement prepareStatement = null;
		try {
			prepareStatement = pureConnection.prepareStatement(sql);
			int index = 0;
			for (Object param : params) {
				prepareStatement.setObject(++index, param);
			}
			prepareStatement.execute();
		} catch (SQLException e) {
			throw e;
		} finally {
			try {
				if (prepareStatement != null) {
					prepareStatement.close();
				}
				
			} catch (SQLException e1) {
				throw e1;
			}
		}
		
	}
}
