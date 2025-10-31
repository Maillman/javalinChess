package client;

import com.google.gson.Gson;

import java.net.ConnectException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.*;
import java.net.http.HttpResponse;

public class HTTPCommunicator {
    private final HttpClient client = HttpClient.newHttpClient();
    private final String serverUrl;

    public HTTPCommunicator(String url){
        this.serverUrl = url;
    }
    public <T> T makeRequest(String method, String path, Object body, String authToken, Class<T> responseClass)
            throws ResponseException {
        var request = HttpRequest.newBuilder(URI.create(this.serverUrl + path))
                .method(method, makeRequestBody(body));
        if(body != null) {
            request.setHeader("Content-Type", "application/json");
        }
        if(authToken != null) {
            request.setHeader("authorization", authToken);
        }
        HttpResponse<String> response = sendRequest(request.build());
        return handleResponse(response, responseClass);
    }
    private BodyPublisher makeRequestBody(Object body) {
        if(body != null){
            return BodyPublishers.ofString(new Gson().toJson(body));
        } else {
            return BodyPublishers.noBody();
        }
    }

    private HttpResponse<String> sendRequest(HttpRequest request) throws ResponseException {
        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception ex) {
            if(ex instanceof ConnectException){
                throw new ResponseException("Error connecting to the server. Server may not be running");
            }
            throw new ResponseException("Something went wrong with the request: " + ex.getMessage());
        }
    }

    private <T> T handleResponse(HttpResponse<String> response, Class<T> responseClass) throws ResponseException {
        int status = response.statusCode();
        if(!isSuccessful(status)){
            String body = response.body();
            if(body != null) {
                throw ResponseException.fromJson(body);
            }
            throw new ResponseException("Server responded with an error. Status Code: " + status);
        }
        if (responseClass != null) {
            return new Gson().fromJson(response.body(), responseClass);
        }

        return null;
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}
