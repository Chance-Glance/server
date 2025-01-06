package com.example.mohago_nocar.transit.infrastructure.scheduler;

import com.example.mohago_nocar.transit.infrastructure.error.exception.OdsayBatchException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static com.example.mohago_nocar.transit.infrastructure.error.code.TransitRouteErrorCode.TRANSIT_ROUTE_BATCH_ERROR;

@Component
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class ODsayApiJobScheduler {

    private final JobLauncher jobLauncher;
    private final Job odsayApiJob;

    @Scheduled(cron = "0 0 2 * * *")
    public void runJob(){
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();

        try {
            JobExecution execution = jobLauncher.run(odsayApiJob, jobParameters);
            log.info("Job finished with status: {}", execution.getStatus());

        } catch (Exception e) {
            log.error("error occured at TransitRouteScheduler : {}", e.getStackTrace());
            throw new OdsayBatchException(TRANSIT_ROUTE_BATCH_ERROR);
        }
    }


/*
    // todo: 오디세이 데이터 업데이트 메서드
    // 한 달에 한 번 수행.
    @Scheduled(cron = "0 0 2 1 * *")
    @Override
    public void updateTransitInfos() {

    }*/

}
