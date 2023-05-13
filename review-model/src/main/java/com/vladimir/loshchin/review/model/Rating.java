package com.vladimir.loshchin.review.model;

public record Rating(
    Long productId, 
    Float rating, 
    /**
     * Total number of reviews
     */
    int total) {
    
}
