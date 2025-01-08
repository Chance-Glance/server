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

    private static final String PROCESSED_DEFERRED_MESSAGES = "processedDeferredMessages";
    private int processedDeferredMessageCount;
    private int dailyBatchLimit;

    private final TransitRouteEventManager messageManager;
    private final ODsayApiLimitProvider apiLimitProvider;

    @Override
    public void beforeStep(StepExecution stepExecution) {
        this.dailyBatchLimit = apiLimitProvider.getDailyBatchLimit();
        ExecutionContext executionContext = stepExecution.getExecutionContext();
        this.processedDeferredMessageCount = executionContext.getInt(PROCESSED_DEFERRED_MESSAGES, 0);
        log.info("Starting step with processedDeferredMessages: {}", processedDeferredMessageCount);
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        ExecutionContext executionContext = stepExecution.getJobExecution().getExecutionContext();
        executionContext.putInt(PROCESSED_DEFERRED_MESSAGES, processedDeferredMessageCount);
        log.info("Step completed. Total processedDeferredMessages: {}", processedDeferredMessageCount);

        return ExitStatus.COMPLETED;
    }

    @Override
    public OdsayApiRequestEvent read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (processedDeferredMessageCount >= dailyBatchLimit) {
            return null;
        }

        try {
            if (dailyBatchLimit <= 0) {
                log.warn("Daily API Batch limit reached. Stopping processing.");
                return null;
            }

            OdsayApiRequestEvent message = messageManager.readFromDeferredQueue();

            if (message != null) {
                processedDeferredMessageCount++;
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
