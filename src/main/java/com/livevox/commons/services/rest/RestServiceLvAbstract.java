/*
 * Copyright (c) 2020. Luis Felipe Sosa Alvarez. All rights reserved to LiveVox.
 * Use this subject to license terms.
 *
 * IS-CALL-MULTIPLIER
 */

package com.livevox.commons.services.rest;

import com.livevox.commons.domain.ClientInfo;
import com.livevox.commons.domain.LvSession;
import com.livevox.commons.services.config.AtgConfigService;
import com.livevox.commons.services.signin.SignInService;
import jakarta.xml.ws.WebServiceException;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import java.util.*;

public abstract class RestServiceLvAbstract extends RestServiceAbstract {
    private static final long serialVersionUID = 60807610181956393L;
    public static final String HTTP_HEADER_AUTH_NAME = "LV-Session";
    public static final String HTTP_ACCESS_TOKEN_NAME = "LV-Access";
    public static final String HTTP_HEADER_AUTH_VALUE_PREFIX = "";
    public static final String HTTP_ACCESS_TOKEN_VALUE_PREFIX = "";
    public static final String API_PROPERTY_PREFIX = "com.livevox.commons.services.rest.";
    protected String version;
    protected String lvAuthToken;
    protected AtgConfigService atgConfigService;
    protected int minutesToForceCacheRefresh;

    @Setter
    protected SignInService signInService;

    protected RestServiceLvAbstract() {
        this.minutesToForceCacheRefresh = 720;
        this.setVersion("v5.0");
        this.setHostName("https://api.livevox.com");
    }

    protected HttpComponentsClientHttpRequestFactory requestFactory() {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setReadTimeout(300000);
        factory.setConnectTimeout(300000);
        factory.setConnectionRequestTimeout(300000);
        return factory;
    }

    protected RestServiceLvAbstract(AtgConfigService atgConfigService) {
        this();
        this.atgConfigService = atgConfigService;
        if (atgConfigService == null) {
            log.warn("The config client was NULL so config values where not set.");
        } else {
            String prop = atgConfigService.getProperty("com.livevox.commons.services.rest.version");
            if (!StringUtils.isEmpty(prop)) {
                this.version = prop;
            }

            prop = atgConfigService.getProperty("com.livevox.commons.services.rest.host");
            if (!StringUtils.isEmpty(prop)) {
                this.setHostName(prop);
            }

            prop = atgConfigService.getProperty("com.livevox.commons.services.rest.authToken");
            if (!StringUtils.isEmpty(prop)) {
                this.setLvAuthToken(prop);
            }

            prop = atgConfigService.getProperty("com.livevox.commons.services.rest.timeOut");
            if (!StringUtils.isEmpty(prop)) {
                Integer timeOutVal = new Integer(prop);
                HttpComponentsClientHttpRequestFactory factory = this.requestFactory();
                factory.setReadTimeout(timeOutVal);
                factory.setConnectTimeout(timeOutVal);
                factory.setConnectionRequestTimeout(timeOutVal);
                this.getRestTemplate().setRequestFactory(factory);
            }

            prop = atgConfigService.getProperty("com.livevox.commons.services.rest.minutesToForceCacheRefresh");
            if (!StringUtils.isEmpty(prop)) {
                this.minutesToForceCacheRefresh = new Integer(prop);
            } else {
                this.minutesToForceCacheRefresh = 720;
            }

        }
    }

    protected RestServiceLvAbstract(AtgConfigService atgConfigService, SignInService signInService) {
        this(atgConfigService);
        this.signInService = signInService;
        if (signInService == null) {
            log.warn("The signIn Service client was NULL.");
        }
    }

