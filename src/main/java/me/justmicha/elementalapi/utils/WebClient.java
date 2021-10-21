package me.justmicha.elementalapi.utils;

import lombok.SneakyThrows;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class WebClient {

    @SneakyThrows
    public static JSONObject get(String url, String... headers) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(URI.create(url)).GET().headers(headers).build();
        return new JSONObject(client.send(request, HttpResponse.BodyHandlers.ofString()).body());
    }
}
