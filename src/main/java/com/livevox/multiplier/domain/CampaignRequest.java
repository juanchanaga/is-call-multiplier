package com.livevox.multiplier.domain;

import com.livevox.multiplier.domain.requests.RecordingReportRequest.SortBy;

import java.util.Date;
import java.util.List;

public class CampaignRequest extends RecordingRequest{

    private static final long serialVersionUID = 55260784754L;

    private String application;

    public String getApplication() {
        return application;
    }

    @Override
    public void setApplication(String application) {
        this.application = application;
    }

    @Override
    public Integer getDuration() {
        return super.getDuration();
    }

    @Override
    public void setDuration(Integer duration) {
        super.setDuration(duration);
    }

    @Override
    public Date getStartDate() {
        return super.getStartDate();
    }

    @Override
    public void setStartDate(Date startDate) {
        super.setStartDate(startDate);
    }

    @Override
    public Date getEndDate() {
        return super.getEndDate();
    }

    @Override
    public void setEndDate(Date endDate) {
        super.setEndDate(endDate);
    }

    @Override
    public Integer getDurationMin() {
        return super.getDurationMin();
    }

    @Override
    public void setDurationMin(Integer durationMin) {
        super.setDurationMin(durationMin);
    }

    @Override
    public Integer getDurationMax() {
        return super.getDurationMax();
    }

    @Override
    public void setDurationMax(Integer durationMax) {
        super.setDurationMax(durationMax);
    }

    @Override
    public String getAccount() {
        return super.getAccount();
    }

    @Override
    public void setAccount(String account) {
        super.setAccount(account);
    }

    @Override
    public String getPhone() {
        return super.getPhone();
    }

    @Override
    public void setPhone(String phone) {
        super.setPhone(phone);
    }

    @Override
    public String getOriginalAccountNumber() {
        return super.getOriginalAccountNumber();
    }

    @Override
    public void setOriginalAccountNumber(String originalAccountNumber) {
        super.setOriginalAccountNumber(originalAccountNumber);
    }

    @Override
    public SortBy getSortBy() {
        return super.getSortBy();
    }

    @Override
    public void setSortBy(SortBy sortBy) {
        super.setSortBy(sortBy);
    }

    @Override
    public Integer getCount() {
        return super.getCount();
    }

    @Override
    public void setCount(Integer count) {
        super.setCount(count);
    }

    @Override
    public Integer getOffset() {
        return super.getOffset();
    }

    @Override
    public void setOffset(Integer offset) {
        super.setOffset(offset);
    }

    @Override
    public Integer getClientId() {
        return super.getClientId();
    }

    @Override
    public void setClientId(Integer id) {
        super.setClientId(id);
    }

    @Override
    public List<Long> getAgentIds() {
        return super.getAgentIds();
    }

    @Override
    public void setAgentIds(List<Long> agentIds) {
        super.setAgentIds(agentIds);
    }

    @Override
    public List<Long> getCallCenterIds() {
        return super.getCallCenterIds();
    }

    @Override
    public void setCallCenterIds(List<Long> callCenterIds) {
        super.setCallCenterIds( callCenterIds );
    }

    @Override
    public Long getCampaignId() {
        return super.getCampaignId();
    }

    @Override
    public void setCampaignId(Long campaignId) {
        super.setCampaignId(campaignId);
    }

    @Override
    public List<Long> getTermCodes() {
        return super.getTermCodes();
    }

    @Override
    public void setTermCodes(List<Long> termCodes) {
        super.setTermCodes(termCodes);
    }

    @Override
    public List<Long> getServiceIds() {
        return super.getServiceIds();
    }

    @Override
    public void setServiceIds(List<Long> serviceIds) {
        super.setServiceIds(serviceIds);
    }

    @Deprecated
    public String getToken() {
        return super.getApiSessionId();
    }

    @Deprecated
    public void setToken(String token) {
        super.setApiSessionId(token);
    }

    @Override
    public String getApiSessionId() {
        return super.getApiSessionId();
    }

    @Override
    public void setApiSessionId(String token) {
        super.setApiSessionId(token);
    }

    @Override
    public String getId() {
        return super.getId();
    }

    @Override
    public void setId(String id) {
        super.setId(id);
    }

    @Override
    public String getApiVersion() {
        return super.getApiVersion();
    }

    @Override
    public void setApiVersion(String id) {
        super.setApiVersion(id);
    }

}
