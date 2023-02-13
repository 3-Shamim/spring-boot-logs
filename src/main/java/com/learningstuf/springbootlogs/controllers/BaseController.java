package com.learningstuf.springbootlogs.controllers;

import brave.Span;
import brave.Tracer;
import com.learningstuf.springbootlogs.services.MetricServiceSessionByURIAndStatus;
import com.learningstuf.springbootlogs.services.MetricServiceSingleSession;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * Created by IntelliJ IDEA.
 * User: Md. Shamim Molla
 * Email: shamim.molla@vivasoftltd.com
 */

@Slf4j
@AllArgsConstructor
@Controller
@RequestMapping(value = "/api/1.0.0")
public class BaseController {

    private final RequestMappingHandlerMapping requestMappingHandlerMapping;
    private final MetricServiceSessionByURIAndStatus metricServiceSessionByURIAndStatus;
    private final MetricServiceSingleSession metricServiceSingleSession;

    private final Tracer tracer;

    @GetMapping(value = "")
    public ResponseEntity<?> base() {
        return ResponseEntity.status(HttpStatus.OK).body("Api Version 1.0.0, Base URL");
    }

    @GetMapping(value = "/message")
    public ResponseEntity<?> baseMessage() {
        return ResponseEntity.status(HttpStatus.OK).body("Api Version 1.0.0, Message URL");
    }

    @GetMapping(value = "/path/{value}")
    public ResponseEntity<?> pathVariable(@PathVariable(value = "value") long value) {
        return ResponseEntity.status(HttpStatus.OK).body("Api Version 1.0.0, Base URL With Path Variable: " + value);
    }

    @GetMapping(value = "/path/{value}/path/{value1}")
    public ResponseEntity<?> pathVariable2(@PathVariable(value = "value") long value, @PathVariable(value = "value1") long value1) {
        return ResponseEntity.status(HttpStatus.OK).body("Api Version 1.0.0, Base URL With Path Variable: " + value + ", Path Variable1: " + value1);
    }

    @GetMapping(value = "/endpoints")
    public ResponseEntity<?> endpoints() {

        requestMappingHandlerMapping.getHandlerMethods().forEach((k, v) -> log.info("{} {}", k, v));

        return ResponseEntity.status(HttpStatus.OK).body("All end point print in the console.");
    }

    @GetMapping(value = "/metrics")
    public ResponseEntity<?> metrics() {

        return ResponseEntity.status(HttpStatus.OK).body(metricServiceSessionByURIAndStatus.getFullMetric());
    }

    @GetMapping(value = "/metrics-single")
    public ResponseEntity<?> metricsSingle() {

        return ResponseEntity.status(HttpStatus.OK).body(metricServiceSingleSession.getFullMetric());
    }

    @GetMapping(value = "/current-trace-id")
    public ResponseEntity<?> currentTraceId() {

        Span span = tracer.currentSpan();
        if (span != null) {
            log.info("Current tracer--1: {}, {}", span.context().traceIdString(), span.context().traceIdString());
        } else {
            log.info("Current tracer--1 not found");
        }

        Span span1 = tracer.currentSpan();

        if (span1 != null) {
            log.info("Current tracer--2: {}, {}", span1.context().traceIdString(), span1.context().traceIdString());
            return ResponseEntity.status(HttpStatus.OK).body(
                    String.format("Tracer %s, %s", span1.context().traceIdString(), span1.context().traceIdString())
            );
        } else {
            log.info("Current tracer--2 not found");
            return ResponseEntity.status(HttpStatus.OK).body("Look at the console log.");
        }

    }

}
