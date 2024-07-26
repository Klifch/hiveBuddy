package com.hivebuddyteam.hivebuddyapplication.webapi;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/api/sse")
public class SSEController {

    private final ExecutorService executorService = Executors.newCachedThreadPool();

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
}
