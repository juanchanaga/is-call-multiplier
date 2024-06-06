/*
 * Copyright (c) 2021. Luis Felipe Sosa Alvarez. All rights reserved to LiveVox.
 * Use this subject to license terms.
 *
 * IS-CALL-MULTIPLIER
 */

package com.livevox.commons.services.rest.configuration;

import com.livevox.commons.domain.SkillDetails;
import com.livevox.commons.domain.SkillPhoneSettings;
import com.livevox.commons.domain.SkillRelatedDetails;
import com.livevox.commons.domain.SkillSettings;
import com.livevox.commons.services.config.AtgConfigService;
import com.livevox.commons.services.rest.domain.ListRestResponse;
import com.livevox.commons.services.rest.session.LvSessionRestServiceImpl;
import com.livevox.integration.commons.domain.stats.Skill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.xml.ws.WebServiceException;
import java.io.Serializable;
import java.util.List;

@Service
public class LvServiceRestServiceImpl extends LvSessionRestServiceImpl
        implements LvServiceRestService, Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -8660652166408246244L;

    public static final String URI_BASE_PREFIX = "/configuration/";

    public static final String URI_BASE_SUFFIX = "/services";

    @Autowired
    public LvServiceRestServiceImpl(final AtgConfigService atgConfigService) {
        super(atgConfigService);
    }

    @Override
    public List<Skill> getSkills(final Integer callCenterId, final Integer clientId, final Integer count,
                                 final Integer offset) throws WebServiceException {
        return getSkills(callCenterId, clientId, null, count, offset, false, null);
    }

    @Override
    public List<Skill> getSkills(final Integer callCenterId, final Integer clientId, final Integer count,
                                 final Integer offset, final boolean returnAll) throws WebServiceException {
        return getSkills(callCenterId, clientId, null, count, offset, returnAll, null);
    }

    @Override
    public List<Skill> getSkills(Integer callCenterId, Integer clientId, String apiSessionId, Integer count,
                                 Integer offset, boolean returnAll) throws WebServiceException {
        return getSkills(callCenterId, clientId, apiSessionId, count, offset, returnAll, null);
    }

    @Override
    public List<Skill> getSkills(Integer callCenterId, Integer clientId, String apiSessionId, Integer count,
                                 Integer offset, boolean returnAll, String apiVersion) throws WebServiceException {
        log.debug("Start getSkills() for callCenter " + callCenterId + " clientId " + clientId);
        if (clientId == null || count == null || offset == null) {
            throw new WebServiceException(" Required value was null ");
        }
        ListRestResponse resp = null;
        try {
            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromUriString(getURI(URI_BASE_PREFIX, URI_BASE_SUFFIX, apiVersion, clientId));
            builder.queryParam("callCenter", callCenterId);
            builder.queryParam("offset", offset);
            builder.queryParam("count", count);
            resp = makeLvCall(builder.toUriString(), HttpMethod.GET, null, ListRestResponse.class, clientId, null);
        } catch (WebServiceException wsExp) {
            throw new WebServiceException("Could not recover from auth failure." + wsExp, wsExp);
        } catch (Exception e) {
            log.error("List services failed for client " + clientId + " call center " + callCenterId, e);
            throw new WebServiceException("List services failed for client" + clientId + "call center " + callCenterId);
        }

        if (resp == null) {
            log.error("Unable to retrieve services. The response was invalid. ");
            throw new WebServiceException("List services failed. The response was invalid ");
        }

        if (returnAll && resp.isHasMore()) {
            List<Skill> more = getSkills(callCenterId, clientId, count, offset + count, returnAll);
            if (more != null) {
                resp.getService()
                        .addAll(more);
            }
        }

        log.debug("End getSkills() ");
        return resp.getService();
    }

    @Override
    public List<Skill> getAllSkills(Integer callCenterId, Integer clientId) throws WebServiceException {
        return getAllSkills(callCenterId, clientId, null);
    }

    @Override
    public List<Skill> getAllSkills(Integer callCenterId, Integer clientId, String apiSessionId)
            throws WebServiceException {
        return getSkills(callCenterId, clientId, apiSessionId, 1000, 0, true, null);
    }

    @Override
    public List<Skill> getAllSkills(Integer callCenterId, Integer clientId, String apiSessionId, String apiVersion)
            throws WebServiceException {
        return getSkills(callCenterId, clientId, apiSessionId, 1000, 0, true, apiVersion);
    }

    @Override
    public SkillRelatedDetails getSkillRelatedDetails(Integer skillId, Integer clientId, String apiSessionId)
            throws WebServiceException {
        return getSkillRelatedDetails(skillId, clientId, apiSessionId, null);
    }

    @Override
    public SkillRelatedDetails getSkillRelatedDetails(Integer skillId, Integer clientId) throws WebServiceException {
        return getSkillRelatedDetails(skillId, clientId, null, null);
    }

    @Override
    public SkillRelatedDetails getSkillRelatedDetails(Integer skillId, Integer clientId, String apiSessionId,
                                                      String apiVersion) throws WebServiceException {
        log.debug("Start getSkillRelatedDetails() ");
        if (clientId == null || skillId == null) {
            throw new WebServiceException(" Required value was null ");
        }
        SkillRelatedDetails resp = null;
        try {
            resp = makeLvCall(getURI(URI_BASE_PREFIX, URI_BASE_SUFFIX + "/" + skillId + "/related", apiVersion, clientId),
                    HttpMethod.GET, null, SkillRelatedDetails.class, clientId, apiSessionId);
        } catch (WebServiceException wsExp) {
            throw new WebServiceException("Could not recover from auth failure." + wsExp, wsExp);
        } catch (Exception e) {
            log.error("Get skill related details failed. ", e);
            throw new WebServiceException("Get skill related details failed. ");
        }

        if (resp == null) {
            log.error("Get skill related details failed. The response was invalid. ");
            throw new WebServiceException("Get skill related details failed. The response was invalid ");
        }
        log.debug("End getSkillRelatedDetails() ");
        return resp;
    }

    @Override
    public SkillDetails getSkillGeneralProperties(Integer skillId, Integer clientId) throws WebServiceException {
        return getSkillGeneralProperties(skillId, clientId, null, null);
    }

    @Override
    public SkillDetails getSkillGeneralProperties(Integer skillId, Integer clientId, String apiSessionId)
            throws WebServiceException {
        return getSkillGeneralProperties(skillId, clientId, apiSessionId, null);
    }

    @Override
    public SkillDetails getSkillGeneralProperties(Integer skillId, Integer clientId, String apiSessionId,
                                                  String apiVersion) throws WebServiceException {
        log.debug("Start getSkillGeneralProperties() ");
        if (clientId == null || skillId == null) {
            throw new WebServiceException(" Required value was null ");
        }
        SkillDetails resp = null;
        try {
            resp = makeLvCall(getURI(URI_BASE_PREFIX, URI_BASE_SUFFIX + "/" + skillId, apiVersion, clientId),
                    HttpMethod.GET, null, SkillDetails.class, clientId, apiSessionId);
        } catch (WebServiceException wsExp) {
            throw new WebServiceException("Could not recover from auth failure." + wsExp, wsExp);
        } catch (Exception e) {
            log.error("Get skill general properties failed. ", e);
            throw new WebServiceException("Get skill general properties failed. ");
        }

        if (resp == null) {
            log.error("Get skill general properties failed. The response was invalid. ");
            throw new WebServiceException("Get skill general properties failed. The response was invalid ");
        }
        log.debug("End getSkillGeneralProperties() ");
        return resp;
    }

    @Override
    public SkillPhoneSettings getSkillPhoneSettings(Integer skillId, Integer clientId) throws WebServiceException {
        return getSkillPhoneSettings(skillId, clientId, null, null);
    }

    @Override
    public SkillPhoneSettings getSkillPhoneSettings(Integer skillId, Integer clientId, String apiSessionId)
            throws WebServiceException {
        return getSkillPhoneSettings(skillId, clientId, apiSessionId, null);
    }

    @Override
    public SkillPhoneSettings getSkillPhoneSettings(Integer skillId, Integer clientId, String apiSessionId,
                                                    String apiVersion) throws WebServiceException {
        log.debug("Start getSkillPhoneSettings() ");
        if (clientId == null || skillId == null) {
            throw new WebServiceException(" Required value was null ");
        }
        SkillPhoneSettings resp = null;
        try {
            resp = makeLvCall(getURI(URI_BASE_PREFIX, URI_BASE_SUFFIX + "/" + skillId + "/phone", apiVersion, clientId),
                    HttpMethod.GET, null, SkillPhoneSettings.class, clientId, apiSessionId);
        } catch (WebServiceException wsExp) {
            throw new WebServiceException("Could not recover from auth failure." + wsExp, wsExp);
        } catch (Exception e) {
            log.error("Get skill phone settings failed.", e);
            throw new WebServiceException("Get skill phone settings failed.");
        }

        if (resp == null) {
            log.error("Unable to retrieve skill phone settings. The response was invalid. ");
            throw new WebServiceException("Get skill phone settings failed. The response was invalid ");
        }
        log.debug("End getSkillPhoneSettings() ");
        return resp;
    }

    @Override
    public SkillSettings getSkillSettings(Integer skillId, Integer clientId) throws WebServiceException {
        return getSkillSettings(skillId, clientId, null, null);
    }

    @Override
    public SkillSettings getSkillSettings(Integer skillId, Integer clientId, String apiSessionId)
            throws WebServiceException {
        return getSkillSettings(skillId, clientId, apiSessionId, null);
    }

    @Override
    public SkillSettings getSkillSettings(Integer skillId, Integer clientId, String apiSessionId, String apiVersion)
            throws WebServiceException {
        log.debug("Start getSkillPhoneSettings() ");
        if (clientId == null || skillId == null) {
            throw new WebServiceException(" Required value was null ");
        }
        SkillSettings resp = null;
        try {
            resp = makeLvCall(
                    getURI(URI_BASE_PREFIX, URI_BASE_SUFFIX + "/" + skillId + "/settings", apiVersion, clientId),
                    HttpMethod.GET, null, SkillSettings.class, clientId, apiSessionId);
        } catch (WebServiceException wsExp) {
            throw new WebServiceException("Could not recover from auth failure." + wsExp, wsExp);
        } catch (Exception e) {
            log.error("Get skill settings failed.", e);
            throw new WebServiceException("Get skill settings failed.");
        }

        if (resp == null) {
            log.error("Unable to retrieve skill settings. The response was invalid. ");
            throw new WebServiceException("Get skill settings failed. The response was invalid ");
        }
        log.debug("End getSkillPhoneSettings() ");
        return resp;
    }

}
