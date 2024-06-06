package com.livevox.integration.shareddata.multiplier.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.livevox.integration.commons.domain.stats.DateRange;
import org.apache.commons.beanutils.BeanUtils;

@JsonInclude(Include.NON_NULL)
public class AssessorRequest extends AssessorData {

    private static final long serialVersionUID = 2530255260784754L;

    private DateRange recordingDateRange;

    private DateRange listenDateRange;


    @Override
    public AssessorRequest copy()  {
        try {
            AssessorRequest newAssDat = new AssessorRequest();
            BeanUtils.copyProperties(newAssDat, this);
            return newAssDat;
        } catch (Exception e) {
            throw new IllegalStateException("Failed to clone AssessorRequest.", e);
        }
    }

    public DateRange getRecordingDateRange() {
        return recordingDateRange;
    }

    public void setRecordingDateRange(DateRange recordingDateRange) {
        this.recordingDateRange = recordingDateRange;
    }

    public DateRange getListenDateRange() {
        return listenDateRange;
    }

    public void setListenDateRange(DateRange listenDateRange) {
        this.listenDateRange = listenDateRange;
    }

}