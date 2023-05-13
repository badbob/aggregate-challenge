package com.vladimir.loshchin.aggregator.controller;

import java.io.IOException;
import java.time.Duration;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerErrorException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.vladimir.loshchin.aggregator.integration.ProductRepository;
import com.vladimir.loshchin.aggregator.integration.dto.DummyJsonError;
import com.vladimir.loshchin.aggregator.integration.dto.Product;
import com.vladimir.loshchin.aggregator.model.Aggregate;
import com.vladimir.loshchin.review.client.ReviewClient;
import com.vladimir.loshchin.review.model.Rating;

import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

@RestController
@RequestMapping("/aggregate")
public class AggregateController {

    @Autowired
    private ProductRepository productClient;
    
    @Autowired
    private ReviewClient reviewClient;

    @GetMapping("/{id}")
    public Aggregate getProduct(@PathVariable("id") Long id) {

        // Ignoring if there is no rating found by ID
        Mono<Optional<Rating>> rating = reviewClient.getRating(id)
                .onErrorResume(
                        WebClientResponseException.class, 
                        ex -> ex.getStatusCode().value() == 404 
                            ? Mono.empty() : Mono.error(ex))
                .map(Optional::of)
                .defaultIfEmpty(Optional.empty());
        
        Mono<Product> product = productClient.get(id)
                .retryWhen(Retry.fixedDelay(3, Duration.ofMillis(300))
                        .filter(ex -> {
                    if (ex instanceof WebClientResponseException) {
                        return ((WebClientResponseException) ex)
                                .getStatusCode().is5xxServerError();
                    }
                    if (ex instanceof IOException) {
                        return true;
                    }
                    return false;
                }))
                .onErrorMap(
                        WebClientResponseException.class, 
                        ex -> { 
                            var err = ex.getResponseBodyAs(DummyJsonError.class);
                            
                            return new ResponseStatusException(
                                    ex.getStatusCode(), 
                                    err.message());
                        }
                );
        
        return Mono.zip(rating, product, 
                (r, p) -> new Aggregate(p, r)).block();
    }
}
