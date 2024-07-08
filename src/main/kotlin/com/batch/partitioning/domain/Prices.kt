package com.batch.partitioning.domain

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.datetime

object Prices : IntIdTable("prices") {
    val productId = reference("product_id", Products)
    val value = integer("value")
    val createdAt = datetime("created_at")
}

class Price(
    id: EntityID<Int>,
) : IntEntity(id) {
    companion object : IntEntityClass<Price>(Prices)

    var product by Product referencedOn Prices.productId
    var value by Prices.value
    var createdAt by Prices.createdAt
}
