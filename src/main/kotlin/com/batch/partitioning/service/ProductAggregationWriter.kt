package com.batch.partitioning.service

import com.batch.partitioning.dto.ProductAggregationDto
import com.batch.partitioning.repository.ProductAggregationRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class ProductAggregationWriter(
    private val productAggregationRepository: ProductAggregationRepository
) {
    fun write(productAggregationDtoList: List<ProductAggregationDto>) {
        logger.info("Writing ${productAggregationDtoList.size} product aggregations")

        productAggregationDtoList.forEach {
            productAggregationRepository.save(it)
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java)
    }
}
