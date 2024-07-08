package com.batch.partitioning.config

import org.springframework.batch.core.Step
import org.springframework.batch.core.partition.support.TaskExecutorPartitionHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.task.SimpleAsyncTaskExecutor
import org.springframework.core.task.TaskExecutor
import org.springframework.core.task.VirtualThreadTaskExecutor
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor

@Configuration
class TaskExecutorConfig {
    @Bean
    fun batch1TaskExecutor(): TaskExecutor = SimpleAsyncTaskExecutor("simple-async-")

    @Bean
    fun batch2TaskExecutor(): TaskExecutor = VirtualThreadTaskExecutor("virtual-thread-")

    @Bean
    fun batchTaskExecutor(): TaskExecutor {
        val executor = ThreadPoolTaskExecutor()
        executor.corePoolSize = 7
        executor.maxPoolSize = 7
        executor.setThreadNamePrefix("partition-thread1-")
        executor.setWaitForTasksToCompleteOnShutdown(true)
        executor.initialize()

        return executor
    }

    companion object {
        fun batchPartitionHandler(step: Step): TaskExecutorPartitionHandler {
            val executor = ThreadPoolTaskExecutor()
            executor.corePoolSize = 7
            executor.maxPoolSize = 7
            executor.setThreadNamePrefix("partition-thread2-")
            executor.setWaitForTasksToCompleteOnShutdown(true)
            executor.initialize()

            val partitionHandler = TaskExecutorPartitionHandler()
            partitionHandler.step = step
            partitionHandler.setTaskExecutor(executor)
            partitionHandler.gridSize = 7

            return partitionHandler
        }
    }
}
