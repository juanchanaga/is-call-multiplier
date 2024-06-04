package com.livevox.is.metricsservice.domain;

import com.livevox.integration.commons.domain.enums.DncType;
import com.livevox.integration.commons.domain.lvapi.*;
import com.livevox.integration.commons.domain.stats.IdType;
import com.livevox.is.metricsservice.service.config.AtgConfigService;
import org.apache.commons.beanutils.BeanUtils;
import com.livevox.is.metricsservice.domain.RecordingReportRequest.SortBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class JobRequest implements Serializable {
    protected static final Logger log = LoggerFactory.getLogger(JobRequest.class);

    private Long id;
    private String recordingId;
    private Integer clientId;
    private String host;
    private List<Long> agentIds;
    private List<Long> termCodes;
    private String application;
    private String sessoinId;
    private AtgConfigService configService;
    private String account;
    private Long campaignId;
    private Long serviceId;
    private String phone;
    private String zip;
    private DncType type;
    private String name;
    private String description;
    private List<DialingPolicy> policy;
    private Date startDate;
    private Date endDate;
    private String originalAccountNumber;
    private RestFilter filter;
    private String loginId;
    private Integer offset;
    private Integer count;
    private Integer durationMin;
    private Integer durationMax;
    private SortBy sortBy;

    public JobRequest() {
    }


    public JobRequest(Integer clientId, String sessionId, String application, AtgConfigService configService) {
        this.configService = configService;
        this.clientId = clientId;
        this.sessoinId = sessionId;
        this.application = application;
    }


    public JobRequest(ListRequest lstReq) {
        if(lstReq == null) {
            return;
        }
        try {
            BeanUtils.copyProperties(this, lstReq);
        } catch(Exception e) {
            throw new IllegalStateException(e);
        }
    }


    public JobRequest(DialingProfile profile) {
        if(profile == null) {
            return;
        }
        try {
            BeanUtils.copyProperties(this, profile);
        } catch(Exception e) {
            throw new IllegalStateException(e);
        }
    }


    public JobRequest(CallDetail call) {
        if(call == null) {
            return;
        }
        try {
            BeanUtils.copyProperties(this, call);
            setPhone(call.getPhoneDialed());
            setSessoinId(call.getApiSessionId());
        } catch(Exception e) {
            throw new IllegalStateException(e);
        }
    }


    public JobRequest(DncRequest req) {
        if(req == null) {
            return;
        }
        try {
            BeanUtils.copyProperties(this, req);
            setSessoinId(req.getApiSessionId());
        } catch(Exception e) {
            throw new IllegalStateException(e);
        }
    }


    public JobRequest(AgentRequest req) {
        if(req == null) {
            return;
        }
        try {
            BeanUtils.copyProperties(this, req);
            setPhone( req.getPhoneNumber() );
            setSessoinId( req.getApiSessionId() );
            setCallCenterIds( (req.getFilter() != null) ?
                    req.getFilter().getCallCenter() : null );
            setServiceIds( (req.getFilter() != null) ?
                    req.getFilter().getService() : null );
        } catch(Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public JobRequest(RecordingRequest req)  {
        try {
            if(req == null) {
                return;
            }
            BeanUtils.copyProperties(this, req);
            setRecordingId( req.getId() );
            setSessoinId( req.getApiSessionId() );
            setCallCenterIds(null);
            addCallCenterIdsAsLong( (req.getCallCenterIds() != null) ?
                    req.getCallCenterIds() : null );
            setServiceIds(null);
            addServiceIdsAsLong( (req.getServiceIds() != null) ?
                    req.getServiceIds() : null );
            setSortBy( ( req.getSortBy() != null) ? req.getSortBy() :
                    RecordingReportRequest.SortBy.CALL_START_TIME ) ;
        } catch(Exception e) {
            throw new IllegalStateException(e);
        }
    }


    public JobRequest copy() {
        JobRequest newReq = new JobRequest();
        try {
            BeanUtils.copyProperties(newReq, this);
            return newReq;
        } catch(Exception e) {
            log.error("Cloning JobRequest failed. "+e);
        }

        return newReq;
    }


    public ListRequest toListRequest()  {
        try {
            ListRequest lstReq = new ListRequest();
            BeanUtils.copyProperties(lstReq, this);
            lstReq.setApiSessionId( getSessoinId());
            return lstReq;
        } catch(Exception e) {
            throw new IllegalStateException(e);
        }
    }


    public CallRecordingRequest toCallRecordingRequest()  {
        try {
            CallRecordingRequest newReq = new CallRecordingRequest();
            BeanUtils.copyProperties(newReq, this);
            newReq.setId(getRecordingId());
            newReq.setAgentIds(getAgentIds());
            newReq.setCallCenterIds(findCallCenterIdsAsLong());
            newReq.setServiceIds(findServiceIdsAsLong());
            newReq.setApiSessionId(getSessoinId());

            log.debug("JobRequest.toCallRecordingRequest Session ID: "+ getSessoinId());
            return newReq;
        } catch(Exception e) {
            throw new IllegalStateException(e);
        }
    }


    public AgentRequest toAgentRequest()  {
        try {
            AgentRequest newReq = new AgentRequest();
            BeanUtils.copyProperties(newReq, this);
            newReq.setApiSessionId(getSessoinId());
            newReq.setLastName(getName());

            return newReq;
        } catch(Exception e) {
            throw new IllegalStateException(e);
        }
    }


    public Integer getClientId() {
        return clientId;
    }


    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }


    public String getHost() {
        return host;
    }


    public void setHost(String host) {
        this.host = host;
    }

    public String getSessoinId() {
        return sessoinId;
    }


    public void setSessoinId(String sessoinId) {
        this.sessoinId = sessoinId;
    }


    public String getApplication() {
        return application;
    }


    public void setApplication(String application) {
        this.application = application;
    }


    public String getAccount() {
        return account;
    }


    public void setAccount(String account) {
        this.account = account;
    }


    public Long getServiceId() {
        return serviceId;
    }


    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }


    public String getPhone() {
        return phone;
    }


    public void setPhone(String phone) {
        this.phone = phone;
    }


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public String getZip() {
        return zip;
    }


    public void setZip(String zip) {
        this.zip = zip;
    }


    public DncType getType() {
        return type;
    }


    public void setType(DncType type) {
        this.type = type;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getDescription() {
        return description;
    }


    public void setDescription(String description) {
        this.description = description;
    }


    public List<DialingPolicy> getPolicy() {
        return policy;
    }


    public void setPolicy(List<DialingPolicy> policy) {
        this.policy = policy;
    }


    public Date getStartDate() {
        return startDate;
    }


    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }


    public Date getEndDate() {
        return endDate;
    }


    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }


    public String getOriginalAccountNumber() {
        return originalAccountNumber;
    }


    public void setOriginalAccountNumber(String originalAccountNumber) {
        this.originalAccountNumber = originalAccountNumber;
    }

    public RestFilter getFilter() {
        return filter;
    }

    public void setFilter(RestFilter filter) {
        this.filter = filter;
    }


    public String getLoginId() {
        return loginId;
    }


    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }


    public List<com.livevox.integration.commons.domain.stats.IdType> getServiceIds() {
        return (filter == null) ? null : filter.getService();
    }


    public List<Long> findServiceIdsAsLong() {
        if(filter == null || filter.getService() == null) {
            return null;
        }
        List<Long> serviceIdList = new ArrayList<Long>();
        for(com.livevox.integration.commons.domain.stats.IdType idType: filter.getService()) {
            if(idType != null && idType.getId() != null) {
                serviceIdList.add(idType.getId());
            }
        }
        return serviceIdList;
    }


    public void setServiceIds(List<com.livevox.integration.commons.domain.stats.IdType> newServiceIds) {
        if(filter == null) {
            filter = new RestFilter();
        }
        filter.setService(newServiceIds);
    }

    public void addServiceIdsAsInt(List<Integer> newServiceIds) {
        if(newServiceIds == null) {
            return;
        }
        if(filter == null ) {
            filter = new RestFilter();
        }
        for(Integer srvcId : newServiceIds) {
            filter.addService(Long.valueOf(srvcId));
        }
    }


    public void addServiceIdsAsLong(List<Long> newServiceIds) {
        if(newServiceIds == null) {
            return;
        }
        if(filter == null ) {
            filter = new RestFilter();
        }
        for(Long srvcId : newServiceIds) {
            filter.addService(srvcId);
        }
    }


    public List<com.livevox.integration.commons.domain.stats.IdType> getCallCenterIds() {
        return (filter == null) ? null : filter.getCallCenter();
    }

    public List<Long> findCallCenterIdsAsLong() {
        if(filter == null || filter.getCallCenter() == null) {
            return null;
        }
        List<Long> callCenterIdsList = new ArrayList<Long>();
        for(com.livevox.integration.commons.domain.stats.IdType idType: filter.getCallCenter()) {
            if(idType != null && idType.getId() != null) {
                callCenterIdsList.add(idType.getId());
            }
        }
        return callCenterIdsList;
    }


    public void setCallCenterIds(List<IdType> newCallCenterIds) {
        if(filter == null) {
            filter = new RestFilter();
        }
        filter.setCallCenter(newCallCenterIds);
    }


    public void addCallCenterIdsAsLong(List<Long> newCallCenterIds) {
        if(newCallCenterIds == null) {
            return;
        }
        if(filter == null ) {
            filter = new RestFilter();
        }
        for(Long srvcId : newCallCenterIds) {
            if(srvcId != null) {
                filter.addCallCenter(srvcId);
            }
        }
    }


    public Integer getOffset() {
        return offset;
    }


    public void setOffset(Integer offset) {
        this.offset = offset;
    }


    public Integer getCount() {
        return count;
    }


    public void setCount(Integer count) {
        this.count = count;
    }


    public String getRecordingId() {
        return recordingId;
    }


    public void setRecordingId(String recordingId) {
        this.recordingId = recordingId;
    }


    public List<Long> getAgentIds() {
        return agentIds;
    }

    public List<Long> agentIdsToLong() {
        if( getAgentIds() == null){
            return null;
        }
        List<Long> agntIds = new ArrayList<Long>();
        for(Long aId : getAgentIds() ) {
            if(aId != null) {
                agntIds.add(Long.valueOf(aId));
            }
        }
        return agntIds;
    }


    public void setAgentIds(List<Long> agentIds) {
        this.agentIds = agentIds;
    }


    public List<Long> getTermCodes() {
        return termCodes;
    }


    public void setTermCodes(List<Long> termCodes) {
        this.termCodes = termCodes;
    }


    public Long getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(Long campaignId) {
        this.campaignId = campaignId;
    }

    public Integer getDurationMin() {
        return durationMin;
    }

    public void setDurationMin(Integer durationMin) {
        this.durationMin = durationMin;
    }

    public Integer getDurationMax() {
        return durationMax;
    }

    public void setDurationMax(Integer durationMax) {
        this.durationMax = durationMax;
    }

    public RecordingReportRequest.SortBy getSortBy() {
        return sortBy;
    }

    public void setSortBy(RecordingReportRequest.SortBy sortBy) {
        this.sortBy = sortBy;
    }

    public void addAgentId(Long agentId) {
        if (agentIds == null) {
            agentIds = new ArrayList<Long>();
        }
        agentIds.add(agentId);
    }


    public void addTermCode(Long termCode) {
        if (termCodes == null) {
            termCodes = new ArrayList<Long>();
        }
        termCodes.add(termCode);
    }


    public AtgConfigService getConfigService() {
        return configService;
    }

    public void setConfigService(AtgConfigService configService) {
        this.configService = configService;
    }
}
