package com.batch.partitioning.job

import com.batch.partitioning.config.TaskExecutorConfig
import com.batch.partitioning.domain.Product
import com.batch.partitioning.dto.ProductAggregationDto
import com.batch.partitioning.repository.ProductRepository
import com.batch.partitioning.service.ProductAggregationProcessor
import com.batch.partitioning.service.ProductAggregationWriter
import org.slf4j.LoggerFactory
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.scope.context.StepSynchronizationManager
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.item.Chunk
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.support.ListItemReader
import org.springframework.batch.item.support.SynchronizedItemReader
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager

@Configuration
class MyStepConfig(
    private val jobRepository: JobRepository,
    private val transactionManager: PlatformTransactionManager,
    private val reader: ProductRepository,
    private val processor: ProductAggregationProcessor,
    private val writer: ProductAggregationWriter,
) {
    @Bean(STEP_NAME + "_manager")
    fun stepManager(): Step =
        StepBuilder("manager", jobRepository)
            .partitioner(STEP_NAME, partitioner())
            .partitionHandler(TaskExecutorConfig.batchPartitionHandler(step()))
            .build()

    @Bean(STEP_NAME + "_partitioner")
    @StepScope
    fun partitioner(): MyPartitioner = MyPartitioner(reader, 1, null)

    //    @JobScope
    @Bean(STEP_NAME)
    fun step(): Step =
        StepBuilder(STEP_NAME, jobRepository)
            .chunk<Product, ProductAggregationDto>(200, transactionManager)
            .reader(reader())
            .processor(processor())
            .writer(writer())
            .build()

    fun reader(): SynchronizedItemReader<Product> {
        val stepExecutionContext =
            StepSynchronizationManager.getContext()?.stepExecutionContext

        val startId = stepExecutionContext?.let { it["minId"] as Int }
        val endId = stepExecutionContext?.let { it["maxId"] as Int }

        val profileIds = reader.findAll(startId, endId)

        return SynchronizedItemReader(ListItemReader(profileIds))
    }

    fun processor(): ItemProcessor<Product, ProductAggregationDto> =
        ItemProcessor<Product, ProductAggregationDto> { product ->
            processor.process(product)
        }

    fun writer(): ItemWriter<ProductAggregationDto> =
        ItemWriter<ProductAggregationDto> { chunk: Chunk<out ProductAggregationDto> ->
            writer.write(chunk.items)
        }

    companion object {
        private const val STEP_NAME = "myStep"
        private val logger = LoggerFactory.getLogger(this::class.java)
    }
}
