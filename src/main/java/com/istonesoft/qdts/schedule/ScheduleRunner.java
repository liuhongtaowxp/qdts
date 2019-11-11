package com.istonesoft.qdts.schedule;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.istonesoft.qdts.jdbc.QdJdbcTemplate;
import com.istonesoft.qdts.jdbc.RowHandler;
import com.istonesoft.qdts.resource.QdMethod;

@Component
public class ScheduleRunner implements ApplicationContextAware {
	@Autowired
	private QdJdbcTemplate jdbcTemplate;
	
	@Autowired
	protected DataSource ds;
	
	private ApplicationContext applicationContext;
	
	@Scheduled(fixedDelay = 1000 * 60)
	public void invokeNetWorkExceptionData() {
		
		try {
			//扫描调用超时的数据,状态为prepare,并且没有exception信息的数据
			List<String> list = jdbcTemplate.executeQuery(ds.getConnection(), "select method from t_qd_consumer where status=? and exception is null", new Object[] {"PREPARE"}, new RowHandler<String>() {

				public String handle(ResultSet rs) {
					try {
						return rs.getString(1);
					} catch (SQLException e) {
						e.printStackTrace();
					}
					return "";
				}
				
			});
			for (String s : list) {
				//反序列化为QdMethod
				QdMethod method = QdMethod.createQdMethodUseJson(s);
				//执行QdMethod
				method.invoke(this.applicationContext);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
		
	}
	
}
