package com.hivebuddyteam.hivebuddyapplication.component;

import com.hivebuddyteam.hivebuddyapplication.domain.Device;
import com.hivebuddyteam.hivebuddyapplication.domain.SensorData;
import com.hivebuddyteam.hivebuddyapplication.service.DeviceService;
import com.hivebuddyteam.hivebuddyapplication.service.SensorDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Random;

@Component
public class DataInitializer implements CommandLineRunner {

    private final SensorDataService sensorDataService;
    private final DeviceService deviceService;
    private final Logger logger = LoggerFactory.getLogger(DataInitializer.class);


    public DataInitializer(SensorDataService sensorDataService, DeviceService deviceService) {
        this.sensorDataService = sensorDataService;
        this.deviceService = deviceService;
    }

    private BigDecimal generateRandomBigDecimal(Random random, int min, int max) {
        double value = min + (max - min) * random.nextDouble();
        return BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public void run(String... args) throws Exception {
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime startTime = currentTime.minusMinutes(30);
        Random random = new Random();
        Device device = deviceService.findById(3);

        logger.info("FILLING THE DATABASE, BITCH");

        while (startTime.isBefore(currentTime)) {
            SensorData sensorData = new SensorData(
                    device,
                    startTime,
                    generateRandomBigDecimal(random, 10, 100),
                    generateRandomBigDecimal(random, 10, 100),
                    generateRandomBigDecimal(random, 10, 100),
                    generateRandomBigDecimal(random, 10, 100),
                    generateRandomBigDecimal(random, 10, 100)
            );

            sensorDataService.save(sensorData);
            startTime = startTime.plusSeconds(30);
        }

    }
}
