package com.batch.partitioning.repository

import com.batch.partitioning.domain.OrderItem
import com.batch.partitioning.domain.OrderItems
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class OrderRepository {
    @Transactional(readOnly = true)
    fun findByProductId(productId: Int) = OrderItem.find { OrderItems.productId eq productId }.toList()
}
