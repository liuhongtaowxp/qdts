package com.istonesoft.qdts.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.istonesoft.qdts.connection.QdConnection;
import com.istonesoft.qdts.context.QdConsumerContext;
/**
 * jdbc执行模板
 * @author issuser
 *
 */
@Component
public class QdJdbcTemplate {
	/**
	 * 执行查询
	 * @param conn
	 * @param sql
	 * @param params
	 * @param handler
	 * @return
	 * @throws Exception
	 */
	public <T> T executeQuerySingle(Connection conn, String sql, Object[] params, RowHandler<T> handler) throws Exception {
		PreparedStatement prepareStatement = null;
		try {
			conn.setReadOnly(true);
			prepareStatement = conn.prepareStatement(sql);
			int index = 0;
			for (Object param : params) {
				prepareStatement.setObject(++index, param);
			}
			ResultSet rs = prepareStatement.executeQuery();
			while (rs.next()) {
				return handler.handle(rs);
			}
		} catch (SQLException e) {
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
		return null;
	}
	/**
	 * 执行查询
	 * @param conn
	 * @param sql
	 * @param params
	 * @param handler
	 * @return
	 * @throws Exception
	 */
	public <T> List<T> executeQuery(Connection conn, String sql, Object[] params, RowHandler<T> handler) throws Exception {
		List<T> list = new ArrayList<T>();
		PreparedStatement prepareStatement = null;
		try {
			conn.setReadOnly(true);
			prepareStatement = conn.prepareStatement(sql);
			int index = 0;
			for (Object param : params) {
				prepareStatement.setObject(++index, param);
			}
			ResultSet rs = prepareStatement.executeQuery();
			while (rs.next()) {
				list.add(handler.handle(rs));
			}
			return list;
		} catch (SQLException e) {
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
