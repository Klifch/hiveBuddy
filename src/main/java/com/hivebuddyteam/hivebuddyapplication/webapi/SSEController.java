package com.hivebuddyteam.hivebuddyapplication.webapi;

import com.hivebuddyteam.hivebuddyapplication.domain.UpdatesSubscription;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.*;

@RestController
@RequestMapping("/api/sse")
public class SSEController {

    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private Map<String, UpdatesSubscription> subscriptions = new ConcurrentHashMap<>();

    @GetMapping("/stream")
    public SseEmitter streamSseEvents(@AuthenticationPrincipal UserDetails userDetails) {
        SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);

        executorService.execute(() -> {
            try {
                for (int i = 0; i < 10; i++) {
                    Thread.sleep(1000); // simulate delay
                    sseEmitter.send("SSE MVC - " + System.currentTimeMillis() + " User: " + userDetails.getUsername());
                    System.out.println("Sending event number " + i + " to user " + userDetails.getUsername());
                }
                System.out.println("Stopped streaming events");
                sseEmitter.complete();
            } catch (Exception e) {
                sseEmitter.completeWithError(e);
            }
        });
        return sseEmitter;
    }

    @GetMapping(value = "/subscribe") //  produces = MediaType.TEXT_EVENT_STREAM_VALUE
    public SseEmitter subscribeToUpdates(
            @RequestParam("deviceSerial") String deviceSerial,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        UpdatesSubscription subscription = new UpdatesSubscription(sseEmitter, scheduler);
        String subscriptionName = String.format("%s-%s", userDetails.getUsername(), deviceSerial);

        subscriptions.put(subscriptionName, subscription);

        sseEmitter.onCompletion(() -> {
            scheduler.shutdown();
            subscriptions.remove(subscriptionName);
        });
        sseEmitter.onTimeout(() -> {
            scheduler.shutdown();
            subscriptions.remove(subscriptionName);
        });
        sseEmitter.onError((e) -> {
            scheduler.shutdown();
            subscriptions.remove(subscriptionName);
        });

        scheduler.scheduleAtFixedRate(() -> {
            try {
                sseEmitter.send("we got info from " + subscriptionName);
                System.out.println("Event sent for - " + subscriptionName);
            } catch (Exception e) {
                sseEmitter.completeWithError(e);
            }
        }, 0, 500, TimeUnit.MILLISECONDS);


        return sseEmitter;
    }




}
