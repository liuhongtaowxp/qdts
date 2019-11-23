package com.istonesoft.qdts.context;

import com.istonesoft.qdts.resource.QdThreadLocal;

public class QdContextHolder {

	public static QdContext getQdContext(boolean isConsumerContext) {
		QdContext result = QdThreadLocal.get();
		if (result != null) {
			return result;
		} else {
			if (isConsumerContext) {
				QdThreadLocal.put(new QdConsumerContext());
			} else {
				QdThreadLocal.put(new QdProviderContext());
			}
			return QdThreadLocal.get();
		}
	}
	
	public static QdContext getQdContext() {
		QdContext result = QdThreadLocal.get();
		if (result != null) {
			return result;
		}
		return null;
	}
	
}
