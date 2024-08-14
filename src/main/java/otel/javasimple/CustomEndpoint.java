package otel.javasimple;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.HashMap;
import java.util.Map;

@Component
@Endpoint(id = "challenge-me")
public class CustomEndpoint {

    @ReadOperation
    public ResponseEntity<Map<String, Boolean>> isChallengingMe(@PathVariable String option) {
        Map<String, Boolean> response = new HashMap<>();

        if ("yes".equalsIgnoreCase(option)) {
            response.put("status", true);
        } else if ("no".equalsIgnoreCase(option)) {
            response.put("status", false);
        } else {
            response.put("come on!!", null);
            return ResponseEntity.badRequest().body(response);
        }

        return ResponseEntity.ok(response);
    }
}
