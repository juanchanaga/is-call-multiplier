package com.livevox.is.metricsservice.jobs;

import com.livevox.is.metricsservice.controller.rest.conf.LvTermCodeRestServiceImpl;
import com.livevox.is.metricsservice.domain.Client;
import com.livevox.is.metricsservice.domain.JobRequest;
import com.livevox.is.metricsservice.exceptions.MissingFieldsException;
import com.livevox.is.metricsservice.exceptions.UnauthorizedException;
import lombok.extern.slf4j.Slf4j;

import javax.xml.ws.WebServiceException;
import java.io.Serializable;
import java.util.concurrent.Callable;

@Slf4j
public class TermCodeLookupJob extends AbstractConfigWrapper<LvTermCodeRestServiceImpl>
        implements Callable<Client>, ClientId, Serializable {


    public TermCodeLookupJob(JobRequest jobReq) throws Exception{
        super(jobReq, LvTermCodeRestServiceImpl.class);

    }

    @Override
    public Client call()  throws UnauthorizedException, MissingFieldsException {
        log.debug("START TermCodeLookupJob.call() ");
        if (getReq() == null || getReq() .getClientId() == null || getService() == null ) {
            log.error("Can not lookup termcode list.   The request is invalid. ");
            throw new MissingFieldsException();
        }
        double callStart = System.currentTimeMillis();
        Client newClient = new Client();
        newClient.setId(getReq() .getClientId());
        try {
            if (getReq().getClientId() != null ){
                newClient.setTermCodes(getService().getAllTermCodes(null,
                        getReq().getClientId(), getReq().getSessoinId()));
            }
        } catch(Exception t) {
            if(t instanceof WebServiceException && t.getCause() != null &&
                    t.getCause() instanceof UnauthorizedException ) {
                log.error("Unauthorized or Unable to access the requested term " +
                        "code failed for clientId "+getReq().getClientId());
            } else {
                log.error("Looking up term code failed for clientId "+getReq().getClientId());
            }
        }
        log.info(" Getting term code for clientId: "+getReq().getClientId()+"  dur=" +
                ((System.currentTimeMillis() - callStart) / 1000));
        if(newClient.getCallCenters() == null) {
            return newClient;
        }

        log.warn(" Getting term code details clientId: "+getReq().getClientId()+" job complete.  dur=" +
                ((System.currentTimeMillis() - callStart) / 1000));
        log.debug("END TermCodeLookupJob.call() ");
        return newClient;

    }

    @Override
    public Integer getClientId() {
        return (getReq() == null) ? null : getReq().getClientId();
    }

    @Override
    public void setClientId(Integer clientId) {
        if(getReq()  == null) {
            setReq( new JobRequest() );
        }
        getReq() .setClientId(clientId);
    }

}
