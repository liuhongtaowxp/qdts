package com.istonesoft.qdts.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.istonesoft.qdts.context.QdContextHolder;
import com.istonesoft.qdts.jdbc.QdJdbcTemplate;
import com.istonesoft.qdts.jdbc.RowHandler;
@Component
public class QdConsumerDao {
	@Autowired
	protected DataSource ds;
	@Autowired
	protected QdJdbcTemplate jdbcTemplate;
	
	public String getGroupIdByMethod(String methodString) throws Exception {
		String dbGroupId = jdbcTemplate.selectOne(ds, "select group_id from t_qd_consumer where method=? and status=? and (exception=? or exception is null)", new Object[] {methodString, "PREPARE", "netConnectException"}, new RowHandler<String>(){

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
	
	public void insertConsumerEntity(String groupId, String methodString, String status, String desc) throws Exception {
		
		jdbcTemplate.executeImmediateCommit(ds, "insert into t_qd_consumer(group_id,method,status,exception,update_time,invoke_count,business_desc) values(?,?,?,?,?,?,?)", new Object[] {
				groupId, methodString,status,null,new Date(),0,desc	
		});
		
	}
	/**
	 * 得到消费端调用时发生网络异常的数据
	 * @return
	 * @throws Exception
	 * @throws SQLException
	 */
	public List<Map<String, String>> getNetWorkExceptionData() throws Exception, SQLException {
		List<Map<String, String>> list = jdbcTemplate.selectList(ds, "select group_id, method from t_qd_consumer where status=? and (exception='netConnectException' or exception is null)", new Object[] {"PREPARE"}, new RowHandler<Map<String, String>>() {

			public Map<String, String> handle(ResultSet rs) {
				Map<String, String> map = new HashMap<String, String>();
				
				try {
					map.put("groupId", rs.getString(1));
					map.put("method", rs.getString(2));
					return map;
				} catch (SQLException e) {
					QdContextHolder.getQdContext().clear();
					e.printStackTrace();
				}
				return null;
			}
			
		});
		return list;
	}
}
