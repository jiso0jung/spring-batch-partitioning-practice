package com.batch.partitioning.domain

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.datetime

object Products : IntIdTable("products") {
    val name = varchar("name", 255)
    val createdAt = datetime("created_at")
}

class Product(
    id: EntityID<Int>,
) : IntEntity(id) {
    companion object : IntEntityClass<Product>(Products)

    var name by Products.name
    var createdAt by Products.createdAt
}
