package com.istonesoft.qdts.config;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.istonesoft.qdts.context.QdProviderContext;
import com.istonesoft.qdts.restTemplate.interceptor.GroupClientHttpRequestInterceptor;
/**
 * 配置
 * @author issuser
 *
 */
@Configuration
public class QdConfig {
	
	/**
	 * 必须使用的RestTemplate
	 * @return
	 */
	@Bean
	public RestTemplate getRestTemplate() {
		SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();  
		requestFactory.setConnectTimeout(3000);// 设置超时  
		requestFactory.setReadTimeout(3000);  
		RestTemplate restTemplate = new RestTemplate(requestFactory);
		//header携带groupId
		restTemplate.getInterceptors().add(new GroupClientHttpRequestInterceptor());
		return restTemplate;
	}

	@Bean
	public Filter filter() {
		return new Filter() {
			@Override
			public void init(javax.servlet.FilterConfig filterConfig) throws ServletException {
			}

			@Override
			public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) {
				
				if (request instanceof HttpServletRequest) {
					//提供方取得携带的 groupId
					String qdGroupId = ((HttpServletRequest) request).getHeader("qdGroupId");
					if (qdGroupId != null) {
						//设置消费者端groupId
						QdProviderContext.setQdGroupId(qdGroupId);
					}
				}
				try {
					chain.doFilter(request, response);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ServletException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void destroy() {
			}
		};
	}
	
}
