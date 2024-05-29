package com.livevox.is.metricsservice.domain;

import com.livevox.is.domain.api.realtime.enumeration.InteractionTypeEnum;
import com.livevox.is.domain.metrics.enumeration.CampaignType;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

import static java.util.Objects.nonNull;

@Data
@Table("interaction_events")
public class InteractionEventDAO extends Audit {

    @Id
    private Long id;
    private String account;
    private String accountNumber;
    private String acctTransactionId;
    private String agentActionType;
    private Long agentId;
    private String agentLoginId;
    private String agentSkillId;
    private String areaCodeState;
    private Integer callCenterId;
    private String callDirection;
    private String callerId;
    private Integer campaignId;
    private CampaignType campaignType;
    private String campaignTypeId;
    private String carrier;
    private Integer clientId;
    private String customOutcome1;
    private String customOutcome2;
    private String customOutcome3;
    private Integer duration;
    private Instant end;
    private String eventType;
    private String fromEmail;
    private InteractionTypeEnum interactionType;
    private String lvResult;
    private Integer lvResultId;
    private String operatorPhone;
    private Integer operatorTransfer;
    private Integer operatorTransferSuccessful;
    private String phoneDialed;
    private Integer phonePosition;
    private String phoneNumber;
    private Integer priority;
    private Integer roundedIvrDuration;
    private Integer serviceId;
    private Integer serviceLevelThreshold;
    private String sessionId;
    private String smsCode;
    private Instant start;
    private String threadId;
    private Instant timestamp;
    private String toEmail;
    private Long transactionId;
    private Integer transferHoldDuration;
    private String zipCode;

    public Boolean isOperatorTransfer() {
        return nonNull(this.operatorTransfer) && this.operatorTransfer == 1;
    }

    public Boolean isOperatorTransferSuccessful() {
        return nonNull(this.operatorTransferSuccessful) && this.operatorTransferSuccessful == 1;
    }

}
