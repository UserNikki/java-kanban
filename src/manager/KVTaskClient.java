package manager;

import com.google.gson.*;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;


public class KVTaskClient {

    private Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new HttpTaskServer.LocalDateTimeAdapter())
            .serializeNulls()
            .create();
    public String token;

    public KVTaskClient() {
        generateToken();
    }
    public String generateToken() {
        String body = null;
        URI uri = URI.create("http://localhost:8078/register");
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .GET()
                    .build();
            HttpClient client = HttpClient.newHttpClient();
            HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
            HttpResponse<String> response = client.send(request, handler);
            if (response.statusCode() == 200) {
                body = response.body();
            }
        } catch (IOException | InterruptedException | NullPointerException e) {
            System.out.println("Во время выполнения запроса ресурса по URL-адресу: '" + uri + "', возникла ошибка.");
        }
        return this.token = body;
    }

    public void putData(String key, String value) {

        URI uri = URI.create("http://localhost:8078/save/" + key + "?API_TOKEN=" + getToken());
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .POST(HttpRequest.BodyPublishers.ofString(value))
                    .build();
            HttpClient client = HttpClient.newHttpClient();
            HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
            HttpResponse<String> response = client.send(request, handler);
            if (response.statusCode() != 200) {
                System.out.println("Ошибка метод putData code: " + response.statusCode());
            }
        } catch (IOException | InterruptedException | NullPointerException e) {
            System.out.println("Во время выполнения запроса ресурса по URL-адресу: '" + uri + "', возникла ошибка.");
        }
    }

    public String load(String key) {
        try {
            URI uri = URI.create("http://localhost:8078/load/" + key + "?API_TOKEN=" + getToken());
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .GET()
                    .build();
            HttpClient client = HttpClient.newHttpClient();
            HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
            HttpResponse<String> response = client.send(request, handler);
            if (response.statusCode() != 200) {
                System.out.println("Ошибка метод load kvtaskclient code: " + response.statusCode());
            }

            return response.body();

        } catch (IOException | InterruptedException | NullPointerException e) {
            System.out.println("Во время выполнения запроса ресурса  возникла ошибка.");
        }
        return null;
    }

    public String getToken() {
        return token;
    }

}