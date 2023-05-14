package com.vladimir.loshchin.aggregator.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

import java.io.IOException;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.util.Assert;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ResponseStatusException;

import com.vladimir.loshchin.aggregator.integration.ProductClient;
import com.vladimir.loshchin.aggregator.integration.dto.DummyJsonError;
import com.vladimir.loshchin.aggregator.integration.dto.Product;
import com.vladimir.loshchin.aggregator.model.Aggregate;
import com.vladimir.loshchin.review.client.ReviewClient;
import com.vladimir.loshchin.review.model.Rating;

import reactor.core.publisher.Mono;

@SpringBootTest
public class AggregateControllerTest {

    @MockBean
    ProductClient productRepo;
    
    @MockBean
    ReviewClient reviewClient;
    
    @Autowired
    AggregateController controller;
    
    @Test
    public void testSuccess() {
        Rating rating = new Rating(1l, 4.5f, 2);
        Product product = new Product(1l, "Product title", "Product description");
        when(reviewClient.getRating(1l)).thenReturn(Mono.just(rating));
        when(productRepo.get(1l)).thenReturn(Mono.just(product));
        
        Aggregate agg = controller.getProduct(1l);
        
        assertThat(agg.product()).isSameAs(product);
        assertThat(agg.rating()).isEqualTo(Optional.of(rating));
    }

    @Test
    public void testNoRating() {
        Product product = new Product(1l, "Product title", "Product description");

        when(reviewClient.getRating(1l)).thenReturn(Mono.error(
                new WebClientResponseException(404, null, null, null, null)));

        when(productRepo.get(1l)).thenReturn(Mono.just(product));
        
        Aggregate agg = controller.getProduct(1l);
        
        assertThat(agg.product()).isSameAs(product);
        assertThat(agg.rating()).isEmpty();
    }

    @Test
    public void testNoSuchProduct() {
        Rating rating = new Rating(1l, 4.5f, 2);
        when(reviewClient.getRating(1l)).thenReturn(Mono.just(rating));
        
        HttpHeaders headers = new HttpHeaders();
        headers.add(CONTENT_TYPE, APPLICATION_JSON_VALUE);
        
        var ex = new WebClientResponseException(
                404, null, 
                headers, 
                "{\"message\":\"No such product\"}".getBytes(), null);
        ex.setBodyDecodeFunction(t -> new DummyJsonError("No such product"));
        
        when(productRepo.get(1l)).thenReturn(Mono.error(ex));

        try {
            Aggregate agg = controller.getProduct(1l);
        } catch (ResponseStatusException notFound) {
            assertThat(notFound.getStatusCode().value()).isEqualTo(404);
        }
    }
}
