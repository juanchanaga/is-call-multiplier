package com.livevox.multiplier.jobs;

import com.livevox.multiplier.domain.exceptions.UnauthorizedException;
import com.livevox.multiplier.services.impl.LvCampaignRestServiceImpl;
import com.livevox.multiplier.domain.lvapi.Campaign;
import com.livevox.multiplier.domain.CampaignResponse;
import com.livevox.multiplier.domain.JobRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;

import jakarta.xml.ws.WebServiceException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

@Slf4j
public class CampaignLookupJob extends AbstractConfigWrapper<LvCampaignRestServiceImpl>
        implements Callable<List<CampaignResponse>>, ClientId, Serializable {

    private static final long serialVersionUID = 3598799672699L;



    public CampaignLookupJob(JobRequest jbReq ) throws Exception {
        super( jbReq, LvCampaignRestServiceImpl.class);
    }



    @Override
    public List<CampaignResponse> call()  throws UnauthorizedException,
            DataIntegrityViolationException, Exception {
        log.debug("START CampaignLookupJob.call() ");
        if (getReq() == null || getService() == null ) {
            log.error("Can not lookup campagin.   The request is invalid. ");
            throw new DataIntegrityViolationException("Missing required fields.");
        }
        try {
            double callStart = System.currentTimeMillis();
            List<CampaignResponse> respList = new ArrayList<>();
            List<Campaign> recordingList = getService().FindMatchingCampaigns(
                    getReq().getClientId(), getReq().getStartDate(), getReq().getEndDate(),
                    getReq().getCount(), getReq().getOffset(), null, null,
                    null, getReq().getSessoinId());
            log.warn(" Got "+ ( (recordingList == null) ? 0 : recordingList.size() ) +
                    " campagins for clientId: "+getReq().getClientId()+".   dur=" +
                    ((System.currentTimeMillis() - callStart) / 1000));
            if(recordingList != null) {
                for(Campaign cmpn : recordingList) {
                    CampaignResponse newCmpnResp = new CampaignResponse(cmpn);
                    newCmpnResp.setClientId(req.getClientId());
                    respList.add(newCmpnResp);
                }
            }
            log.debug("END CampaignLookupJob.call() ");
            return respList;
        } catch (WebServiceException wsExp) {
            if(wsExp.getCause() != null && wsExp.getCause() instanceof UnauthorizedException) {
                log.error("Getting the campaign list failed for clientId: "+getReq().getClientId()+
                        " the token was not autorized.");
                throw new UnauthorizedException("Getting the campaign list failed for clientId: "+
                        getReq().getClientId()+"  the token was not autorized.");
            }
            log.error("Getting the campaign list failed for clientId: "+getReq().getClientId(), wsExp);
            throw wsExp;
        }catch (Exception t) {
            log.error("Getting the campaign list failed for clientId: "+getReq().getClientId(), t );
            throw t;
        }
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

}

