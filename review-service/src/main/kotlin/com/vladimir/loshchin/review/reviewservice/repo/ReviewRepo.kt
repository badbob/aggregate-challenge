package com.vladimir.loshchin.review.reviewservice.repo

import org.springframework.data.repository.CrudRepository
import com.vladimir.loshchin.review.reviewservice.model.Review
import org.springframework.stereotype.Repository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.Optional


@Repository
interface ReviewRepo : CrudRepository<Review, Long> {
    
    @Query("SELECT avg(score),count(*) FROM Review r WHERE productId = :productId")
    fun stat(@Param("productId") productId: Long): List<Array<Any>>;
}