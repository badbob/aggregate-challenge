package com.vladimir.loshchin.review.reviewservice

import org.springframework.context.annotation.Configuration
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import com.vladimir.loshchin.review.reviewservice.repo.ReviewRepo
import com.vladimir.loshchin.review.reviewservice.model.Review

@Configuration
class DBInitializer {

    @Bean
    fun initDB(reviewRepo : ReviewRepo) : CommandLineRunner {
        return CommandLineRunner() {
            reviewRepo.save(Review(null, 1, 3));
            reviewRepo.save(Review(null, 1, 4));
            reviewRepo.save(Review(null, 1, 4));

            reviewRepo.save(Review(null, 2, 5));
        };
    }
}