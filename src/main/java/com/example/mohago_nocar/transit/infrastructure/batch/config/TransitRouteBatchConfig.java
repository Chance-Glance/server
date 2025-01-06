package com.example.mohago_nocar.transit.infrastructure.batch.config;

import com.example.mohago_nocar.transit.infrastructure.error.exception.OdsayBatchException;
import com.example.mohago_nocar.transit.infrastructure.messaging.persistence.OdsayApiRequestEvent;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class TransitRouteBatchConfig {

    private final FreshMessageTasklet freshMessageTasklet;
    private final DeferredMessageProcessor deferredMessageProcessor;
    private final EntityManagerFactory entityManagerFactory;

    @Bean
    public Job odsayApiJob(JobRepository jobRepository) {
        return new JobBuilder("odsayApiJob", jobRepository)
                .start(sendDeferredMessageIfPresent(jobRepository))
                .next(sendFreshMessage(jobRepository))
                .listener(new JobExecutionListener() {
                    @Override
                    public void afterJob(JobExecution jobExecution) {
                        log.info("sendDeferredMessages In steps send Message Num : {}",
                                jobExecution.getExecutionContext().getInt("processedDeferredMessages"));
                        log.info("sendFreshMessage In steps send Message Num : {}",
                                jobExecution.getExecutionContext().getInt("processedFreshMessages"));
                    }
                })
                .build();
    }

    @Bean
    public Step sendDeferredMessageIfPresent(
            JobRepository jobRepository
    ) {
        return new StepBuilder("deferredMessageStep", jobRepository)
                .<OdsayApiRequestEvent, OdsayApiRequestEvent>chunk(100, transactionManager())
                .reader(deferredMessageProcessor)
                .processor(deferredMessageProcessor)
                .writer(deferredMessageProcessor)
                .faultTolerant()
                .retry(OdsayBatchException.class)
                .retryLimit(3) // todo: retry 전부 실패했을 경우 listener 설정
                .build();
    }


    @Bean
    public Step sendFreshMessage(
            JobRepository jobRepository
    ){
        return new StepBuilder("sendFreshMessage", jobRepository)
                .tasklet(freshMessageTasklet, transactionManager())
                .build();
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new JpaTransactionManager(entityManagerFactory);
    }

}

