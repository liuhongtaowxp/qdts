package com.istonesoft.qdts.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 提供方注解，需放在controller层
 * @author issuser
 *
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface QdTransactionProvider {
	
	public String businessName() default "businessName";
	
}
