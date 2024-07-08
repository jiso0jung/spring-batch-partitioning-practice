package com.batch.partitioning.repository

import com.batch.partitioning.domain.Product
import com.batch.partitioning.domain.Products
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.andIfNotNull
import org.jetbrains.exposed.sql.selectAll
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class ProductRepository {
    @Transactional(readOnly = true)
    fun findMaxProductId() =
        Products
            .select(Products.id)
            .orderBy(Products.id, SortOrder.DESC)
            .limit(1)
            .map { it[Products.id].value }
            .firstOrNull()

    @Transactional(readOnly = true)
    fun findAll(
        minId: Int? = null,
        maxId: Int? = null,
    ) = Products
        .selectAll()
        .where {
            Op.TRUE andIfNotNull
                minId?.let { Products.id greaterEq it } andIfNotNull
                maxId?.let { Products.id lessEq it }
        }.map { Product.wrapRow(it) }


    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java)
    }
}
