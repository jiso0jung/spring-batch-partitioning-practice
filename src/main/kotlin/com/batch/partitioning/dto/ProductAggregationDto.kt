package com.batch.partitioning.dto

import java.time.LocalDate

data class ProductAggregationDto(
    val productId: Int,
    val productName: String,
    val totalQuantity: Long,
    val totalAmount: Double,
    val daily: List<ProductDailyAggregationDto>,
)

data class ProductDailyAggregationDto(
    val date: LocalDate,
    val totalQuantity: Long,
    val totalAmount: Double,
)
