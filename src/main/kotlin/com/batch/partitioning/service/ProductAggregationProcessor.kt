package com.batch.partitioning.service

import com.batch.partitioning.domain.Order
import com.batch.partitioning.domain.OrderItem
import com.batch.partitioning.domain.Product
import com.batch.partitioning.dto.ProductAggregationDto
import com.batch.partitioning.dto.ProductDailyAggregationDto
import com.batch.partitioning.repository.OrderRepository
import com.batch.partitioning.repository.PriceRepository
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class ProductAggregationProcessor(
    private val orderRepository: OrderRepository,
    private val priceRepository: PriceRepository,
) {
    fun process(product: Product): ProductAggregationDto? {
        return runBlocking {
            // 주어진 product에 해당하는 모든 OrderItems 가져오기
            val orderItems = orderRepository.findByProductId(product.id.value)

            if (orderItems.isEmpty()) {
                return@runBlocking null
            }

            // 일별 판매 집계 데이터 계산
            val dailyAggregations = calculateDailyAggregations(orderItems)

            // 총 판매량 및 총 매출 계산
            val totalQuantity = orderItems.filter { it.order.status == "ORDERED" }.sumOf { it.quantity.toLong() }
            val totalAmount = orderItems.filter { it.order.status == "ORDERED" }.sumOf { calculateItemAmount(it, product) }

            ProductAggregationDto(
                productId = product.id.value,
                productName = product.name,
                totalQuantity = totalQuantity,
                totalAmount = totalAmount,
                daily = dailyAggregations,
            )
        }
    }

    // calculateDailyAggregations 함수 구현
    private fun calculateDailyAggregations(orderItems: List<OrderItem>): List<ProductDailyAggregationDto> {
        val dailyAggregations = mutableMapOf<LocalDate, ProductDailyAggregationDto>()

        // 일일 집계를 위한 맵 초기화
        val dailyMap = mutableMapOf<LocalDate, Pair<Long, Double>>() // Pair<totalQuantity, totalAmount>

        // 일일 집계 데이터 생성
        for (orderItem in orderItems) {
            val order = orderItem.order
            val quantity = orderItem.quantity.toLong()
            val amount = calculateItemAmount(orderItem, orderItem.product)

            if (order.status == "CANCELLED" && order.createdAt.toLocalDate() == order.updatedAt.toLocalDate()) continue

            orderedDailyAggregation(order, quantity, amount, dailyMap)
            cancelledDailyAggregation(order, quantity, amount, dailyMap)
        }

        // Pair에서 ProductDailyAggregationDto으로 변환
        val result =
            dailyMap.map { (date, pair) ->
                ProductDailyAggregationDto(date = date, totalQuantity = pair.first, totalAmount = pair.second)
            }

        return result
    }

    private fun cancelledDailyAggregation(
        order: Order,
        quantity: Long,
        amount: Double,
        dailyMap: MutableMap<LocalDate, Pair<Long, Double>>,
    ) {
        if (order.status == "CANCELLED") {
            val cancelDate = order.updatedAt.toLocalDate()

            if (!dailyMap.containsKey(cancelDate)) {
                dailyMap[cancelDate] = Pair(0L, 0.0)
            }

            val (cancelledTotalQuantity, canceledTotalAmount) = dailyMap[cancelDate]!!

            dailyMap[cancelDate] =
                Pair(
                    cancelledTotalQuantity - quantity,
                    canceledTotalAmount - amount,
                )
        }
    }

    private fun orderedDailyAggregation(
        order: Order,
        quantity: Long,
        amount: Double,
        dailyMap: MutableMap<LocalDate, Pair<Long, Double>>,
    ): Pair<Order, Long> {
        val orderDate = order.createdAt.toLocalDate()

        // Ordered 상태의 경우 양수로 집계
        if (!dailyMap.containsKey(orderDate)) {
            dailyMap[orderDate] = Pair(0L, 0.0)
        }

        val (totalQuantity, totalAmount) = dailyMap[orderDate]!!

        dailyMap[orderDate] = Pair(totalQuantity + quantity, totalAmount + amount)

        return Pair(order, quantity)
    }

    // calculateItemAmount 함수 구현
    private fun calculateItemAmount(
        orderItem: OrderItem,
        product: Product,
    ): Double {
        val price = priceRepository.findByProductAndTime(product.id.value, orderItem.createdAt)?.value ?: 0

        return orderItem.quantity.toDouble() * price
    }
}
