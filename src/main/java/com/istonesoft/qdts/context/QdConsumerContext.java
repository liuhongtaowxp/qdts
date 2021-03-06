package com.istonesoft.qdts.context;

import com.istonesoft.qdts.resource.QdGroup;
import com.istonesoft.qdts.transaction.ConsumerNoTransProcessor;
/**
 * 消费者端环境
 * @author issuser
 *
 */
public class QdConsumerContext extends QdContext {
	
	private QdGroup qdGroup;
	
	public QdConsumerContext() {
		proceedingJoinPointResultHandler = new ConsumerNoTransProcessor();
	}
	
	public QdGroup getQdGroup() {
		return qdGroup;
	}

	public void setQdGroup(QdGroup qdGroup) {
		this.qdGroup = qdGroup;
	}

	@Override
	public boolean isConsumerContxt() {
		return true;
	}

	@Override
	public boolean isProviderContxt() {
		return false;
	}

}
