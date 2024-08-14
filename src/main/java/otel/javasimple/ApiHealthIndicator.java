package otel.javasimple;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
public class ApiHealthIndicator implements HealthIndicator {

    private final RestTemplate restTemplate;

    public ApiHealthIndicator(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Health health() {
        try {
            // Blocking call to the remote API
            ResponseEntity<String> response= restTemplate.getForEntity("http://localhost:8080/actuator/challenge-me?option=yes", String.class);
            System.out.println(response.getBody());
            Map<String, String> map = new HashMap<>();
            map.put("endpoint","http://localhost:8080/actuator/challenge-me?option=yes");
            map.put("body", response.getBody());
            map.put("status", response.getStatusCode().toString());
            map.putAll(response.getHeaders().toSingleValueMap());
            map.put("hasBody", String.valueOf(response.hasBody()));
            return Health.up().withDetails(map).build();

        } catch (Exception e) {
            return Health.down().withDetail("error", e.getMessage()).build();
        }
    }
}
