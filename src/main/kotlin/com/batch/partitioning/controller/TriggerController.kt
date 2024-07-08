package com.batch.partitioning.controller

import com.batch.partitioning.job.MyJobConfig
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TriggerController(
    private val jobLauncher: JobLauncher,
    private val myJobConfig: MyJobConfig,
) {
    @PostMapping("/trigger")
    fun trigger() {
        val jobParameters =
            JobParametersBuilder()
                .addString("timestamp", System.currentTimeMillis().toString())
                .toJobParameters()

        jobLauncher.run(myJobConfig.myJob(), jobParameters)
    }
}
