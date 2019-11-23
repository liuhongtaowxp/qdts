package com.istonesoft.qdts.restTemplate.interceptor;

import java.io.IOException;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import com.istonesoft.qdts.context.QdConsumerContext;
import com.istonesoft.qdts.context.QdContextHolder;
/**
 *  请求拦截器，携带groupId
 * @author issuser
 *
 */
public class GroupClientHttpRequestInterceptor implements ClientHttpRequestInterceptor {

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
			throws IOException {
		String groupId = QdContextHolder.getQdContext().getQdGroupId();
		if (groupId != null) {
			request.getHeaders().add("qdGroupId", groupId);
		}
		ClientHttpResponse response = execution.execute(request, body);
		return response;
	}

}
