package com.example.mohago_nocar.transit.infrastructure.batch.config;

import com.example.mohago_nocar.transit.infrastructure.batch.persistence.OdsayApiRequestEntry;
import com.example.mohago_nocar.transit.infrastructure.batch.persistence.OdsayApiRequestEntryRepository;
import com.example.mohago_nocar.transit.infrastructure.messaging.persistence.OdsayApiRequestEvent;
import com.example.mohago_nocar.transit.infrastructure.messaging.TransitRouteEventManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.mohago_nocar.transit.infrastructure.odsay.client.ODsayApiLimitProvider.DEFAULT_BATCH_LIMIT_PER_DAY;

@Component
@RequiredArgsConstructor
@Slf4j
public class FreshMessageTasklet implements Tasklet {

    private final OdsayApiRequestEntryRepository odsayApiRequestEntryRepository;
    private final TransitRouteEventManager messagingService;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        ExecutionContext executionContext = contribution.getStepExecution().getJobExecution().getExecutionContext();

        int attempted = executionContext.getInt("processedDeferredMessages");
        int remained = DEFAULT_BATCH_LIMIT_PER_DAY - attempted;
        log.info(">>> remained {} messages ", remained);

        int processedFreshMessageCount = 0;
        if (remained > 0) {
            PageRequest pageRequest = PageRequest.of(0, remained, Sort.by(Sort.Direction.ASC, "createdAt"));
            List<OdsayApiRequestEntry> freshMessages = odsayApiRequestEntryRepository.findAllByOrderByCreatedAtAsc(pageRequest);

            for (OdsayApiRequestEntry freshMessage : freshMessages) {
                messagingService.sendToPriorityQueue(OdsayApiRequestEvent.parse(freshMessage));
            }

            processedFreshMessageCount = freshMessages.size();
            odsayApiRequestEntryRepository.deleteAllByIdInBatch(freshMessages.stream()
                    .map(OdsayApiRequestEntry::getId).collect(Collectors.toList()));
        }

        executionContext.putInt("processedFreshMessages", processedFreshMessageCount);

        return RepeatStatus.FINISHED;
    }

}
