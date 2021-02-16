package com.test.aop;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import stcore.commons.traffic.ApiResult;

@Aspect
@Component
public class CarAspect {

    @Pointcut("execution(* com.test.*.view())")
    public void printPointCutBeforeExample() {}

    @Before("execution(* com.test.*.view())")
    public void printBeforeWithMethodExample(JoinPoint joinPoint) {
        System.out.println("I'm a Car Aspect Before Example showing method name --> " + joinPoint.toString());
    }

    @Pointcut("execution(* com.test.*.deleteCar(..))")
    public void parameterDisplayer() {}

    @Before("parameterDisplayer()")
    public void printBeforeWithParameterExample(JoinPoint joinPoint){
        Object[] parameters = joinPoint.getArgs();
        for (Object o : parameters) {
            System.out.println("I found a parameter! --> " + o.toString());
        }
    }

    @Before("printPointCutBeforeExample()")
    public void printBeforeWithPointCutExample() {
        System.out.println("I'm a Car Aspect WITH POINT CUT Before Example");
    }

    @After("printPointCutBeforeExample()")
    public void printAfterPointCutExample() {
        System.out.println("I'm a Car Aspect WITH POINT CUT After Example");
    }

    @After("execution(* com.test.*.view())")
    public void printAfterExample() {
        System.out.println("I'm a Car Aspect After Example");
    }

    @AfterReturning(pointcut = "parameterDisplayer()", returning = "retVal")
    public void checkReturnValue(Object retVal) {
        System.out.println("Got a returned value of: " + retVal.toString());
    }

    @Around("parameterDisplayer()")
    public Object moddingParameter(ProceedingJoinPoint proceedingJoinPoint) {
        Object value = null;
        try {
            value = proceedingJoinPoint.proceed();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        System.out.println("Received " + value + ". Changing to false");
        return Mono.just(ApiResult.ok(false));
    }

    @Pointcut(value = "@annotation(org.springframework.web.bind.annotation.GetMapping)")
    public void getWasCalled() {

    }

    @Before("getWasCalled()")
    public void beforeGetWasCalled() {
        System.out.println("A get mapping API was called!!!!");
    }

}
