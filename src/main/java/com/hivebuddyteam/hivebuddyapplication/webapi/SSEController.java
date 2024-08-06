package com.hivebuddyteam.hivebuddyapplication.webapi;

import com.hivebuddyteam.hivebuddyapplication.domain.SensorData;
import com.hivebuddyteam.hivebuddyapplication.domain.UpdatesSubscription;
import com.hivebuddyteam.hivebuddyapplication.dto.SensorDataDto;
import com.hivebuddyteam.hivebuddyapplication.service.DeviceService;
import com.hivebuddyteam.hivebuddyapplication.service.SensorDataService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.*;

@RestController
@RequestMapping("/api/sse")
public class SSEController {

    private final SensorDataService sensorDataService;
    private final DeviceService deviceService;
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private Map<String, UpdatesSubscription> subscriptions = new ConcurrentHashMap<>();

    public SSEController(
            SensorDataService sensorDataService,
            DeviceService deviceService
    ) {
        this.sensorDataService = sensorDataService;
        this.deviceService = deviceService;
    }

//    @GetMapping("/stream")
//    public SseEmitter streamSseEvents(@AuthenticationPrincipal UserDetails userDetails) {
//        SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);
//
//        executorService.execute(() -> {
//            try {
//                for (int i = 0; i < 10; i++) {
//                    Thread.sleep(1000); // simulate delay
//                    sseEmitter.send("SSE MVC - " + System.currentTimeMillis() + " User: " + userDetails.getUsername());
//                    System.out.println("Sending event number " + i + " to user " + userDetails.getUsername());
//                }
//                System.out.println("Stopped streaming events");
//                sseEmitter.complete();
//            } catch (Exception e) {
//                sseEmitter.completeWithError(e);
//            }
//        });
//        return sseEmitter;
//    }

    @GetMapping(value = "/subscribe") //  produces = MediaType.TEXT_EVENT_STREAM_VALUE
    public SseEmitter subscribeToUpdates(
            @RequestParam("deviceSerial") String deviceSerial,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        UpdatesSubscription subscription = new UpdatesSubscription(sseEmitter, scheduler);
        System.out.println("Subscription created!");
        String subscriptionName = String.format("%s-%s", userDetails.getUsername(), deviceSerial);

        subscriptions.put(subscriptionName, subscription);

        sseEmitter.onCompletion(() -> {
            scheduler.shutdownNow();
            subscriptions.remove(subscriptionName);
        });
        sseEmitter.onTimeout(() -> {
            scheduler.shutdownNow();
            subscriptions.remove(subscriptionName);
        });
        sseEmitter.onError((e) -> {
            scheduler.shutdownNow();
            subscriptions.remove(subscriptionName);
        });

        scheduler.scheduleAtFixedRate(() -> {
            try {
                SensorData sensorData = sensorDataService.findLatestByDevice(deviceService.findBySerial(deviceSerial));
                SensorDataDto sensorDataDto = SensorDataDto.mapToDto(deviceSerial, sensorData);
                sseEmitter.send(sensorDataDto);
                System.out.println(String.format("Number of subs: %d, event sent to %s", subscriptions.size(), subscriptionName));
            } catch (Exception e) {
                System.out.println("Error sending info for - " + subscriptionName);
                sseEmitter.completeWithError(e);
            }
            System.out.println("Scheduler still alive");
        }, 5000, 1000, TimeUnit.MILLISECONDS);


        return sseEmitter;
    }

    // It's a test, should be POST
    // Should have tests :
    //      - does subscription exist?
    //      - can we shut down?
    //      - add try catch;
    //      - check for open threads and make cleaning if needed;

    @GetMapping("/unsubscribe")
    public ResponseEntity<String> unsubscribe (
            @RequestParam("deviceSerial") String deviceSerial,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        System.out.println("Unsubscribing " + userDetails.getUsername());
        String subscriptionName = String.format("%s-%s", userDetails.getUsername(), deviceSerial);
        System.out.println("Unsubscribing " + subscriptionName);
        UpdatesSubscription subscription = subscriptions.get(subscriptionName);

        subscription.getScheduler().shutdownNow();
        System.out.println("Scheduler shut down");
        subscription.getEmitter().complete();
        System.out.println("Emitter completed");

        subscriptions.remove(subscriptionName);

        System.out.println(subscriptions.size());
        return ResponseEntity.status(HttpStatus.OK).body("Done!");
    }




}
