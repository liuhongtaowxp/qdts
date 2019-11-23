package com.istonesoft.qdts.context;

import com.istonesoft.qdts.resource.QdNameThreadLocal;

public class QdContextHolder {

	public static QdContext getQdContext(boolean isConsumerContext) {
		Object result = QdNameThreadLocal.get("context");
		if (result != null) {
			return (QdContext)result;
		} else {
			if (isConsumerContext) {
				QdNameThreadLocal.put("context", new QdConsumerContext());
			} else {
				QdNameThreadLocal.put("context", new QdProviderContext());
			}
			return (QdContext)QdNameThreadLocal.get("context");
		}
	}
	
	public static QdContext getQdContext() {
		Object result = QdNameThreadLocal.get("context");
		if (result != null) {
			return (QdContext)result;
		}
		return null;
	}
	
}
