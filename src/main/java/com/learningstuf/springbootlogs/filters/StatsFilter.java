package com.learningstuf.springbootlogs.filters;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

/**
 * Created by IntelliJ IDEA.
 * User: Md. Shamim Molla
 * Email: shamim.molla@vivasoftltd.com
 */

@Slf4j
@Component
public class StatsFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {
        // empty
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {

        log.info(
                "URI: {} is called. [URL: {}]",
                ((HttpServletRequest) req).getRequestURI(),
                ((HttpServletRequest) req).getRequestURL()
        );

        Instant start = Instant.now();
        try {
            chain.doFilter(req, resp);
        } finally {
            Instant finish = Instant.now();
            long time = Duration.between(start, finish).toMillis();
            log.info("{}: {} ms ", ((HttpServletRequest) req).getRequestURI(), time);
        }
    }

    @Override
    public void destroy() {
        // empty
    }

}
