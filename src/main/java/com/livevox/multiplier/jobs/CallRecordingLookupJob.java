/*
 * Copyright (c) 2021. Luis Felipe Sosa Alvarez. All rights reserved to LiveVox.
 * Use this subject to license terms.
 *
 * IS-CALL-MULTIPLIER
 */

package com.livevox.multiplier.jobs;

import com.livevox.multiplier.domain.CallRecordingRequest;
import com.livevox.multiplier.domain.ClientInternalDetails;
import com.livevox.multiplier.domain.exceptions.InvalidParametersException;
import com.livevox.multiplier.domain.exceptions.MissingFieldsException;
import com.livevox.multiplier.domain.exceptions.UnauthorizedException;
import com.livevox.multiplier.services.LvClientRestService;
import com.livevox.multiplier.services.impl.LvClientRestServiceImpl;
import com.livevox.multiplier.services.LvStandardReportsRestService;
import com.livevox.multiplier.services.impl.LvStandardReportsRestServiceImpl;
import com.livevox.multiplier.domain.lvapi.CallRecording;
import com.livevox.multiplier.domain.JobRequest;
import lombok.extern.slf4j.Slf4j;

import jakarta.xml.ws.WebServiceException;
import java.io.Serializable;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Callable;


@Slf4j
public class CallRecordingLookupJob extends AbstractConfigWrapper<LvStandardReportsRestService>
        implements Callable<List<CallRecording>>,  ClientId, Serializable {

    private static final long serialVersionUID = 23599672699L;

    private LvClientRestServiceImpl clientSrvc;


    public CallRecordingLookupJob(JobRequest req) throws Exception {
        super(req, LvStandardReportsRestServiceImpl.class);
        //
        //   This job uses more than one service, so the second one is
        //   created here and populated using the same parrent logic.
        //
        clientSrvc = (LvClientRestServiceImpl) generateService(
                LvClientRestServiceImpl.class);
        populateClientSpecificConfig(getClientDetails(), clientSrvc,
                LvClientRestServiceImpl.class);
    }

    @Override
    public Integer getClientId() {
        if(req == null) {
            return null;
        }
        return req.getClientId();
    }



    @Override
    public void setClientId(Integer clientId) {
        if(req != null) {
            req.setClientId(clientId);
        }
    }

    @Override
    public List<CallRecording> call()  throws UnauthorizedException,
            MissingFieldsException, InvalidParametersException, Exception {
        log.debug("START CallRecordingLookupJob.call() ");
        if (req == null  ) {
            log.error("Can not lookup recording.   The request is invalid. ");
            throw new MissingFieldsException();
        }
        if (getService() == null ) {
            log.error("Can not lookup recording.  The request is invalid. ");
            throw new IllegalStateException("Service configuration is invalid");
        }
        try {
            double callStart = System.currentTimeMillis();
            CallRecordingRequest correctedReq = makeDateValuesFitClientsDataRetention(
                    req.toCallRecordingRequest(), clientSrvc );
            if(correctedReq == null) {
                log.warn("A request for call recordings was made ");
            }
            List<CallRecording> recordingList = getService().getRecordings(correctedReq);
            log.warn(" Got "+ ( (recordingList == null) ? 0 : recordingList.size() ) +
                    " recordings clientId: "+req.getClientId()+".   dur=" +
                    ((System.currentTimeMillis() - callStart) / 1000));
            log.debug("END CallRecordingLookupJob.call() ");
            return recordingList;
        } catch (InvalidParametersException e) {
            throw e;
        } catch (WebServiceException wsExp) {
            if(wsExp.getCause() != null && wsExp.getCause() instanceof UnauthorizedException) {
                log.error("Getting the call recording list failed for clientId: "+req.getClientId()+
                        " the token was not autorized.");
                throw new UnauthorizedException("Getting the call recording list failed for clientId: "+
                        req.getClientId()+"  the token was not autorized.");
            }
            log.error("Getting the call recording list failed for clientId: "+req.getClientId(), wsExp);
            throw wsExp;
        }catch (Exception t) {
            log.error("Getting the call recording list failed for clientId: "+req.getClientId(), t );
            throw t;
        }
    }

    /**
     *   You might want to have a drink before trying to follow this....
     *
     *   So since this is part of the shared services logic where this request is possibly being made
     *   for multiple different clients, AND different clients retain their data for different amounts
     *   of time it is important that we not allow a requested date range that is outside of the client's
     *   window of retention.   Otherwise the call recording lookup call will fail even though they
     *   might actually have valid data to return.
     *
     *
     * @param req
     * @return
     */
    private CallRecordingRequest makeDateValuesFitClientsDataRetention(CallRecordingRequest req,
                                                                       LvClientRestService clientDetailsService) throws InvalidParametersException {
        if(req == null || clientDetailsService == null) {
            return null;
        }
        ClientInternalDetails clientSettings = clientDetailsService.getClientInternalDetails(
                req.getClientId(), req.getApiSessionId());
        if(clientSettings == null || clientSettings.getCallRecordingDays() == null) {
            log.warn("The client internal deatils platform API call failed for client: "+
                    req.getClientId()+ " so its search date rangs can NOT be fixed if the " +
                    "are outside the allowed range.");
            return req;
        }
        Calendar maxStartDate = Calendar.getInstance();
        maxStartDate.add(Calendar.DAY_OF_MONTH, -1 * clientSettings.getCallRecordingDays()    );
        //
        //  If even the end date is outside the window then they are searching for data that does not
        //   exist.   So nothing to fix and the call should NOT be made.
        //
        if(req.getEndDate().before(maxStartDate.getTime()) ) {
            throw new InvalidParametersException(
                    "Invalid date range.  Searches should not be more than "+
                            clientSettings.getCallRecordingDays()+" days from today.");
        }
        if(req.getStartDate().before(maxStartDate.getTime()) ) {
            req.setStartDate(maxStartDate.getTime());
        }

        return req;
    }
}
