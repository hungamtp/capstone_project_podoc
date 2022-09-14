package com.capstone.pod.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.context.annotation.Configuration;

@Aspect
@Configuration
@Slf4j
public class PaymentAspect {

    @Before("execution(* com.capstone.pod.controller.*.*(..))")
    public void before(JoinPoint joinPoint){
        log.info(" before called " + joinPoint.toString());
    }

    @Around("execution(* com.capstone.pod.controller.*.*(..))")
    public void around(JoinPoint joinPoint){
        log.info(" before called " + joinPoint.toString());
    }


    // run when the method is done successfully
    @AfterReturning("execution(* com.capstone.pod.controller.*.*(..))")
    public void afterReturning(JoinPoint joinPoint){
        log.info(" before called " + joinPoint.toString());
    }

    // run when method case exception
    @AfterThrowing("execution(* com.capstone.pod.controller.*.*(..))")
    public void afterThrowing(JoinPoint joinPoint){
        log.info(" before called " + joinPoint.toString());
    }
}
