package com.istonesoft.qdts.resource;

import java.util.UUID;

import org.aspectj.lang.ProceedingJoinPoint;
/**
 * 事务组
 * @author issuser
 *
 */
public class QdGroup {
	//组ID
	private String groupId;
	//调用的方法
	private QdMethod method;
	//状态
	private QdStatus status;
	
	public QdGroup() {
		super();
	}

	public QdGroup(String groupId, QdMethod method, QdStatus status) {
		super();
		this.groupId = groupId;
		this.method = method;
		this.status = status;
	}

	public static QdGroup newQdGroup(ProceedingJoinPoint joinPoint) {
		String groupId = UUID.randomUUID().toString().replaceAll("-","");
		QdMethod method = QdMethod.createQdMethod(joinPoint);
		return new QdGroup(groupId, method, QdStatus.PREPARE);
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public QdMethod getMethod() {
		return method;
	}

	public void setMethod(QdMethod method) {
		this.method = method;
	}

	public QdStatus getStatus() {
		return status;
	}

	public void setStatus(QdStatus status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "QdGroup [groupId=" + groupId + ", method=" + method + ", status=" + status + "]";
	}
	
	
}
