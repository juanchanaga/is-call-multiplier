/*
 * Copyright (c) 2021. Luis Felipe Sosa Alvarez. All rights reserved to LiveVox.
 * Use this subject to license terms.
 *
 * IS-CALL-MULTIPLIER
 */

package com.livevox.integration.shareddata.multiplier.jobs;

import com.livevox.commons.domain.Client;;
import com.livevox.commons.exceptions.UnauthorizedException;
import com.livevox.commons.services.rest.configuration.LvCallCenterRestServiceImpl;
import com.livevox.commons.services.rest.configuration.LvClientRestServiceImpl;
import com.livevox.commons.services.rest.configuration.LvServiceRestServiceImpl;
import com.livevox.commons.services.signin.SignInService;
import com.livevox.integration.commons.domain.lvapi.CallCenter;
import com.livevox.integration.shareddata.multiplier.domain.JobRequest;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;

import jakarta.xml.ws.WebServiceException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class ConfigDetailsJob extends AbstractConfigWrapper<LvCallCenterRestServiceImpl>
        implements Callable<Client>, ClientId, Serializable {

    private static final long serialVersionUID = 57599672699L;

    protected static final Logger log = LoggerFactory.getLogger(ConfigDetailsJob.class);

    private LvServiceRestServiceImpl srvcLookUpServcie;

    @Setter
    private SignInService signInService;

    public ConfigDetailsJob(JobRequest req) throws Exception{
        super(req, LvCallCenterRestServiceImpl.class);
        //
        //   This job uses more than one service, so the second one is
        //   created here and populated using the same parrent logic.
        //
        srvcLookUpServcie = (LvServiceRestServiceImpl) generateService(
                LvServiceRestServiceImpl.class);
        populateClientSpecificConfig(getClientDetails(), srvcLookUpServcie,
                LvClientRestServiceImpl.class);
    }


    @Override
    public Client call()  throws UnauthorizedException, DataIntegrityViolationException {
        log.debug("START ConfigDetailsJob.call() ");
        if(req == null || req.getClientId() == null || getService() == null ||
                srvcLookUpServcie == null ) {
            log.error("Can not lookup config details.   The request is invalid. ");
            throw new DataIntegrityViolationException("Missing required fields or service(s) are not configured.");
        }
        srvcLookUpServcie.setSignInService(signInService);
        double callStart = System.currentTimeMillis();
        Client newClient = new Client();
        newClient.setId(req.getClientId());
        try {
            newClient.setCallCenters(getService().getAllCallCenters(req.getClientId(),
                    req.getSessoinId()) );
        } catch(Exception t) {
            if(t instanceof WebServiceException && t.getCause() != null &&
                    t.getCause() instanceof UnauthorizedException ) {
                log.error("Unauthorized or Unable to access the requested call center failed for clientId "+req.getClientId());
            } else {
                log.error("Looking up call center failed for clientId "+req.getClientId());
            }
        }
        log.info(" Getting call centers for clientId: "+req.getClientId()+"  dur=" +
                ((System.currentTimeMillis() - callStart) / 1000));
        if(newClient.getCallCenters() == null) {
            return newClient;
        }
        double serviceLookupStart = System.currentTimeMillis();
        List<CallCenter> completedCallCenters = new ArrayList<CallCenter>();
        for(CallCenter cc : newClient.getCallCenters() ) {
            try {
                cc.setServices( srvcLookUpServcie.getAllSkills(
                        cc.getCallCenterId().intValue(), req.getClientId(), req.getSessoinId()) );
                completedCallCenters.add(cc);
            } catch(Exception e) {
                log.warn("Looking up skills failed, but the error has been supressed, for callCenterId:  "+cc.getCallCenterId()+"  "+e);
            }
        }
        newClient.setCallCenters(completedCallCenters);
        log.info(" Getting services lists for clientId: "+req.getClientId()+" complete.  dur=" +
                ((System.currentTimeMillis() - serviceLookupStart) / 1000));
        log.warn(" Getting config details clientId: "+req.getClientId()+" job complete.  dur=" +
                ((System.currentTimeMillis() - callStart) / 1000));
        log.debug("END ConfigDetailsJob.call() ");
        return newClient;
    }


    @Override
    public Integer getClientId() {
        return (req == null) ? null : req.getClientId();
    }


    @Override
    public void setClientId(Integer clientId) {
        if(req == null) {
            req = new JobRequest();
        }
        req.setClientId(clientId);
    }
}
