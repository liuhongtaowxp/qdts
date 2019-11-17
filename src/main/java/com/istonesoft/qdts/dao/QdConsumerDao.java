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
public class QdConsumerDao {
	@Autowired
	protected DataSource ds;
	@Autowired
	protected QdJdbcTemplate jdbcTemplate;
	
	public String getGroupIdByMethod(String methodString) throws Exception {
		String dbGroupId = jdbcTemplate.executeQueryToEntity(ds, "select group_id from t_qd_consumer where method=? and status=? and exception=?", new Object[] {methodString, "PREPARE", "netConnectException"}, new RowHandler<String>(){

			@Override
			public String handle(ResultSet rs) {
				try {
					return rs.getString(1);
				} catch (SQLException e) {
					return null;
				}
			}
			
		});
		return dbGroupId;
	}
	
	public void insertConsumerEntity(String groupId, String methodString, String status) throws Exception {
		
		jdbcTemplate.executeSql(ds, "insert into t_qd_consumer(group_id,method,status,exception,update_time,invoke_count) values(?,?,?,?,?,?)", new Object[] {
				groupId, methodString,status,null,new Date(),0	
		});
		
	}
}
