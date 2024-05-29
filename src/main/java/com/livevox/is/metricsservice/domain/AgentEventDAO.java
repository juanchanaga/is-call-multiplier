package com.livevox.is.metricsservice.domain;

import com.livevox.is.domain.metrics.enumeration.AgentEventType;
import com.livevox.is.domain.metrics.enumeration.CampaignType;
import com.livevox.is.domain.metrics.enumeration.LineNumber;
import com.livevox.is.domain.metrics.enumeration.NotReadyReasonCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Getter
@Setter
@Table("agent_events")
public class AgentEventDAO extends Audit {

    @Id
    private Long id;
    private String eventId;
    private Instant timestamp;
    private Integer clientId;
    private Integer agentId;
    private Integer agentServiceId;
    private Integer agentTeamId;
    private LineNumber lineNumber;
    private AgentEventType eventType;
    private NotReadyReasonCode notReadyReasonCode;
    private Integer callServiceId;
    private Long transactionId;
    private String phoneNumber;
    private String result;
    private Integer agent2Id;
    private CampaignType campaignType;
    private String supervisorSessionId;
    private String supervisorPhoneNumber;
    private Integer seqNum;
    private String toEmail;
    private String fromEmail;
    private Integer ptpAmount;

}
