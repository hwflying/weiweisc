package com.weiweisc.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class TimeAspect {

	private final static Logger logger = LoggerFactory.getLogger(TimeAspect.class);
	
	@Around("execution(* tv.love.controller.*.*(..))")
	public Object around(ProceedingJoinPoint joinPoint) throws Throwable{
		long start = System.currentTimeMillis();
		String methodName = joinPoint.getSignature().getName();
		Object  result = joinPoint.proceed();
		long end = System.currentTimeMillis();
		String msg = "调用 " + methodName + "\t耗时 : " + (end - start) + " 毫秒!";
		if("exception".equals(methodName)){
			logger.error(msg);
		}else{
			logger.debug(msg);
		}
		return result;
	}
	
}
