package com.istonesoft.qdts.schedule;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.istonesoft.qdts.context.QdConsumerContext;
import com.istonesoft.qdts.context.QdContextHolder;
import com.istonesoft.qdts.jdbc.QdJdbcTemplate;
import com.istonesoft.qdts.jdbc.RowHandler;
import com.istonesoft.qdts.resource.QdGroup;
import com.istonesoft.qdts.resource.QdMethod;
import com.istonesoft.qdts.resource.QdStatus;

@Component
public class ScheduleRunner implements ApplicationContextAware {
	@Autowired
	private QdJdbcTemplate jdbcTemplate;
	
	@Autowired
	protected DataSource ds;
	
	private ApplicationContext applicationContext;
	
	@Scheduled(fixedDelay = 1000 * 3600)
	public void invokeNetWorkExceptionData() {
		try {
			//扫描调用超时的数据,状态为prepare,并且没有exception信息的数据
			List<Map<String, String>> exceptionList = getNetWorkExceptionData();
			for (Map<String, String> dataMap : exceptionList) {
				//反序列化为QdMethod
				QdMethod method = QdMethod.createQdMethodUseJson(dataMap.get("method"));
				initQdGroup(dataMap, method);
				//执行QdMethod
				method.invoke(this.applicationContext);
			}
		} catch (Exception e) {
			QdContextHolder.getQdContext().clear();
			e.printStackTrace();
		}
		
	}
	/**
	 * 设置当前环境的QdGroup
	 * @param dataMap
	 * @param method
	 */
	private void initQdGroup(Map<String, String> dataMap, QdMethod method) {
		QdGroup group = new QdGroup();
		group.setGroupId(dataMap.get("groupId"));
		group.setMethod(method);
		group.setStatus(QdStatus.PREPARE);
		((QdConsumerContext)QdContextHolder.getQdContext(true)).setQdGroup(group);
	}
	/**
	 * 得到消费端调用时发生网络异常的数据
	 * @return
	 * @throws Exception
	 * @throws SQLException
	 */
	private List<Map<String, String>> getNetWorkExceptionData() throws Exception, SQLException {
		List<Map<String, String>> list = jdbcTemplate.selectList(ds, "select group_id, method from t_qd_consumer where status=? and exception='netConnectException'", new Object[] {"PREPARE"}, new RowHandler<Map<String, String>>() {

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

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
		
	}
	
}
