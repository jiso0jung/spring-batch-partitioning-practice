package com.batch.partitioning.job

import org.springframework.batch.core.Job
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MyJobConfig(
    private val jobRepository: JobRepository,
    private val myStepConfig: MyStepConfig,
    private val myJobListener: MyJobListener,
) {
    @Bean("myJob")
    fun myJob(): Job =
        JobBuilder("myJob", jobRepository)
            .listener(myJobListener)
            .start(myStepConfig.stepManager())
            .build()
}
