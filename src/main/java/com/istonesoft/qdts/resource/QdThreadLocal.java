package com.istonesoft.qdts.resource;

import com.istonesoft.qdts.context.QdContext;

public class QdThreadLocal {

	public static final ThreadLocal<QdContext> threadObj = new ThreadLocal<QdContext>();
	
	public static void put(QdContext val) {
		QdContext nameThreadLocal = threadObj.get();
		if (nameThreadLocal == null) {
			threadObj.set(val);
		}
	}
	
	public static QdContext get() {
		return threadObj.get();
	}
	
	public static void clear() {
		threadObj.remove();
	}

}
