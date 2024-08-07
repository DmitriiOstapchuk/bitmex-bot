package com.dmitrii.ostapchuk.bitmexBot.util.generator;

import com.dmitrii.ostapchuk.bitmexBot.model.AuthenticationHeaders;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpRequest;


@Component
public class HttpRequestGenerator {

    public HttpRequest createHttpRequest(String baseUrl, String endpoint, String data, String httpMethod, AuthenticationHeaders authenticationHeaders) {
        HttpRequest.BodyPublisher bodyPublisher = httpMethod.equals("GET") ? HttpRequest.BodyPublishers.noBody()
                : HttpRequest.BodyPublishers.ofString(data);

        return HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + endpoint))
                .method(httpMethod, bodyPublisher)
                .header("Content-Type", "application/json")
                .header("api-expires", authenticationHeaders.getExpires())
                .header("api-key", authenticationHeaders.getApiKey())
                .header("api-signature", authenticationHeaders.getSignature())
                .build();
    }
}
