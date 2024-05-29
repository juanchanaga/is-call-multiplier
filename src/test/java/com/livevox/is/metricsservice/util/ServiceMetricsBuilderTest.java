package com.livevox.is.metricsservice.util;

import com.livevox.is.domain.metrics.CustomMetric;
import com.livevox.is.domain.metrics.ServiceMetrics;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.util.CollectionUtils.firstElement;

@ExtendWith(MockitoExtension.class)
class ServiceMetricsBuilderTest {

    @Test
    public void testBuilderAttributes() {

        Random random = new Random();
        Integer clientId = random.nextInt();
        Integer callCenterId = random.nextInt();
        Integer skillId = random.nextInt();
        Instant start = Instant.MIN;
        Instant end = Instant.MAX;
        Instant intervalDate = Instant.EPOCH;
        String skillName = "name";
        CustomMetric customMetric = CustomMetric.builder().build();

        ServiceMetrics metrics = ServiceMetricsBuilder.initialize()
                .clientId(clientId)
                .callCenterId(callCenterId)
                .skillId(skillId)
                .start(start)
                .end(end)
                .intervalDate(intervalDate)
                .skillName(skillName)
                .addCustomMetric(customMetric)
                .getMetrics();

        assertEquals(clientId, metrics.getClientId());
        assertEquals(callCenterId, metrics.getCallCenterId());
        assertEquals(skillId, metrics.getSkillId());
        assertEquals(start, metrics.getStart());
        assertEquals(end, metrics.getEnd());
        assertEquals(intervalDate, metrics.getIntervalDate());
        assertEquals(skillName, metrics.getSkillName());
        assertFalse(metrics.getCustomMetrics().isEmpty());
        assertEquals(customMetric, firstElement(metrics.getCustomMetrics()));
    }

    @Test
    public void testBuilderSeconds() {

        ServiceMetrics metrics = ServiceMetricsBuilder.initialize()
                .addReadySeconds(3)
                .addReadySeconds(1)
                .addNotReadySeconds(2)
                .addNotReadySeconds(4)
                .addInCallSeconds(1)
                .addInCallSeconds(1)
                .addWrapUpSeconds(5)
                .addWrapUpSeconds(3)
                .addPreviewDialSeconds(6)
                .addPreviewDialSeconds(1)
                .addHoldSeconds(0)
                .addHoldSeconds(1)
                .addRightPartyInCallSeconds(9)
                .addRightPartyInCallSeconds(3)
                .addRightPartyWrapUpSeconds(5)
                .addRightPartyWrapUpSeconds(4)
                .addIvrSeconds(5)
                .addIvrSeconds(5)
                .getMetrics();

        assertEquals(4, metrics.getReadySeconds());
        assertEquals(6, metrics.getNotReadySeconds());
        assertEquals(2, metrics.getInCallSeconds());
        assertEquals(8, metrics.getWrapUpSeconds());
        assertEquals(7, metrics.getPreviewDialSeconds());
        assertEquals(1, metrics.getHoldSeconds());
        assertEquals(12, metrics.getRightPartyInCallSeconds());
        assertEquals(9, metrics.getRightPartyWrapUpSeconds());
        assertEquals(10, metrics.getIvrSeconds());
    }

    @Test
    public void testBuilderCalls() {

        ServiceMetrics metrics = ServiceMetricsBuilder.initialize()
                .addTotalInboundCalls(1)
                .addTotalInboundCalls(2)
                .addCallsAttempted(3)
                .addCallsAttempted(4)
                .addConnectedCalls(1)
                .addConnectedCalls(5)
                .addNotConnectedCalls(3)
                .addNotConnectedCalls(4)
                .addPromiseToPayCalls(3)
                .addPromiseToPayCalls(3)
                .addSuccessfulCallsBelowServiceLevelThreshold(1)
                .addSuccessfulCallsBelowServiceLevelThreshold(1)
                .addSuccessfulCallsAfterServiceLevelThreshold(0)
                .addSuccessfulCallsAfterServiceLevelThreshold(0)
                .addAbandonedCallsBelowServiceLevelThreshold(8)
                .addAbandonedCallsBelowServiceLevelThreshold(8)
                .addAbandonedCallsAfterServiceLevelThreshold(2)
                .addAbandonedCallsAfterServiceLevelThreshold(2)
                .getMetrics();

        assertEquals(3, metrics.getTotalInboundCalls());
        assertEquals(7, metrics.getCallsAttempted());
        assertEquals(6, metrics.getConnectedCalls());
        assertEquals(7, metrics.getNotConnectedCalls());
        assertEquals(6, metrics.getPtpCalls());
        assertEquals(2, metrics.getSuccessfulCallsBelowServiceLevelThreshold());
        assertEquals(0, metrics.getSuccessfulCallsAfterServiceLevelThreshold());
        assertEquals(16, metrics.getAbandonedCallsBelowServiceLevelThreshold());
        assertEquals(4, metrics.getAbandonedCallsAfterServiceLevelThreshold());
    }

    @Test
    public void testBuilderAdditionalMethods() {

        ServiceMetrics metrics = ServiceMetricsBuilder.initialize()
                .addTransferHoldDuration(1)
                .addTransferHoldDuration(2)
                .addSuccessfulOperatorTransfers(3)
                .addSuccessfulOperatorTransfers(4)
                .addFailedOperatorTransfers(3)
                .addFailedOperatorTransfers(3)
                .addRightPartyContacts(1)
                .addRightPartyContacts(1)
                .addWrongPartyContacts(6)
                .addWrongPartyContacts(3)
                .addRunningAccount("123")
                .addRunningAccount("123")
                .addRunningAccount("777")
                .getMetrics();

        assertEquals(3, metrics.getTransferHoldDuration());
        assertEquals(7, metrics.getSuccessfulOperatorTransfers());
        assertEquals(6, metrics.getFailedOperatorTransfers());
        assertEquals(2, metrics.getRightPartyContacts());
        assertEquals(9, metrics.getWrongPartyContacts());
        assertEquals(2, metrics.getAccountsAttempted());
        assertEquals(2, metrics.getRunningAccountList().size());

    }

}