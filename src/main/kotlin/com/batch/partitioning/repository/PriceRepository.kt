package com.batch.partitioning.repository

import com.batch.partitioning.domain.Price
import com.batch.partitioning.domain.Prices
import org.jetbrains.exposed.sql.and
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Repository
class PriceRepository {
    @Transactional(readOnly = true)
    fun findByProductAndTime(
        productId: Int,
        time: LocalDateTime,
    ) = Price
        .find {
            (Prices.productId eq productId) and
                (Prices.createdAt lessEq time)
        }.maxByOrNull { it.createdAt }
}
