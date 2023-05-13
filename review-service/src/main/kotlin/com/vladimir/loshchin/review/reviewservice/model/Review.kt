package com.vladimir.loshchin.review.reviewservice.model

import org.jetbrains.annotations.NotNull
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.GeneratedValue

@Entity
data class Review(
    @Id @GeneratedValue val id: Long?,
    val productId: Long,
    @field:Max(5) @field:Min(1) val score: Int) {
}
