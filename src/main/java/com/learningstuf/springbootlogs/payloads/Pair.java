package com.learningstuf.springbootlogs.payloads;

import lombok.ToString;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Created by IntelliJ IDEA.
 * User: Md. Shamim Molla
 * Email: shamim.molla@vivasoftltd.com
 */

@ToString
public class Pair {

    private long count;
    private LocalDateTime startTime;

    private Pair(long count, LocalDateTime startTime) {
        this.count = count;
        this.startTime = startTime;
    }

    public static Pair create() {
        return new Pair(1, LocalDateTime.now());
    }

    public void increaseCount() {
        this.count += 1;
    }

    public void reset() {
        this.count = 1;
        this.startTime = LocalDateTime.now();
    }

    public boolean hasExpired() {
        Duration duration = Duration.between(this.startTime, LocalDateTime.now());
        return duration.getSeconds() > 300;
    }

    public long getCount() {
        return count;
    }

}
