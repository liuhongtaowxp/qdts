package com.istonesoft.qdts.context;

import com.istonesoft.qdts.resource.QdNameThreadLocal;
import com.istonesoft.qdts.resource.QdGroup;
/**
 * 消费者端环境
 * @author issuser
 *
 */
public class QdConsumerContext extends QdContext {

	public static void setQdGroup(QdGroup qdGroup) {
		QdNameThreadLocal.put("qdGroup", qdGroup);
	}
	
	public static QdGroup getQdGroup() {
		return (QdGroup)QdNameThreadLocal.get("qdGroup");
	}
	
	public static void setQdConsumer() {
		QdNameThreadLocal.put("qdConsumer", true);
	}
	
	public static boolean isConsumer() {
		Object obj = QdNameThreadLocal.get("qdConsumer");
		if (obj == null) {
			return false;
		} else {
			return true;
		}
	}
	
}
