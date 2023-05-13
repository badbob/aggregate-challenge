package com.vladimir.loshchin.review.reviewservice

import org.springframework.web.bind.annotation.GetMapping
import com.vladimir.loshchin.review.model.Rating
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.beans.factory.annotation.Autowired
import com.vladimir.loshchin.review.reviewservice.repo.ReviewRepo
import org.springframework.web.bind.annotation.PostMapping
import com.vladimir.loshchin.review.reviewservice.model.Review
import org.springframework.web.server.ResponseStatusException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity


@RestController
@RequestMapping("/rating")
class ReviewController(@Autowired val reviewRepo: ReviewRepo) {

    @GetMapping("/{productId}")
    fun rating(@PathVariable("productId") productId: Long) : Rating {
        val stat = reviewRepo.stat(productId)

        val rating = stat.get(0)[0] as Double?
        val count = stat.get(0)[1] as Long?

        if (rating == null) {
            throw ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "No review for productId was found: " + productId)
        } else {
            return Rating(productId, rating.toFloat(), count?.toInt()!!);
        }
    }
    
    @PostMapping("/{productId}/{score}")
    fun post(@PathVariable("productId") productId: Long,
             @PathVariable("score") scope: Int) : ResponseEntity<Void> {
        reviewRepo.save(Review(null, productId, scope))
        return ResponseEntity<Void>(null, HttpStatus.CREATED)
    }
}
