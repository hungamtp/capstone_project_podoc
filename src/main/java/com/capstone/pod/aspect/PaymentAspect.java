package com.capstone.pod.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.context.annotation.Configuration;

@Aspect
@Configuration
@Slf4j
public class PaymentAspect {

    @Before("execution(* com.capstone.pod.controller.*.*(..))")
    public void before(JoinPoint joinPoint) {
//        log.info("Before called " + joinPoint.toString());
    }

    @After("execution(* com.capstone.pod.controller.*.*(..))")
    public void after(JoinPoint joinPoint) {
//        log.info("After called " + joinPoint.toString());
    }

    @Around("execution(* com.capstone.pod.controller.*.*(..))")
    public void around(ProceedingJoinPoint joinPoint) throws Throwable {
//        log.info(" before called " + joinPoint.toString());
//        joinPoint.proceed(); // above code will be executed like @Before and below like @After
//        log.info(" after called in around" + joinPoint.toString());
    }


    // run when the method is done successfully
    @AfterReturning("execution(* com.capstone.pod.controller.*.*(..))")
    public void afterReturning(JoinPoint joinPoint) {
//        log.info("After return " + joinPoint.toString());
    }

    // run when method case exception
    @AfterThrowing("execution(* com.capstone.pod.controller.*.*(..))")
    public void afterThrowing(JoinPoint joinPoint) {
//        log.info("After Throwing " + joinPoint.toString());
    }
}
