package com.istonesoft.qdts.dao;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.istonesoft.qdts.jdbc.QdJdbcTemplate;
import com.istonesoft.qdts.jdbc.RowHandler;
@Component
public class QdProviderDao {
	@Autowired
	protected DataSource ds;
	@Autowired
	protected QdJdbcTemplate jdbcTemplate;
	
	public int getInvokeCount(String qdGroupId) throws Exception {
		return jdbcTemplate.selectOne(ds, "select count(1) from t_qd_provider where group_id=?", new String[] {qdGroupId}, new RowHandler<Integer>() {
			public Integer handle(ResultSet rs) {
				try {
					return rs.getInt(1);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return 0;
			}
		});
	}
	
	public void insertProviderEntity(String qdGroupId) throws Exception {
		
		jdbcTemplate.executeImmediateCommit(ds, "insert into t_qd_provider(group_id,status,exception,update_time,invoke_count) values(?,?,?,?,?)", new Object[] {
				qdGroupId,"PREPARE",null,new Date(),0	
		});
		
	}
	
	public String getSuccessResult(String qdGroupId) throws Exception {
		return jdbcTemplate.selectOne(ds, "select result from t_qd_provider where group_id=? and status='SUCCESS'", new String[] {qdGroupId}, new RowHandler<String>() {
			public String handle(ResultSet rs) {
				try {
					return rs.getString(1);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return null;
			}
			
		});
	}
}
