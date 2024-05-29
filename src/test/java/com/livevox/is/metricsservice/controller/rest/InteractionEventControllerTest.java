package com.livevox.is.metricsservice.controller.rest;

import com.livevox.is.domain.metrics.ServiceMetrics;
import com.livevox.is.domain.metrics.request.ActiveServiceMetricsRequest;
import com.livevox.is.domain.metrics.request.ActiveServicesRequest;
import com.livevox.is.domain.metrics.request.CallMetricsRequest;
import com.livevox.is.domain.metrics.request.MetricsDirection;
import com.livevox.is.domain.util.IdClass;
import com.livevox.is.metricsservice.service.InteractionEventService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = InteractionEventController.class)
@AutoConfigureMockMvc(addFilters = false)
class InteractionEventControllerTest {

    @MockBean
    private InteractionEventService interactionEventService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetActiveServices() throws Exception {

        Integer callCenterId = 5;
        Integer idClassValue = 1;
        ArgumentCaptor<ActiveServicesRequest> requestCaptor =
                ArgumentCaptor.forClass(ActiveServicesRequest.class);

        when(interactionEventService.getActiveServices(requestCaptor.capture()))
                .thenReturn(Arrays.asList(new IdClass(idClassValue)));

        this.mockMvc.perform(
                        get("/active-services")
                                .param("start", "2020-01-01T15:33:33Z")
                                .param("end", "2020-01-01T15:33:33Z")
                                .param("callCenterId", callCenterId.toString())
                                .param("clientId", "1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("[{\"id\":1}]")));

        ActiveServicesRequest requestValue = requestCaptor.getValue();
        assertNotNull(requestValue.getStart());
        assertNotNull(requestValue.getEnd());
        assertNull(requestValue.getCount());
        assertNull(requestValue.getOffset());
        assertEquals(callCenterId, requestValue.getCallCenterId());
    }

    @Test
    public void testGetActiveServiceMetrics() throws Exception {

        Integer serviceId = 10;
        Integer callCenterId = 16;
        Integer count = 5;
        Integer offset = 11;
        MetricsDirection direction = MetricsDirection.INBOUND;
        ArgumentCaptor<ActiveServiceMetricsRequest> requestCaptor =
                ArgumentCaptor.forClass(ActiveServiceMetricsRequest.class);

        ServiceMetrics metrics = new ServiceMetrics();
        metrics.setPtpCalls(1);
        when(interactionEventService.getActiveServiceMetrics(requestCaptor.capture()))
                .thenReturn(metrics);

        this.mockMvc.perform(
                        get("/active-service-metrics")
                                .param("start", "2020-01-01T15:33:33Z")
                                .param("end", "2020-01-01T15:33:33Z")
                                .param("serviceId", serviceId.toString())
                                .param("callCenterId", callCenterId.toString())
                                .param("direction", direction.name())
                                .param("count", count.toString())
                                .param("offset", offset.toString())
                                .param("clientId", "1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("{\"ptpCalls\":1}")));

        ActiveServiceMetricsRequest requestValue = requestCaptor.getValue();
        assertNotNull(requestValue.getStart());
        assertNotNull(requestValue.getEnd());
        assertEquals(direction, requestValue.getDirection());
        assertEquals(serviceId, requestValue.getServiceId());
        assertEquals(callCenterId, requestValue.getCallCenterId());
        assertEquals(count, requestValue.getCount());
        assertEquals(offset, requestValue.getOffset());
    }

    @Test
    public void testGetCallMetrics() throws Exception {

        String callSessionId = "1234";
        ArgumentCaptor<CallMetricsRequest> requestCaptor =
                ArgumentCaptor.forClass(CallMetricsRequest.class);

        ServiceMetrics metrics = new ServiceMetrics();
        metrics.setConnectedCalls(5);
        when(interactionEventService.getCallMetrics(requestCaptor.capture()))
                .thenReturn(Optional.of(metrics));

        this.mockMvc.perform(
                        get("/call-metrics/{callSessionId}", callSessionId)
                                .queryParam("clientId", "1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("{\"connectedCalls\":5}")));

        assertEquals(callSessionId, requestCaptor.getValue().getCallSessionId());
    }

}