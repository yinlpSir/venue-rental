package com.example.fieldrental.handler;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
public class AspectJ {

    @Pointcut("execution(* com.example.fieldrental.service.*.*(..))")
    public void FieldPointCut()
    {

    }

    @Before("FieldPointCut()")
    public void before(JoinPoint joinPoint)
    {
        log.info("Before+++",joinPoint.getSignature().getName());
    }

    @AfterReturning("FieldPointCut()")
    public void afterReturning(JoinPoint joinPoint)
    {
        log.info("afterReturning+++"+joinPoint.getSignature().getName());
    }

    @Around("FieldPointCut()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {


        Object proceed = proceedingJoinPoint.proceed();
        if (proceed == null)
            proceed = "";
        log.info("Around+++ " + proceed);
        return proceed;
    }

    @After("FieldPointCut()")
    public void after(JoinPoint joinPoint)
    {
        log.info("the end +++"+joinPoint.getSignature().getName());
    }

    @AfterThrowing(value = "FieldPointCut()",throwing = "e")
    public void afterThrowing(Exception e)
    {
        log.error(e.toString());
    }
}
