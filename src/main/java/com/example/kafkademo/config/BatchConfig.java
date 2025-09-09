package com.example.kafkademo.config;

import com.example.kafkademo.service.JoinService;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.batch.core.launch.JobLauncher;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class BatchConfig {

    @Bean
    public Step joinStep(JobRepository jobRepository,
                         PlatformTransactionManager transactionManager,
                         JoinService joinService) {
        return new StepBuilder("joinStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    joinService.processReadyPairs();
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean
    public Job joinMessagesJob(JobRepository jobRepository, Step joinStep) {
        return new JobBuilder("joinMessagesJob", jobRepository)
                .start(joinStep)
                .build();
    }

    private final JobLauncher jobLauncher;
    private final Job joinMessagesJob;

    // Run every 30 seconds
    @Scheduled(fixedRate = 30_000)
    public void runJob() throws Exception {
        JobParameters params = new JobParametersBuilder()
                .addLong("run.id", System.currentTimeMillis())
                .toJobParameters();
        jobLauncher.run(joinMessagesJob, params);
    }
}

