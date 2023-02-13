package com.learningstuf.springbootlogs.configs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 * User: Md. Shamim Molla
 * Email: shamim.molla@vivasoftltd.com
 */

@Slf4j
public class ApiLogger implements HandlerInterceptor {

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // for only POST request.
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String requestId = UUID.randomUUID().toString();

        if (handler instanceof HandlerMethod handlerMethod) {
            RequestMapping methodAnnotation = handlerMethod.getMethodAnnotation(RequestMapping.class);
            RequestMapping classAnnotation = handlerMethod.getBeanType().getAnnotation(RequestMapping.class);
            if (methodAnnotation != null && classAnnotation != null) {
                String[] classValues = classAnnotation.value();
                String[] methodValues = methodAnnotation.value();
                String fullPath = classValues[0] + methodValues[0];

                log.info("URI full path is: {}", fullPath);

            }
        }

        log(request, response, requestId);

        long startTime = System.currentTimeMillis();
        request.setAttribute("startTime", startTime);
        request.setAttribute("requestId", requestId);

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        long startTime = (Long) request.getAttribute("startTime");
        long endTime = System.currentTimeMillis();
        long executeTime = endTime - startTime;
        log.info("requestId {}, Handle :{} , request take time: {}", request.getAttribute("requestId"), handler, executeTime);
    }

    private void log(HttpServletRequest request, HttpServletResponse response, String requestId) {
        log.info("requestId {}, host {}  HttpMethod: {}, URI : {}", requestId, request.getHeader("host"), request.getMethod(), request.getRequestURI());
    }

}