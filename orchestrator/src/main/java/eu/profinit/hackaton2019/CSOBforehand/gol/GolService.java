package eu.profinit.hackaton2019.CSOBforehand.gol;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class GolService {

    private static final String TEAM = "Team6";
    private static final String URL = "https://34.90.168.64";

    private final RestTemplate rt;

    public void clearHistory() {
        ResponseEntity<Void> resp = rt.postForEntity(URL + "/clearHistory/" + TEAM, new HttpEntity<>(null, getHeaders()), Void.class);

        checkError(resp);
    }

    public void send(Request request) {
        request.setClientID(TEAM);
        ResponseEntity<Map> resp = rt.postForEntity(URL + "/state", new HttpEntity<>(request, getHeaders()), Map.class);

        if (resp.getStatusCodeValue() == 200) {
            return;
        }

        Map<String, Object> res = (Map<String, Object>) resp.getBody();

        log.error("error " + res.toString());

        throw new RuntimeException("error " + res.toString());
    }

    private void checkError(ResponseEntity<?> resp) {
        if (resp.getStatusCodeValue() >= 300) {
            throw new RuntimeException("error when clear history " + resp.toString());
        }
        if (resp.getStatusCodeValue() != 200) {
            log.error("Response is not ok {}", resp.getStatusCodeValue());
        }
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Accept", "*/*");
        return headers;
    }
}
