package com.istonesoft.qdts.aspect;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.istonesoft.qdts.context.QdContextHolder;
import com.istonesoft.qdts.context.QdProviderContext;
import com.istonesoft.qdts.dao.QdProviderDao;
import com.istonesoft.qdts.handler.ProceedingJoinPointHandler;
import com.istonesoft.qdts.resource.QdResult;
/**
 * QdTransactionProvider注解拦截，拦截的 是提供方controller层
 * @author issuser
 *
 */
@Aspect
@Component
public class QdTransactionProviderAspect extends ProceedingJoinPointHandler {
	@Autowired
	private QdProviderDao dao;
	
	@Around("@annotation(com.istonesoft.qdts.annotation.QdTransactionProvider)")
	public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
		//设置当前环境为消费者端
		QdProviderContext ctx = (QdProviderContext)QdContextHolder.getQdContext(false);
		//获取groupId
		String qdGroupId = ctx.getQdGroupId();
		//查询调用次数
		int invokeCount = dao.getInvokeCount(qdGroupId);
		QdResult result = null;
		//第一次执行
		if (invokeCount <= 0) {
			dao.insertProviderEntity(qdGroupId);
			//调用controller逻辑
			result = this.invoke(joinPoint);
		} else {//重复执行
			//获取曾经成功执行过的返回值
			String iResult = dao.getSuccessResult(qdGroupId);
			if (iResult == null) {//没有成功执行过
				//调用controller逻辑
				result = this.invoke(joinPoint);
			} else {//有成功执行过
				result = QdResult.createQdResultUseJson(iResult);
			}
		}
		ctx.clear();
		return result;
	}

}