    protected synchronized Map<String, List<String>> createHeaderMap(String apiSessionId, String lvAuthTokenOverride, Optional<? extends LvSession> client) throws WebServiceException {
        log.debug("Start createHeaderMap() ");
        Map<String, List<String>> headers = new HashMap();
        String specificAuthToken = this.lvAuthToken;
        if (!StringUtils.isEmpty(lvAuthTokenOverride)) {
            log.info("Using provided lvAuthToken");
            specificAuthToken = lvAuthTokenOverride;
        } else if (client.isPresent()) {
            if (!StringUtils.isEmpty(((LvSession)client.get()).getApiToken())) {
                specificAuthToken = ((LvSession)client.get()).getApiToken();
            } else {
                log.info("The global auth token is being used because a client specific auth token was not found for clientId: " + ((LvSession)client.get()).getClientId());
            }
        }

        if (StringUtils.isEmpty(this.lvAuthToken)) {
            throw new WebServiceException("Unable to authenticate.  Access token was not provided in property com.livevox.commons.services.rest.authToken");
        } else {
            if (apiSessionId != null) {
                headers.put("LV-Session", Arrays.asList("" + apiSessionId));
            }

            headers.put("LV-Access", Arrays.asList("" + specificAuthToken));
            log.debug("End createHeaderMap() ");
            return headers;
        }
    }

    public String getVersion() {
        return this.getVersion((Integer)null, (String)null, (String)null);
    }

    public String getVersion(String versionOverride) {
        return this.getVersion((Integer)null, versionOverride, (String)null);
    }

    public String getVersion(Integer clientId) {
        return this.getVersion(clientId, (String)null, (String)null);
    }

    public String getVersion(Integer clientId, String versionOverride) {
        return this.getVersion(clientId, versionOverride, (String)null);
    }

    public String getVersion(Integer clientId, String versionOverride, String clientName) {
        if (!StringUtils.isBlank(versionOverride)) {
            return versionOverride;
        } else {
            Optional info;
            if (this.atgConfigService != null && clientId != null) {
                info = this.atgConfigService.getClientApiVersion(clientId);
                if (info.isPresent()) {
                    return (String)info.get();
                }
            } else if (this.atgConfigService != null && clientName != null) {
                info = this.atgConfigService.getClient(clientName);
                if (info.isPresent() && !StringUtils.isBlank(((ClientInfo)info.get()).getApiVersion())) {
                    return ((ClientInfo)info.get()).getApiVersion();
                }
            }

            return this.version;
        }
    }

    protected synchronized String getSessionId(Optional<? extends LvSession> client, String apiSessionId) {
        log.debug("Start getSessionId() ");
        if (!StringUtils.isBlank(apiSessionId)) {
            log.debug("Returning provided apiSessionId");
            return apiSessionId;
        } else if (client.isPresent()) {
            LvSession selectedHeader = (LvSession)client.get();
            this.refreshExpiredSessionSignIn(selectedHeader);
            log.debug("End getSessionId() ");
            return selectedHeader.getSessionId();
        } else {
            throw new IllegalStateException("Client was not found and no apiSessionId was provided");
        }
    }

    protected String getHostName(Optional<? extends LvSession> client, String hostNameOverride) {
        log.debug("Start getHostname() ");
        if (!StringUtils.isEmpty(hostNameOverride)) {
            return hostNameOverride;
        } else {
            if (client.isPresent()) {
                if (!StringUtils.isEmpty(((LvSession)client.get()).getApiHost())) {
                    return ((LvSession)client.get()).getApiHost();
                }

                log.info("The global api host is being used because a client specific auth host was not found for clientId: " + ((LvSession)client.get()).getClientId());
            }

            log.debug("Returning default host name ");
            return this.getHostName();
        }
    }

    public String getURI(String prefix, String suffix) {
        return this.getURI(prefix, suffix, (String)null, (Integer)null, (String)null);
    }

    public String getURI(String prefix, String suffix, String versionOverRide) {
        return this.getURI(prefix, suffix, versionOverRide, (Integer)null, (String)null);
    }

