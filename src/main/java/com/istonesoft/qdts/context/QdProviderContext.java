package com.istonesoft.qdts.context;

import com.istonesoft.qdts.resource.QdNameThreadLocal;
/**
 * 提供者端环境
 * @author issuser
 *
 */
public class QdProviderContext extends QdContext {

	public static void setQdProvider() {
		QdNameThreadLocal.put("qdProvider", true);
	}
	
	public static boolean isProvider() {
		Object obj = QdNameThreadLocal.get("qdProvider");
		if (obj == null) {
			return false;
		} else {
			return true;
		}
	}
	
}
