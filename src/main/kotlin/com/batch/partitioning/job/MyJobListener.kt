package com.batch.partitioning.job

import org.slf4j.LoggerFactory
import org.springframework.batch.core.JobExecutionListener
import org.springframework.stereotype.Component

@Component
class MyJobListener : JobExecutionListener {
    override fun beforeJob(jobExecution: org.springframework.batch.core.JobExecution) {
       logger.info("Before job")
    }

    override fun afterJob(jobExecution: org.springframework.batch.core.JobExecution) {
        logger.info("After job")
    }

    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java)
    }
}
