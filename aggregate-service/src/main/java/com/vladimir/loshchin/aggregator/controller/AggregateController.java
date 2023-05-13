package com.vladimir.loshchin.aggregator.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.vladimir.loshchin.aggregator.integration.ProductRepository;
import com.vladimir.loshchin.aggregator.integration.dto.Product;
import com.vladimir.loshchin.aggregator.model.Aggregate;
import com.vladimir.loshchin.review.client.ReviewClient;
import com.vladimir.loshchin.review.model.Rating;

import reactor.core.publisher.Mono;

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
                        ex -> ex.getRawStatusCode() == 404 
                            ? Mono.empty() : Mono.error(ex))
                .map(Optional::of)
                .defaultIfEmpty(Optional.empty());
        
        Mono<Product> product = productClient.get(id)
                .onErrorMap(
                        WebClientResponseException.class, 
                        ex -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND, 
                                "No such product with given ID: " + id));
        
        return Mono.zip(rating, product, 
                (r, p) -> new Aggregate(p, r)).block();
    }
}
