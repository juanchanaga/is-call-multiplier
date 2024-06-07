/*
 * Copyright (c) 2021. Luis Felipe Sosa Alvarez. All rights reserved to LiveVox.
 * Use this subject to license terms.
 *
 * IS-CALL-MULTIPLIER
 */

package com.livevox.multiplier.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import org.apache.commons.beanutils.BeanUtils;

@JsonInclude(Include.NON_NULL)
public class RecordingRequest extends CallRecordingRequest {

    private static final long serialVersionUID = 2530255260784754L;

    private String application;

    public RecordingRequest copyInstance()  {
        try {
            RecordingRequest newReq = new RecordingRequest();
            BeanUtils.copyProperties(newReq, this);
            return newReq;
        } catch (Exception e) {
            throw new IllegalStateException("Cloning RecordingRequest object failed.", e);
        }
    }

    public String getApplication() {
        return application;
    }


    public void setApplication(String application) {
        this.application = application;
    }


    public Integer getDuration() {
        return getDurationMax();
    }


    public void setDuration(Integer duration) {
        setDurationMax(duration);
        setDurationMin(duration);
    }
}
