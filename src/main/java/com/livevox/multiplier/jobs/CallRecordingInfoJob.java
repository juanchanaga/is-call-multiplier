/*
 * Copyright (c) 2021. Luis Felipe Sosa Alvarez. All rights reserved to LiveVox.
 * Use this subject to license terms.
 *
 * IS-CALL-MULTIPLIER
 */

package com.livevox.multiplier.jobs;

import com.livevox.multiplier.domain.exceptions.MissingFieldsException;
import com.livevox.multiplier.domain.exceptions.UnauthorizedException;
import com.livevox.multiplier.services.impl.LvRecordingRestServiceImpl;
import com.livevox.multiplier.domain.lvapi.CallRecording;
import com.livevox.multiplier.domain.JobRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataIntegrityViolationException;

import jakarta.xml.ws.WebServiceException;
import java.io.Serializable;
import java.util.concurrent.Callable;

@Slf4j
public class CallRecordingInfoJob extends AbstractConfigWrapper<LvRecordingRestServiceImpl>
        implements Callable<CallRecording>, Serializable {

    private static final long serialVersionUID = 52343599672699L;

    public CallRecordingInfoJob(JobRequest req) throws Exception {
        super(req, LvRecordingRestServiceImpl.class);
    }



    @Override
    public CallRecording call()  throws UnauthorizedException,
            DataIntegrityViolationException, MissingFieldsException, Exception {
        log.debug("START CallRecordingInfoJob.call() ");
        if (req == null || StringUtils.isBlank(req.getRecordingId())  ||
                getService() == null ||
                StringUtils.isBlank(req.getSessoinId())) {
            log.error("Can not lookup recording info.  The request is invalid. ");
            throw new MissingFieldsException();
        }
        try {
            double callStart = System.currentTimeMillis();
            CallRecording recording = getService().getRecordingInfo(req.getRecordingId(),
                    req.getClientId(), req.getSessoinId());
            log.warn(" Completed call recording info lookup.   dur=" +
                    ((System.currentTimeMillis() - callStart) / 1000));
            log.debug("END CallRecordingInfoJob.call() ");
            return recording;
        } catch (WebServiceException wsExp) {
            String msgPrefix = "Getting the call recording info for recordingId: ";
            if(wsExp.getCause() != null && wsExp.getCause() instanceof UnauthorizedException) {
                log.error(msgPrefix+req.getRecordingId()+
                        " the token was not autorized.");
                throw new UnauthorizedException(msgPrefix+req.getRecordingId());
            }
            log.error(msgPrefix+req.getRecordingId(), wsExp);
            throw wsExp;
        }catch (Exception t) {
            log.error("Looking up the call recording info for recordingId: "+req.getRecordingId(), t );
            throw t;
        }
    }



}