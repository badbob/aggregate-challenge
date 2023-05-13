package com.vladimir.loshchin.aggregator.model;

import java.util.Optional;

import com.vladimir.loshchin.aggregator.integration.dto.Product;
import com.vladimir.loshchin.review.model.Rating;

public record Aggregate(Product product, Optional<Rating> rating) {

}
