package com.istonesoft.qdts.context;

import com.istonesoft.qdts.connection.QdConnection;
import com.istonesoft.qdts.resource.QdNameThreadLocal;
/**
 * 消费者和提供者共同使用的环境
 * @author issuser
 *
 */
public abstract class QdContext {
	
	public static void setQdGroupId(String qdGroupId) {
		QdNameThreadLocal.put("qdGroupId", qdGroupId);
	}
	
	public static String getQdGroupId() {
		return (String)QdNameThreadLocal.get("qdGroupId");
	}
	
	public static void setQdConnection(QdConnection qdConnection) {
		QdNameThreadLocal.put("qdConnection", qdConnection);
	}
	
	public static QdConnection getQdConnection() {
		return (QdConnection)QdNameThreadLocal.get("qdConnection");
	}
	
	public static void clear() {
		QdNameThreadLocal.maps.clear();
	}
	
	public static void requiredNewConnection() {
		QdNameThreadLocal.put("requiredNewConnection", true);
	}
	
	public static void cleanRequiredNewConnection() {
		QdNameThreadLocal.clear("requiredNewConnection");
	}
	
	public static boolean isRequiredNewConnection() {
		
		Object obj = QdNameThreadLocal.get("requiredNewConnection");
		if (obj == null) {
			return false;
		} else {
			return true;
		}
		
	}
	
}