    public String getURI(String prefix, String suffix, String versionOverRide, String clientName) {
        return this.getURI(prefix, suffix, versionOverRide, (Integer)null, clientName);
    }

    public String getURI(String prefix, String suffix, String versionOverRide, Integer clientId) {
        return this.getURI(prefix, suffix, versionOverRide, clientId, (String)null);
    }

    public String getURI(String prefix, String suffix, Integer clientId) {
        return this.getURI(prefix, suffix, (String)null, clientId, (String)null);
    }

    public String getURI(String prefix, String suffix, String versionOverRide, Integer clientId, String clientName) {
        StringBuilder uri = new StringBuilder();
        if (!StringUtils.isBlank(prefix)) {
            uri.append(prefix);
        }

        String finalVersion = this.getVersion(clientId, versionOverRide, clientName);
        if (StringUtils.equalsAny(finalVersion, new CharSequence[]{"v6.0", "v5.0", "v3.5", "v3.4", "v3.3", "v3.2", "v3.1", "v3.0"})) {
            uri.append(finalVersion);
        }

        if (!StringUtils.isBlank(suffix)) {
            uri.append(suffix);
        }

        return uri.toString();
    }

    protected Void makeLvCall(String uri, HttpMethod callType, Integer clientId, String lvSessionOverride) {
        return (Void)this.makeLvCall(uri, callType, (Object)null, Void.class, clientId, (String)null, lvSessionOverride);
    }

    protected <T> T makeLvCall(String uri, HttpMethod callType, Object requestObj, Class<T> responseType, Integer clientId) {
        return this.makeLvCall(uri, callType, requestObj, responseType, clientId, (String)null, (String)null);
    }

    protected <T> T makeLvCall(String uri, HttpMethod callType, Object requestObj, Class<T> responseType, Integer clientId, String lvSessionOverride) {
        return this.makeLvCall(uri, callType, requestObj, responseType, clientId, (String)null, lvSessionOverride);
    }

    protected <T> T makeLvCall(String uri, HttpMethod callType, Object requestObj, Class<T> responseType, Integer clientId, String clientName, String lvSessionOverride) {
        return this.makeLvCall(uri, callType, requestObj, responseType, (MediaType)null, clientId, clientName, lvSessionOverride);
    }

    protected <T> T makeLvCall(String uri, HttpMethod callType, Object requestObj, Class<T> responseType, MediaType acceptMediaType, Integer clientId, String clientName, String lvSessionOverride) {
        return this.makeLvCall(uri, callType, requestObj, responseType, acceptMediaType, clientId, clientName, lvSessionOverride, (String)null, (String)null);
    }

    protected <T> T makeLvCall(String uri, HttpMethod callType, Object requestObj, Class<T> responseType, MediaType acceptMediaType, Integer clientId, String clientName, String lvSessionOverride, String apiHostOverride, String apiLvTokenOverride) {
        log.debug("START makeLvCall()");
        HttpHeaders httpHeader = this.getHeaders(acceptMediaType, (Map)null);
        String reqTrackingId = this.addLVTrackingHeader(httpHeader);
        Optional<ClientInfo> client = this.atgConfigService.getClient(clientId, clientName);
        if (!client.isPresent()) {
            log.warn("Unable to find matching client for clientId " + clientId + " or clientName " + clientName);
        }

        httpHeader.putAll(this.createHeaderMap(this.getSessionId(client, lvSessionOverride), apiLvTokenOverride, client));
        log.debug("httpHeader: "+httpHeader.toString());
        HttpEntity entity;
        if (requestObj != null) {
            entity = new HttpEntity(requestObj, httpHeader);
        } else {
            entity = new HttpEntity(httpHeader);
        }

        ResponseEntity<T> resp = this.getRestTemplate().exchange(this.getFullUrl(uri, client, apiHostOverride), callType, entity, responseType, new Object[0]);
        log.debug("Response  [" + reqTrackingId + "] : " + resp);
        log.debug("END makeLvCall()");
        return resp.getBody();
    }

