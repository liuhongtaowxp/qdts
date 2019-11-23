package com.istonesoft.qdts.context;

import com.istonesoft.qdts.handler.ProceedingJoinPointResultHandler;
import com.istonesoft.qdts.transaction.ProviderNoTransProcessor;

/**
 * 提供者端环境
 * @author issuser
 *
 */
public class QdProviderContext extends QdContext {

	@Override
	public boolean isConsumerContxt() {
		return false;
	}

	@Override
	public boolean isProviderContxt() {
		return true;
	}
	
	@Override
	public ProceedingJoinPointResultHandler getTransProcessor() {
		return new ProviderNoTransProcessor();
	}
}
