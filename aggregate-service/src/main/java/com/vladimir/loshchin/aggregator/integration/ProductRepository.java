package com.vladimir.loshchin.aggregator.integration;

import org.springframework.stereotype.Component;

import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.JdkClientHttpConnector;

import com.vladimir.loshchin.aggregator.integration.dto.Product;

import java.time.Duration;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Redirect;

import reactor.core.publisher.Mono;
import org.springframework.http.MediaType;

// import static org.springframework.web.client.reactive.support.RxJava1ClientWebRequestBuilder.*;

@Component
public class ProductRepository {

    private String URL = "https://dummyjson.com/products/{productId}";

    private WebClient webClient;

    public ProductRepository() {

        HttpClient httpClient = HttpClient.newBuilder()
            .followRedirects(Redirect.NORMAL)
            .connectTimeout(Duration.ofSeconds(20))
            .build();

        ClientHttpConnector connector =
            new JdkClientHttpConnector(httpClient);

        webClient = WebClient.builder().clientConnector(connector).build();
    }

    public Mono<Product> get(String productId) {

        return webClient.get()
            .uri(URL, productId)
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToMono(Product.class);
    }
}
