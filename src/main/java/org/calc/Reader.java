package org.calc;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Reader {
    private JSONObject jsonObject;

    public void read() throws IOException, URISyntaxException, InterruptedException {
        String uriString = "http://localhost:8080/v1/dataset";
        URI uri = new URI(uriString);

        HttpResponse<String> response = sendRequest(uri);
        jsonObject = new JSONObject(response.body());
    }

    private HttpResponse<String> sendRequest(URI uri) throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(uri).build();
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }
}