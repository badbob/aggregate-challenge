package com.vladimir.loshchin.review.client;

import com.vladimir.loshchin.review.model.Rating;

import reactor.core.publisher.Mono;

import java.net.http.HttpClient;
import java.net.http.HttpClient.Redirect;
import java.time.Duration;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.JdkClientHttpConnector;
import org.springframework.beans.factory.annotation.Value;

@Component
public class ReviewClient {

    private WebClient webClient;
    
    @Value("${review.host}")
    private String reviewHost;
    
    public ReviewClient() {
        HttpClient httpClient = HttpClient.newBuilder()
            .followRedirects(Redirect.NORMAL)
            .connectTimeout(Duration.ofSeconds(3))
            .build();

        ClientHttpConnector connector =
            new JdkClientHttpConnector(httpClient);

        webClient = WebClient.builder().clientConnector(connector).build();
    }
    
    public Mono<Rating> getRating(String productId) {
        return webClient.get()
                .uri("http://{reviewHost}:8081/rating/{productId}", reviewHost, productId)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Rating.class);
    }
}
