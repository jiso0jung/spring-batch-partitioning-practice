package com.batch.partitioning.domain

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.datetime

object OrderItems : IntIdTable("order_items") {
    val productId = reference("product_id", Products)
    val orderId = reference("order_id", Orders)
    val quantity = integer("quantity")
    val createdAt = datetime("created_at")
}

class OrderItem(
    id: EntityID<Int>,
) : IntEntity(id) {
    companion object : IntEntityClass<OrderItem>(OrderItems)

    var product by Product referencedOn OrderItems.productId
    var order by Order referencedOn OrderItems.orderId
    var quantity by OrderItems.quantity
    var createdAt by OrderItems.createdAt
}
