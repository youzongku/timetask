package com.tomtop.timetask.config;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Order(-1)// 保证该AOP在@Transactional之前执行
@Component
public class DynamicDataSourceAspect {

	private static final Logger logger = LoggerFactory.getLogger(DynamicDataSourceAspect.class);

    @Before("@annotation(module)")
    public void changeDataSource(JoinPoint point, TargetDataSource module) throws Throwable {
        String dsId = module.name();
        if (!DynamicDataSourceContextHolder.containsDataSource(dsId)) {
            logger.error("数据源[{}]不存在，使用默认数据源 > {}", module.name(), point.getSignature());
        } else {
            logger.info("Use DataSource : {} > {}", module.name(), point.getSignature());
            DynamicDataSourceContextHolder.setDataSourceType(module.name());
        }
    }

    @After("@annotation(module)")
    public void restoreDataSource(JoinPoint point, TargetDataSource module) {
        logger.info("Revert DataSource : {} > {}", module.name(), point.getSignature());
        DynamicDataSourceContextHolder.clearDataSourceType();
    }

}
