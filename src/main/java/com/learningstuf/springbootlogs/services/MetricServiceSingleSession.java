package com.learningstuf.springbootlogs.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Md. Shamim Molla
 * Email: shamim.molla@vivasoftltd.com
 */

@Slf4j
@Service
public class MetricServiceSingleSession {

    private Map<String, Long> metricMap;
    private LocalDateTime start;

    public MetricServiceSingleSession() {
        metricMap = new ConcurrentHashMap<>();
        start = LocalDateTime.now();
    }

    public void increaseCount(String request) {

        Long count = metricMap.get(request);

        if (count == null) {
            count = 1L;
        } else {
            count += 1;
        }

        if (hasExpired()) {

            Map<String, Long> copyMetrics = new ConcurrentHashMap<>(metricMap);

            CompletableFuture.runAsync(() -> copyMetrics.forEach((reqURI, _count) -> log.warn("URI: {}, Count: {}", reqURI, _count)));

            metricMap = new ConcurrentHashMap<>();
            start = LocalDateTime.now();
            count = 1L;
        }

        metricMap.put(request, count);

    }

    public Map<String, Long> getFullMetric() {
        return metricMap;
    }

    private boolean hasExpired() {
        Duration between = Duration.between(start, LocalDateTime.now());
        return between.getSeconds() > 10;
    }

}