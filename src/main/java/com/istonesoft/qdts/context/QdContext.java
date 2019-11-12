package com.istonesoft.qdts.context;

import com.istonesoft.qdts.connection.QdConnection;
import com.istonesoft.qdts.resource.NameThreadLocal;

public abstract class QdContext {
	public static void setQdGroupId(String qdGroupId) {
		NameThreadLocal.put("qdGroupId", qdGroupId);
	}
	
	public static String getQdGroupId() {
		return (String)NameThreadLocal.get("qdGroupId");
	}
	
	public static void setQdConnection(QdConnection qdConnection) {
		NameThreadLocal.put("qdConnection", qdConnection);
	}
	
	public static QdConnection getQdConnection() {
		return (QdConnection)NameThreadLocal.get("qdConnection");
	}
	
	public static void clear() {
		NameThreadLocal.maps.clear();
	}
	
	public static void requiredNewConnection() {
		NameThreadLocal.put("requiredNewConnection", true);
	}
	public static void cleanRequiredNewConnection() {
		NameThreadLocal.clear("requiredNewConnection");
	}
	public static boolean isRequiredNewConnection() {
		
		Object obj = NameThreadLocal.get("requiredNewConnection");
		if (obj == null) {
			return false;
		} else {
			return true;
		}
		
	}
}