    protected <T> T makeLoginCall(String uri, HttpMethod callType, Object requestObj, Class<T> responseType, String clientName, String hostNameOverride, String lvAuthTokenOverride) {
        log.debug("START makeLvCall()");
        HttpHeaders httpHeader = this.getHeaders();
        String reqTrackingId = this.addLVTrackingHeader(httpHeader);
        Optional<ClientInfo> client = this.atgConfigService.getClient((Integer)null, clientName);
        if (!client.isPresent()) {
            log.warn("Unable to find matching client for clientName " + clientName);
        }

        httpHeader.putAll(this.createHeaderMap((String)null, lvAuthTokenOverride, client));
        HttpEntity entity;
        if (requestObj != null) {
            entity = new HttpEntity(requestObj, httpHeader);
        } else {
            entity = new HttpEntity(httpHeader);
        }

        ResponseEntity<T> resp = this.getRestTemplate().exchange(this.getFullUrl(uri, client, hostNameOverride), callType, entity, responseType, new Object[0]);
        log.debug("Response  [" + reqTrackingId + "] : " + resp);
        log.debug("END makeLvCall()");
        return resp.getBody();
    }

    protected <T> T makeLoginCall(String uri, HttpMethod callType, Object requestObj, Class<T> responseType, String clientName, String apiSessionId, String hostNameOverride, String lvAuthTokenOverride) {
        log.debug("START makeLvCall()");
        HttpHeaders httpHeader = this.getHeaders();
        String reqTrackingId = this.addLVTrackingHeader(httpHeader);
        Optional<ClientInfo> client = this.atgConfigService.getClient((Integer)null, clientName);
        if (!client.isPresent()) {
            log.warn("Unable to find matching client for clientName " + clientName);
        }

        httpHeader.putAll(this.createHeaderMap(this.getSessionId(client, apiSessionId), lvAuthTokenOverride, client));
        HttpEntity entity;
        if (requestObj != null) {
            entity = new HttpEntity(requestObj, httpHeader);
        } else {
            entity = new HttpEntity(httpHeader);
        }

        ResponseEntity<T> resp = this.getRestTemplate().exchange(this.getFullUrl(uri, client, hostNameOverride), callType, entity, responseType, new Object[0]);
        log.debug("Response  [" + reqTrackingId + "] : " + resp);
        log.debug("END makeLvCall()");
        return resp.getBody();
    }

    protected String getFullUrl(String uri, Optional<? extends LvSession> client, String hostNameOverride) {
        if (StringUtils.isEmpty(uri)) {
            return null;
        } else {
            String hostName = this.getHostName(client, hostNameOverride);
            if (StringUtils.isEmpty(hostName)) {
                return null;
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append(hostName);
                if (!hostName.endsWith("/") && !uri.startsWith("/")) {
                    sb.append("/");
                }

                sb.append(uri.replaceAll("\\/\\/", "/"));
                log.debug("Making call with URL: " + sb.toString());
                return sb.toString();
            }
        }
    }

    public abstract void refreshExpiredSession(LvSession var1) throws WebServiceException;

    public abstract void refreshExpiredSessionSignIn(LvSession var1) throws WebServiceException;

    public void setVersion(String version) {
        this.version = version;
    }

    public String getLvAuthToken() {
        return this.lvAuthToken;
    }

    public void setLvAuthToken(String lvAuthToken) {
        this.lvAuthToken = lvAuthToken;
    }

    public void setAtgConfigService(AtgConfigService atgConfigService) {
        this.atgConfigService = atgConfigService;
    }

    public int getMinutesToForceCacheRefresh() {
        return this.minutesToForceCacheRefresh;
    }

    public void setMinutesToForceCacheRefresh(int minutesToForceCacheRefresh) {
        this.minutesToForceCacheRefresh = minutesToForceCacheRefresh;
    }
}
