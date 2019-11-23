package com.istonesoft.qdts.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import com.istonesoft.qdts.connection.QdServiceConnection;
public class JdbcTemplate extends AbstractJdbcTemplate {

	@Override
	public <T> T selectOne(DataSource ds, String sql, Object[] params,
			RowHandler<T> handler) throws Exception {

		Connection conn = ds.getConnection();
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

	@Override
	public <T> List<T> selectList(DataSource ds, String sql, Object[] params,
			RowHandler<T> handler) throws Exception {

		Connection conn = ds.getConnection();
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

	@Override
	public void executeImmediateCommit(DataSource ds, String sql,
			Object[] params) throws Exception {

		Connection conn = ds.getConnection();
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

	@Override
	public void executeWaitCommit(QdServiceConnection conn, String sql,
			Object[] params) throws Exception {

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
