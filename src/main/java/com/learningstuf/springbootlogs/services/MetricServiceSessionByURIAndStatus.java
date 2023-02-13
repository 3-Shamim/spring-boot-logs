package com.learningstuf.springbootlogs.services;

import com.learningstuf.springbootlogs.payloads.Pair;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Md. Shamim Molla
 * Email: shamim.molla@vivasoftltd.com
 */

@Slf4j
@Service
public class MetricServiceSessionByURIAndStatus {

    private final Map<String, Map<Integer, Pair>> metricMap;

    public MetricServiceSessionByURIAndStatus() {
        metricMap = new ConcurrentHashMap<>();
    }

    public void increaseCount(String request, int status) {

        Map<Integer, Pair> statusMap = metricMap.get(request);

        if (statusMap == null) {
            statusMap = new ConcurrentHashMap<>();
        }

        Pair pair = statusMap.get(status);

        if (pair == null) {
            pair = Pair.create();
        } else {

            if (pair.hasExpired()) {
                log.info("URL: {}, Status: {}, Count: {}", request, status, pair.getCount());
                pair.reset();
            } else {
                pair.increaseCount();
            }

        }

        statusMap.put(status, pair);
        metricMap.put(request, statusMap);
    }

    public Map<String, Map<Integer, Pair>> getFullMetric() {
        return metricMap;
    }

}