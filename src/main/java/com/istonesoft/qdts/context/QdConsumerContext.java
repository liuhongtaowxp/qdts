package com.istonesoft.qdts.context;

import com.istonesoft.qdts.resource.NameThreadLocal;
import com.istonesoft.qdts.resource.QdGroup;
/**
 * 消费者端环境
 * @author issuser
 *
 */
public class QdConsumerContext extends QdContext {

	
	public static void setQdGroup(QdGroup qdGroup) {
		NameThreadLocal.put("qdGroup", qdGroup);
	}
	
	public static QdGroup getQdGroup() {
		return (QdGroup)NameThreadLocal.get("qdGroup");
	}
	
	public static void setQdConsumer() {
		NameThreadLocal.put("qdConsumer", true);
	}
	
	public static boolean isConsumer() {
		Object obj = NameThreadLocal.get("qdConsumer");
		if (obj == null) {
			return false;
		} else {
			return true;
		}
	}
	
	
}
