package com.example.demo.logs;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;


@Aspect
@Component
public class RequestResponseLogger {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Before("within(com.example.demo.controller..*))")
    public void beforeCallAtMethod(JoinPoint jp) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();

        logger.info("Time: " + DateTimeFormatter.ofPattern("yyy-MM-dd hh:mm:ss.SSS").format(LocalDateTime.now())
                + " Method: " + request.getMethod()
                + " URI: " + request.getRequestURI()
                + "\nRequest object: \n" + Arrays.toString(jp.getArgs()));
    }

    @AfterReturning(pointcut = "within(com.example.demo.controller..*))",
            returning = "returnValue")
    public void afterCallAt(ResponseEntity<?> returnValue) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();

        logger.info("Time: " + DateTimeFormatter.ofPattern("yyy-MM-dd hh:mm:ss.SSS").format(LocalDateTime.now())
                + " Method: " + request.getMethod()
                + " URI: " + request.getRequestURI()
                + "\nResponse: \n" + returnValue.getStatusCode() + " " + returnValue.getBody());
    }

    @AfterThrowing(pointcut = "within(com.example.demo.controller..*))", throwing = "e")
    public void endpointAfterThrowing(JoinPoint p, Exception e) {
        logger.error(p.getTarget().getClass().getSimpleName() + " "
                + p.getSignature().getName() + " "
                + e.getMessage());
    }
}
