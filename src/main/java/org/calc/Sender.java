package org.calc;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Sender {

    private static final String URI_STRING = "http://localhost:8080/v1/result";
    private static final String CONTENT_TYPE = "application/json";

    public void processTransmition(Map<String, Long> executionList) throws IOException, URISyntaxException, InterruptedException {
        URI uri = new URI(URI_STRING);
        List<Map<String, Object>> results = createJsonObjects(executionList);

        String jsonData = convertToJson(results);
        System.out.println(jsonData);

        sendRequest(uri, jsonData);
    }

    private List<Map<String, Object>> createJsonObjects(Map<String, Long> executionList) {
        List<Map<String, Object>> jsonObjects = new ArrayList<>();

        executionList.forEach((workLoadID, timeDifference) -> {
            Map<String, Object> jsonObject = new HashMap<>();
            jsonObject.put("customerId", workLoadID);
            jsonObject.put("consumption", timeDifference);
            jsonObjects.add(jsonObject);
        });

        return jsonObjects;
    }

    private String convertToJson(List<Map<String, Object>> jsonObjects) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonData = objectMapper.writeValueAsString(jsonObjects);
        return "{\"result\":" + jsonData + "}";
    }

    private void sendRequest(URI uri, String jsonData) throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(uri)
                .header("Content-Type", CONTENT_TYPE)
                .POST(HttpRequest.BodyPublishers.ofString(jsonData))
                .build();

        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        System.out.println(response);
    }
}