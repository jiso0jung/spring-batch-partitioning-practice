package com.batch.partitioning.repository

import com.batch.partitioning.domain.Product
import com.batch.partitioning.domain.ProductAggregation
import com.batch.partitioning.domain.ProductDailyAggregation
import com.batch.partitioning.dto.ProductAggregationDto
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Repository
class ProductAggregationRepository {
    @Transactional
    fun save(dto: ProductAggregationDto): ProductAggregation {
        val productAggregation =
            ProductAggregation.new {
                productId = Product[dto.productId] // Dto의 productId를 사용하여 연결
                productName = dto.productName
                totalQuantity = dto.totalQuantity
                totalAmount = dto.totalAmount
                createdAt = LocalDateTime.now()
            }

        dto.daily.map {
            ProductDailyAggregation.new {
                this.productAggregation = productAggregation // Dto의 productId를 사용하여 연결
                this.date = it.date
                this.totalQuantity = it.totalQuantity
                this.totalAmount = it.totalAmount
                this.createdAt = LocalDateTime.now()
            }
        }

        return productAggregation
    }
}
