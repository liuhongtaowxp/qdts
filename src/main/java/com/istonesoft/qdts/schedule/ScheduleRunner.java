package com.istonesoft.qdts.schedule;

import java.util.List;
import java.util.Map;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.istonesoft.qdts.context.QdConsumerContext;
import com.istonesoft.qdts.context.QdContextHolder;
import com.istonesoft.qdts.dao.QdConsumerDao;
import com.istonesoft.qdts.resource.QdGroup;
import com.istonesoft.qdts.resource.QdMethod;
import com.istonesoft.qdts.resource.QdStatus;

@Component
public class ScheduleRunner implements ApplicationContextAware {
	@Autowired
	private QdConsumerDao qdConsumerDao;
	
	private ApplicationContext applicationContext;
	
	@Scheduled(fixedDelay = 1000 * 3600)
	public void invokeNetWorkExceptionData() {
		
		try {
			//扫描调用超时的数据,状态为prepare,并且没有exception信息的数据
			List<Map<String, String>> exceptionList = qdConsumerDao.getNetWorkExceptionData();
			for (Map<String, String> dataMap : exceptionList) {
				//反序列化为QdMethod
				QdMethod method = QdMethod.createQdMethodUseJson(dataMap.get("method"));
				initQdGroup(dataMap, method);
				//执行QdMethod
				method.invoke(this.applicationContext);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			QdContextHolder.getQdContext().clear();
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

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
		
	}
	
}
