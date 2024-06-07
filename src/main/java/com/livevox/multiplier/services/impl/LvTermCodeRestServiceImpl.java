package com.livevox.multiplier.services.impl;

import com.livevox.multiplier.domain.exceptions.UnauthorizedException;
import com.livevox.multiplier.domain.requests.ListRestResponse;
import com.livevox.integration.commons.domain.stats.TermCode;
import com.livevox.multiplier.services.config.AtgConfigService;
import com.livevox.multiplier.services.LvTermCodeRestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.xml.ws.WebServiceException;
import java.io.Serializable;
import java.util.List;

@Service
public class LvTermCodeRestServiceImpl extends LvSessionRestServiceImpl implements LvTermCodeRestService, Serializable {
    private static final long serialVersionUID = 2706991485294371287L;

    public static final String URI_PREFIX = "/configuration/";

    public static final String URI_SUFFIX = "/termCodes";

    @Autowired
    public LvTermCodeRestServiceImpl(AtgConfigService atgConfigService) {
        super(atgConfigService);
    }

    public List<TermCode> getTermCodeList(Long skillId, Integer count, Integer offset, Integer clientId) {
        return this.getTermCodeList(skillId, count, offset, clientId, false, null, null);
    }

    public List<TermCode> getTermCodeList(Long skillId, Integer count, Integer offset, Integer clientId, String apiSessionId) {
        return this.getTermCodeList(skillId, count, offset, clientId, false, apiSessionId, null);
    }

    public List<TermCode> getTermCodeList(Long skillId, Integer count, Integer offset, Integer clientId, String apiSessionId, String apiVersion) {
        return this.getTermCodeList(skillId, count, offset, clientId, false, apiSessionId, apiVersion);
    }

    public List<TermCode> getAllTermCodes(Long skillId, Integer clientId, String apiSessionId) {
        return this.getTermCodeList(skillId, 1000, 0, clientId, true, apiSessionId, null);
    }

    public List<TermCode> getAllTermCodes(Long skillId, Integer clientId, String apiSessionId, String apiVersion) {
        return this.getTermCodeList(skillId, 1000, 0, clientId, true, apiSessionId, apiVersion);
    }

    public List<TermCode> getTermCodeList(Long skillId, Integer count, Integer offset, Integer clientId, boolean returnAll, String apiSessionId, String apiVersion) {
        log.debug("Start getTermCodeList() ");
        if (clientId != null && count != null && offset != null) {
            ListRestResponse resp;

            try {
                UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(this.getURI(URI_PREFIX,  URI_SUFFIX, apiVersion, clientId));
                builder.queryParam("count", count);
                builder.queryParam("offset", offset);
                builder.queryParam("client", clientId);
                if (skillId != null) {
                    builder.queryParam("service", skillId);
                }
                resp = this.makeLvCall(builder.toUriString(), HttpMethod.GET, null, ListRestResponse.class, clientId, apiSessionId);


            } catch (UnauthorizedException var10) {
                throw new UnauthorizedException("Could not recover from auth failure." + var10);
            } catch (Exception var11) {
                log.error("List term codes failed. ", var11);
                throw new WebServiceException("List term codes failed. ");
            }

            if (resp == null) {
                log.error("Unable to term codes. The response was invalid. ");
                throw new WebServiceException("List term codes failed. The response was invalid ");
            } else {
                if (returnAll && resp.isHasMore()) {
                    List<TermCode> more = this.getTermCodeList(skillId, count, offset + count, clientId, true, apiSessionId, apiVersion);
                    if (more != null) {
                        resp.getTermCode().addAll(more);
                    }
                }

                log.debug("End getTermCodeList() ");
                return resp.getTermCode();
            }
        } else {
            throw new WebServiceException(" Required value was null ");
        }
    }
}

