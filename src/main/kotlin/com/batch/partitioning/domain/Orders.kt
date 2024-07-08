package com.batch.partitioning.domain

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.datetime

object Orders : IntIdTable("orders") {
    val status = varchar("status", 50)
    val createdAt = datetime("created_at")
    val updatedAt = datetime("updated_at")
}

class Order(
    id: EntityID<Int>,
) : IntEntity(id) {
    companion object : IntEntityClass<Order>(Orders)

    var status by Orders.status
    var createdAt by Orders.createdAt
    var updatedAt by Orders.updatedAt
}
