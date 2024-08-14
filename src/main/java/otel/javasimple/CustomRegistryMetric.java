package otel.javasimple;

import io.micrometer.core.instrument.*;
import io.prometheus.metrics.core.metrics.Summary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
public class CustomRegistryMetric {

    private static final Logger logger = LoggerFactory.getLogger(CustomRegistryMetric.class);
    private final Counter requestCounter;
    private final Timer timer;
    private final Gauge activeUsersGauge;
    private final DistributionSummary fileSizeSummary;


    public CustomRegistryMetric(MeterRegistry meterRegistry) {
        this.timer =meterRegistry.timer("do.sleep.method", "operation", "sleep", "unit", "seconds", "method", "GET");

        this.requestCounter=meterRegistry.counter("counter", "method", "GET");


        this.activeUsersGauge = Gauge.builder("do.sleep.method", this, CustomRegistryMetric::getActiveUserCount)
                .register(meterRegistry);

        this.fileSizeSummary = meterRegistry.summary("response.time");

    }

    private double getActiveUserCount() {
        return Math.random();
    }

    public void uploadFile() {
        this.fileSizeSummary.record(Double.MAX_EXPONENT);
    }


    @GetMapping("/sleep")
    public long doSleep(@RequestParam long time) {
        logger.info("Sleeping for {} seconds...", time);

        timer.record(() -> {
            try {
                TimeUnit.SECONDS.sleep(time); // Sleep for the specified time
            } catch (InterruptedException e) {

                logger.error("Sleep interrupted: {}", e.getMessage());
                Thread.currentThread().interrupt(); // Preserve interrupt status
            }

            logger.info("Awake now!");
        });

        requestCounter.increment();

        uploadFile();

        return time;
    }
}