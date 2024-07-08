package com.batch.partitioning.domain

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.date
import org.jetbrains.exposed.sql.javatime.datetime

object ProductDailyAggregations : IntIdTable("product_daily_aggregations") {
    val productAggregationId = reference("product_aggregation_id", ProductAggregations)
    val date = date("date")
    val totalQuantity = long("total_quantity")
    val totalAmount = double("total_amount")
    val createdAt = datetime("created_at")
}

class ProductDailyAggregation(
    id: EntityID<Int>,
) : IntEntity(id) {
    companion object : IntEntityClass<ProductDailyAggregation>(ProductDailyAggregations)

    var productAggregation by ProductAggregation referencedOn ProductDailyAggregations.productAggregationId
    var date by ProductDailyAggregations.date
    var totalQuantity by ProductDailyAggregations.totalQuantity
    var totalAmount by ProductDailyAggregations.totalAmount
    var createdAt by ProductDailyAggregations.createdAt
}
