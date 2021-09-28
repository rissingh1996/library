package com.rishabh.librarymanagement.annotations;

import com.rishabh.librarymanagement.utils.CustomThreadLocal;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class AuthorizationAspect {

    @Autowired
    private CustomThreadLocal customThreadLocal;

    @Around("@annotation(Authorization)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        String role = String.valueOf(customThreadLocal.getCustomThreadLocal().get().get("role"));
        Authorization authorization = ((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotation(Authorization.class);
        String[] allowedRoles = authorization.value();
        if(!Arrays.stream(allowedRoles).anyMatch(ar-> ar.equals(role)))
            throw new RuntimeException("Forbidden!!!");
        return joinPoint.proceed();
    }
}
