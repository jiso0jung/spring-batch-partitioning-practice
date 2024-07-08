package com.batch.partitioning.domain

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.datetime

object ProductAggregations : IntIdTable("product_aggregations") {
    val productId = reference("product_id", Products)
    val productName = varchar("product_name", 255)
    val totalQuantity = long("total_quantity")
    val totalAmount = double("total_amount")
    val createdAt = datetime("created_at")

    init {
        uniqueIndex(productId)
    }
}

class ProductAggregation(
    id: EntityID<Int>,
) : IntEntity(id) {
    companion object : IntEntityClass<ProductAggregation>(ProductAggregations)

    var productId by Product referencedOn ProductAggregations.productId
    var productName by ProductAggregations.productName
    var totalQuantity by ProductAggregations.totalQuantity
    var totalAmount by ProductAggregations.totalAmount
    var createdAt by ProductAggregations.createdAt
}
