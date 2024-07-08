package com.batch.partitioning

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@EnableBatchProcessing
@SpringBootApplication
class SpringBatchPartitioningApplication

fun main(args: Array<String>) {
    runApplication<SpringBatchPartitioningApplication>(*args)
}
