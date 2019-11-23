package com.istonesoft.qdts.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

import com.istonesoft.qdts.config.QdConfig;
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Import(QdConfig.class)
/**
 * 开启功能注解
 *
 */
public @interface EnableQdts {

}
