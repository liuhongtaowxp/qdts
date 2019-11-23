package com.istonesoft.qdts.jdbc;

import java.util.List;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.istonesoft.qdts.connection.QdServiceConnection;
import com.istonesoft.qdts.context.QdContextHolder;
import com.istonesoft.qdts.context.State;
/**
 * jdbc执行模板
 * @author issuser
 *
 */
@Component
public class QdJdbcTemplate extends AbstractJdbcTemplate {
	
	private JdbcTemplate JdbcTemplate = new JdbcTemplate();
	
	
	@Override
	public <T> T selectOne(DataSource ds, String sql, Object[] params,
			RowHandler<T> handler) throws Exception {
		try {
			QdContextHolder.getQdContext().setState(State.NOSERVICE);
			return JdbcTemplate.selectOne(ds, sql, params, handler);
		} finally {
			QdContextHolder.getQdContext().setState(State.SERVICE);
		}
		
	}

	@Override
	public <T> List<T> selectList(DataSource ds, String sql, Object[] params,
			RowHandler<T> handler) throws Exception {
		try {
			QdContextHolder.getQdContext().setState(State.NOSERVICE);
			return JdbcTemplate.selectList(ds, sql, params, handler);
		} finally {
			QdContextHolder.getQdContext().setState(State.SERVICE);
		}
	}

	@Override
	public void executeImmediateCommit(DataSource ds, String sql,
			Object[] params) throws Exception {
		try {
			QdContextHolder.getQdContext().setState(State.NOSERVICE);
			JdbcTemplate.executeImmediateCommit(ds, sql, params);
		} finally {
			QdContextHolder.getQdContext().setState(State.SERVICE);
		}
	}

	@Override
	public void executeWaitCommit(QdServiceConnection conn, String sql,
			Object[] params) throws Exception {
		JdbcTemplate.executeWaitCommit(conn, sql, params);
	}
	
	
}
