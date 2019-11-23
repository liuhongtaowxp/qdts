package com.istonesoft.qdts.transaction;

import java.sql.SQLException;

import javax.sql.DataSource;

import com.alibaba.fastjson.JSON;
import com.istonesoft.qdts.context.QdContextHolder;
import com.istonesoft.qdts.handler.ProceedingJoinPointResultHandler;
import com.istonesoft.qdts.jdbc.QdJdbcTemplate;
import com.istonesoft.qdts.resource.QdResult;

public class ProviderNoTransProcessor implements ProceedingJoinPointResultHandler {

	@Override
	public void successHandle(QdJdbcTemplate jdbcTemplate, DataSource ds,
			QdResult result) throws SQLException {
		try {
			jdbcTemplate.executeImmediateCommit(ds, "update t_qd_provider set status='SUCCESS',invoke_count=invoke_count+1,update_time=now(), result=? where group_id=?", new Object[] {JSON.toJSONString(result), QdContextHolder.getQdContext().getQdGroupId()});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void failHandle(QdJdbcTemplate jdbcTemplate, DataSource ds,
			QdResult result) throws SQLException {
		try {
			jdbcTemplate.executeImmediateCommit(ds, "update t_qd_provider set exception=?,invoke_count=invoke_count+1,update_time=now()  where group_id=?", new Object[] {result.getMsg(), QdContextHolder.getQdContext().getQdGroupId()});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
