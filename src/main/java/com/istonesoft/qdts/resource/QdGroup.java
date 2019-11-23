package com.istonesoft.qdts.resource;

import java.util.UUID;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import com.istonesoft.qdts.annotation.QdTransactionConsumer;
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
	//描述
	private String desc;
	
	public QdGroup() {
		super();
	}

	public QdGroup(String groupId, QdMethod method, QdStatus status, String desc) {
		super();
		this.groupId = groupId;
		this.method = method;
		this.status = status;
		this.desc = desc;
	}

	public static QdGroup newQdGroup(ProceedingJoinPoint joinPoint) {
		String groupId = UUID.randomUUID().toString().replaceAll("-","");
		QdMethod method = QdMethod.createQdMethod(joinPoint);
		return new QdGroup(groupId, method, QdStatus.PREPARE, getDesc(joinPoint));
	}

	private static String getDesc(ProceedingJoinPoint joinPoint) {
		MethodSignature methodSignature = (MethodSignature)joinPoint.getSignature();
		return methodSignature.getMethod().getAnnotation(QdTransactionConsumer.class).desc();
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

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	@Override
	public String toString() {
		return "QdGroup [groupId=" + groupId + ", method=" + method + ", status=" + status + "]";
	}
}
