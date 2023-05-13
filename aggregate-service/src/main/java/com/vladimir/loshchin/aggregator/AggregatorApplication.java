package com.vladimir.loshchin.aggregator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.vladimir.loshchin.review.client.ReviewClient;

@SpringBootApplication
public class AggregatorApplication {

    @Bean
    public ReviewClient reviewClient() {
        return new ReviewClient();
    }
    
	public static void main(String[] args) {
		SpringApplication.run(AggregatorApplication.class, args);
	}

}
