package com.hivebuddyteam.hivebuddyapplication.domain;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.concurrent.ScheduledExecutorService;

public class UpdatesSubscription {

    private final SseEmitter emitter;
    private final ScheduledExecutorService scheduler;

    public UpdatesSubscription(SseEmitter emitter, ScheduledExecutorService scheduler) {
        this.emitter = emitter;
        this.scheduler = scheduler;
    }

    public SseEmitter getEmitter() {
        return emitter;
    }

    public ScheduledExecutorService getScheduler() {
        return scheduler;
    }
}
