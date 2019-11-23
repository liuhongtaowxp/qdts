package com.istonesoft.qdts.context;

import com.istonesoft.qdts.connection.QdServiceConnection;
import com.istonesoft.qdts.handler.ProceedingJoinPointResultHandler;
import com.istonesoft.qdts.resource.QdNameThreadLocal;
/**
 * 消费者和提供者共同使用的环境
 * @author issuser
 *
 */
public abstract class QdContext {
	//事务组ID
	protected String qdGroupId;
	//service层connection
	protected QdServiceConnection qdConnection;
	//controller层所处的事务状态
	protected State state = State.SERVICE;

	public String getQdGroupId() {
		return qdGroupId;
	}

	public void setQdGroupId(String qdGroupId) {
		this.qdGroupId = qdGroupId;
	}

	public QdServiceConnection getQdConnection() {
		return qdConnection;
	}

	public void setQdConnection(QdServiceConnection qdConnection) {
		this.qdConnection = qdConnection;
	}

	public State getState() {
		return state;
	}
	public void setState(State state) {
		this.state = state;
	}
	//是否为消费者方
	public abstract boolean isConsumerContxt();
	
	public abstract boolean isProviderContxt();
	
	public ProceedingJoinPointResultHandler getProceedingJoinPointResultHandler() {
		if (getQdConnection() != null) {
			return getQdConnection();
		} else {
			return getTransProcessor();
		}
	}
	
	public abstract ProceedingJoinPointResultHandler getTransProcessor();
	
	public void clear() {
		QdNameThreadLocal.maps.clear();
	}
	
}
