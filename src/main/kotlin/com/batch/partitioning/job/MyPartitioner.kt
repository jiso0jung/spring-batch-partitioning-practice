package com.batch.partitioning.job

import com.batch.partitioning.repository.ProductRepository
import org.slf4j.LoggerFactory
import org.springframework.batch.core.partition.support.Partitioner
import org.springframework.batch.item.ExecutionContext
import kotlin.math.min

open class MyPartitioner(
    private val productRepository: ProductRepository,
    private val minProductId: Int,
    private val maxProductId: Int? = null,
) : Partitioner {
    override fun partition(gridSize: Int): MutableMap<String, ExecutionContext> {
        val min = minProductId
        val max = maxProductId ?: productRepository.findMaxProductId() ?: minProductId
        val result = mutableMapOf<String, ExecutionContext>()

        logger.info("마이그레이션 시작 : $min ~ $max")

        for ((count, i) in (min..max step 2000).withIndex()) {
            val executionContext = ExecutionContext()
            executionContext.putInt("minId", i)
            executionContext.putInt("maxId", min(i + 2000, max))

            result["partition$count"] = executionContext
        }

        return result
    }

    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java)
    }
}
