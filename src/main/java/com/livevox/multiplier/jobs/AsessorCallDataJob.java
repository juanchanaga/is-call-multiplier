package com.livevox.multiplier.jobs;

import com.livevox.multiplier.domain.exceptions.MissingFieldsException;
import com.livevox.multiplier.domain.exceptions.UnauthorizedException;
import com.livevox.multiplier.domain.AssessorData;
import com.livevox.multiplier.domain.AssessorRequest;
import com.livevox.multiplier.services.RestServiceAbstract;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpMethod;

import jakarta.xml.ws.WebServiceException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class AsessorCallDataJob extends RestServiceAbstract implements Callable<List<AssessorData>>, Serializable {

    private static final long serialVersionUID = 8794689672699L;

    private AssessorRequest req;

    public AsessorCallDataJob(AssessorRequest req, String host ) {
        this.req = req;
        setHostName(host);
    }

    @Override
    public List<AssessorData> call()  throws UnauthorizedException, DataIntegrityViolationException, Exception {
        log.debug("START AsessorCallDataJob.call() ");
        if (req == null ) {
            log.error("Can not lookup assessor data.   The request is invalid. ");
            throw new MissingFieldsException();
        }
        try {
            AssessorRequest newReq = req.copy();
            newReq.setClientId(null);
            double callStart = System.currentTimeMillis();
            ParameterizedTypeReference<List<AssessorData>> returnTypeRef =
                    new ParameterizedTypeReference<List<AssessorData>>() {};

            /*ResponseEntity<List<AssessorData>> resp =  request()
                    .uri("/assessor/call/listener/search")
                    .post()
                    .body(newReq)
                    .acceptMimeType(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .exchange(returnTypeRef);*/

            List<AssessorData> resp = makeCall("/assessor/call/listener/search",
            		HttpMethod.POST, newReq, returnTypeRef, getHeaders(req.getToken()) );

            log.warn(" Got "+ ( (resp == null) ? 0 : resp.size() ) +" agents for clientId: "+
                    req.getClientId()+".   dur=" + ((System.currentTimeMillis() - callStart) / 1000));
            log.debug("END AsessorCallDataJob.call() ");
            return resp;
        } catch (WebServiceException wsExp) {
            if(wsExp.getCause() != null && wsExp.getCause() instanceof UnauthorizedException) {
                log.error("Getting the assessor list failed. ");
                throw new UnauthorizedException("Getting the assessor list failed.");
            }
            log.error("Getting the assessor list failed. ", wsExp);
            throw wsExp;
        }catch (Exception t) {
            log.error("Getting the assessor list failed.", t );
            throw t;
        }
    }


    private Map<String, List<String>> getHeaders(String apiSessionId) {
        HashMap<String, List<String>> hdrs = new HashMap<String, List<String>>();
        List<String> vals = new ArrayList<>();
        vals.add(apiSessionId);
        hdrs.put("LV-Session", vals);
        return hdrs;
    }

}
