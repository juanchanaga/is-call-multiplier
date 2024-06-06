/*
 * Copyright (c) 2020. Luis Felipe Sosa Alvarez. All rights reserved to LiveVox.
 * Use this subject to license terms.
 *
 * IS-CALL-MULTIPLIER
 */

package com.livevox.integration.commons.domain.lvapi;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.livevox.integration.commons.domain.stats.DefaultDao;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CallDetail extends DefaultDao implements Serializable {

    private Long serviceId;
    private Integer clientId;
    private Long callCenterId;
    private String sessionId;
    private Long agentId;
    private String agentLoginId;
    private String account;
    private String originalAccount;
    private Long campaignId;
    private String phoneDialed;
    private Integer priority;
    private Integer totalCount;
    private String apiSessionId;
    private Integer phonePosition;
    private Integer duration;
    private String lvResult;
    private Long lvResultId;
    private Date start;
    private Date end;
    private String zipCode;
    private String zipCodeState;
    private String areaCodeState;
    private String callEventType;
    private Long transactionId;
    private String operatorPhone;
    private String campaignType;
    private String callerId;
    private String customOutcome1;
    private String customOutcome2;
    private String customOutcome3;
    private String eventId;
    private Integer operatorTransfer;
    private Integer operatorTransferSuccessful;
    private Integer roundedIvrDuration;
    private Integer serviceLevelThreshold;
    private Integer transferHoldDuration;
    private String agentActionType;
    private String email;
    /**
     * WARNING: If you are in a good mood and want to stay that way STOP READING
     * NOW.... or at least have a drink first.
     *
     * As it turns out the call events API can not be relied up on to return the
     * correct term code. Don't ask. You don't want to know. To account for this
     * the call event API has a flag called "correctInvalidTermCodes" that can
     * be set to true to try to lookup the correct term codes. They creats a
     * major performance issue though. So this boolean has been added so you can
     * tell if the term code has been checked, and is "probably" correct.
     *
     */
    private boolean resultCodeVerified;
    private boolean resultCodeCouldNotBeVerified;
    private Date lastUpdated;
    private Date firstUpdated;

    public CallDetail() {}

    public CallDetail(DncRequest dncReq) {
        if(dncReq == null) {
            return;
        }
        setAccount(dncReq.getAccount());
        setPhoneDialed(dncReq.getPhone());
        setId((dncReq.getId() != null) ? dncReq.getId().longValue() : null);
        setClientId((dncReq.getClientId() != null) ? dncReq.getClientId().intValue() : null);
        setServiceId((dncReq.getServiceId() != null) ? dncReq.getServiceId() : null);
        setId( dncReq.getId() );
        setZipCode( dncReq.getZip() );
        setTotalCount( dncReq.getCount() );
        setApiSessionId( dncReq.getApiSessionId() );
    }
}
