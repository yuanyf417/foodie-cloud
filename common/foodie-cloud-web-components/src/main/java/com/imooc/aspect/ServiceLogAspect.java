package com.imooc.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 *  foodie-dev
 *  日志AOP
 * @author: YYF
 * @create: 2019-12-21 17:07
 **/
@Aspect
@Component
public class ServiceLogAspect {

    /**
     * AOP 通知：
     * 1. 前置通知 执行前
     * 2. 后置通知 正常执行后
     * 3. 环绕通知 执行前后
     * 4. 异常通知 异常后
     * 5. 最终通知 执行后
     */

    public static final Logger LOGGER = LoggerFactory.getLogger( ServiceLogAspect.class );

    /**
     * 第一处 * 号 方法返回类型
     * 第二处 包名 aop监控类 所在包
     * 第三处 。。 包以及其子包下的所有方法
     * 第四处 * 代表类名 ， * 代表所有类
     * 第五处 *（..）* 代表类中的方法名 ，（..） 表示方法中的任何参数
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around( "execution(* com.imooc..*.service.impl..*.*(..))" )
    public Object recordTimeLog(ProceedingJoinPoint joinPoint) throws Throwable {
        LOGGER.info( "========= 开始执行  {},[] =========",joinPoint.getTarget().getClass(),joinPoint.getSignature().getName() );

        long begin = System.currentTimeMillis();

        Object result = joinPoint.proceed();

        long end = System.currentTimeMillis();
        long takeTime = end - begin ;
        if(takeTime >3000){
            LOGGER.error( "========= 执行结束，耗时：{}毫秒 =========" ,takeTime );
        }else if(takeTime >2000){
            LOGGER.warn( "========= 执行结束，耗时：{}毫秒 =========" ,takeTime );
        }else{
            LOGGER.info( "========= 执行结束，耗时：{}毫秒 =========" ,takeTime );
        }
        return result;
    }

    public static void main(String[] args) {
        String f = "abc.doc.docx";

//        int a = f.lastIndexOf( "." );
//
//        System.out.println( "f.substring( 0, a ) = " + f.substring( 0, a ) );



    }
}
