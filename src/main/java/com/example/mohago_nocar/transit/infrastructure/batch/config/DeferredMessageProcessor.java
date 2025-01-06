package com.example.mohago_nocar.transit.infrastructure.batch.config;

import com.example.mohago_nocar.transit.infrastructure.error.exception.OdsayBatchException;
import com.example.mohago_nocar.transit.infrastructure.messaging.TransitRouteEventManager;
import com.example.mohago_nocar.transit.infrastructure.messaging.persistence.OdsayApiRequestEvent;
import com.example.mohago_nocar.transit.infrastructure.odsay.client.ODsayApiLimitProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.*;
import org.springframework.stereotype.Component;

import static com.example.mohago_nocar.transit.infrastructure.error.code.TransitRouteErrorCode.ITEM_READER_ERROR;
import static com.example.mohago_nocar.transit.infrastructure.error.code.TransitRouteErrorCode.ITEM_WRITE_ERROR;

@Component
@RequiredArgsConstructor
@Slf4j
public class DeferredMessageProcessor implements ItemReader<OdsayApiRequestEvent>,
        ItemProcessor<OdsayApiRequestEvent, OdsayApiRequestEvent>, ItemWriter<OdsayApiRequestEvent>, StepExecutionListener {

    private int deferredMessageProcessingCount = 0;

    private final TransitRouteEventManager messageManager;
    private final ODsayApiLimitProvider apiLimitProvider;
    private StepExecution stepExecution;

    @Override
    public void beforeStep(StepExecution stepExecution) {
        this.stepExecution = stepExecution;

        ExecutionContext executionContext = stepExecution.getExecutionContext();
        this.deferredMessageProcessingCount = executionContext.getInt("processedDeferredMessages", 0);
        log.info("Starting step with processedDeferredMessages: {}", deferredMessageProcessingCount);
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        ExecutionContext executionContext = stepExecution.getJobExecution().getExecutionContext();
        executionContext.putInt("processedDeferredMessages", deferredMessageProcessingCount);
        log.info("Step completed. Total processedDeferredMessages: {}", deferredMessageProcessingCount);

        return ExitStatus.COMPLETED;
    }

    @Override
    public OdsayApiRequestEvent read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        int batchLimit = apiLimitProvider.getTodayBatchLimit();

        if (deferredMessageProcessingCount >= batchLimit) {
            return null;
        }

        try {
            if (batchLimit <= 0) {
                log.warn("Daily API Batch limit reached. Stopping processing.");
                return null;
            }

            OdsayApiRequestEvent message = messageManager.readFromDeferredQueue();

            if (message != null) {
                deferredMessageProcessingCount++;
            }

            return message;
        } catch (Exception e) {
            log.error("Error reading message from queue: {}", e.getMessage());
            throw new OdsayBatchException(e.getMessage(), ITEM_READER_ERROR);
        }
    }

    @Override
    public OdsayApiRequestEvent process(OdsayApiRequestEvent message) throws Exception {
        message.incrementRetryCount();
        return message;
    }

    @Override
    public void write(Chunk<? extends OdsayApiRequestEvent> messages) throws Exception {
        try {
            for (OdsayApiRequestEvent message : messages) {
                messageManager.sendToPriorityQueue(message);
                log.debug("Successfully sent message {} to priority queue", message.getId());
            }
        } catch (Exception e) {
            log.error("Error writing messages to priority queue: {}", e.getMessage());
            throw new OdsayBatchException(e.getMessage(), ITEM_WRITE_ERROR);
        }
    }

}
