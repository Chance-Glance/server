package com.example.mohago_nocar.transit.infrastructure.batch.config;

import com.example.mohago_nocar.transit.infrastructure.batch.persistence.OdsayApiRequestEntry;
import com.example.mohago_nocar.transit.infrastructure.batch.persistence.OdsayApiRequestEntryRepository;
import com.example.mohago_nocar.transit.infrastructure.messaging.persistence.OdsayApiRequestEvent;
import com.example.mohago_nocar.transit.infrastructure.messaging.TransitRouteEventManager;
import com.example.mohago_nocar.transit.infrastructure.odsay.client.ODsayApiLimitProvider;
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

@Component
@RequiredArgsConstructor
@Slf4j
public class FreshMessageTasklet implements Tasklet {

    private static final String PROCESSED_FRESH_MESSAGES = "processedFreshMessages";
    private static final String PROCESSED_DEFERRED_MESSAGES = "processedDeferredMessages";

    private final OdsayApiRequestEntryRepository odsayApiRequestEntryRepository;
    private final TransitRouteEventManager messagingService;
    private final ODsayApiLimitProvider oDsayApiLimitProvider;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        ExecutionContext executionContext = getExecutionContext(contribution);
        int availableCount = calcTransmittableCount(executionContext);
        int processedFreshMessageCount = 0;

        if (has(availableCount)) {
            List<OdsayApiRequestEntry> freshMessages = findApiRequestEntries(availableCount);
            sendMessageToQueue(freshMessages);
            cleanSavedMessages(freshMessages);
            processedFreshMessageCount = freshMessages.size();
        }

        executionContext.putInt(PROCESSED_FRESH_MESSAGES, processedFreshMessageCount);

        return RepeatStatus.FINISHED;
    }

    private int calcTransmittableCount(ExecutionContext executionContext) {
        int processedCount = getProcessedDeferredMessages(executionContext);
        return oDsayApiLimitProvider.getDailyBatchLimit() - processedCount;
    }

    private List<OdsayApiRequestEntry> findApiRequestEntries(int availableCount) {
        PageRequest pageRequest = PageRequest.of(0, availableCount, Sort.by(Sort.Direction.ASC, "createdAt"));
        return odsayApiRequestEntryRepository.findAllByOrderByCreatedAtAsc(pageRequest);
    }

    private void sendMessageToQueue(List<OdsayApiRequestEntry> freshMessages) {
        for (OdsayApiRequestEntry freshMessage : freshMessages) {
            messagingService.sendToPriorityQueue(OdsayApiRequestEvent.parse(freshMessage));
        }
    }

    private void cleanSavedMessages(List<OdsayApiRequestEntry> freshMessages) {
        odsayApiRequestEntryRepository.deleteAllByIdInBatch(freshMessages.stream()
                .map(OdsayApiRequestEntry::getId).collect(Collectors.toList()));
    }

    private boolean has(int remainingCount) {
        return remainingCount > 0;
    }

    private int getProcessedDeferredMessages(ExecutionContext executionContext) {
        return executionContext.getInt(PROCESSED_DEFERRED_MESSAGES);
    }

    private ExecutionContext getExecutionContext(StepContribution contribution) {
        return contribution.getStepExecution().getJobExecution().getExecutionContext();
    }

}
