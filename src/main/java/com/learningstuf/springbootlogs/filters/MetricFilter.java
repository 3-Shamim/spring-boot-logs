package com.learningstuf.springbootlogs.filters;

import com.learningstuf.springbootlogs.services.MetricServiceSessionByURIAndStatus;
import com.learningstuf.springbootlogs.services.MetricServiceSingleSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * Created by IntelliJ IDEA.
 * User: Md. Shamim Molla
 * Email: shamim.molla@vivasoftltd.com
 */

@AllArgsConstructor
@Component
public class MetricFilter implements Filter {

    private final MetricServiceSessionByURIAndStatus metricServiceSessionByURIAndStatus;
    private final MetricServiceSingleSession metricServiceSingleSession;

//    @Override
//    public void init(FilterConfig config) {
//        metricService = (MetricService) WebApplicationContextUtils
//                .getRequiredWebApplicationContext(config.getServletContext())
//                .getBean("metricService");
//    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = ((HttpServletRequest) request);
        String urlPattern = httpRequest.getRequestURI().replaceAll("/\\d+/", "/{id}/").replaceAll("/\\d+$", "/{id}");
        String req = httpRequest.getMethod() + " - " + urlPattern;

        chain.doFilter(request, response);

        int status = ((HttpServletResponse) response).getStatus();
        metricServiceSessionByURIAndStatus.increaseCount(req, status);
        metricServiceSingleSession.increaseCount(req);
    }

}