/*
 * Copyright (c) 2020. Luis Felipe Sosa Alvarez. All rights reserved to LiveVox.
 * Use this subject to license terms.
 *
 * IS-CALL-MULTIPLIER
 */

package com.livevox.commons.services.rest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.io.Serializable;
import java.util.*;

public abstract class RestServiceAbstract implements Serializable {
    private static final long serialVersionUID = -7012801110294006157L;
    public static final String LV_REQUEST_TRACKING_ID = "LV_REQUEST_TRACKING_ID";
    public static final String CONFIG_KEY_HEADER_NAME = "atg-config-key";
    protected static final Logger log = LoggerFactory.getLogger(RestServiceAbstract.class);
    protected RestTemplate restTemplate = new RestTemplate(this.requestFactory());
    private String hostName;
    private MediaType acceptMimeType;
    private String atgConfigKey = null;

    protected RestServiceAbstract() {
        this.acceptMimeType = MediaType.APPLICATION_JSON;
    }

    protected ClientHttpRequestFactory requestFactory() {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setReadTimeout(60000);
        factory.setConnectTimeout(60000);
        return factory;
    }

    protected ClientHttpRequestFactory requestFactory(int readTimeout, int connectTimout) {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setReadTimeout(readTimeout);
        factory.setConnectTimeout(connectTimout);
        factory.setConnectionRequestTimeout(connectTimout);
        return factory;
    }

    protected <T> T makeCall(String uri, HttpMethod callType, Object requestObj, Class<T> responseType, MediaType acceptMediaType, Map<String, List<String>> headers) {
        log.debug("START makeCall()");
        HttpHeaders httpHeader = this.getHeaders(acceptMediaType, (Map)null);
        String reqTrackingId = this.addLVTrackingHeader(httpHeader);
        httpHeader.putAll(headers);
        HttpEntity entity = null;
        if (requestObj != null) {
            entity = new HttpEntity(requestObj, httpHeader);
        } else {
            entity = new HttpEntity(httpHeader);
        }

        ResponseEntity<T> resp = this.getRestTemplate().exchange(this.getFullUrl(uri), callType, entity, responseType, new Object[0]);
        log.debug("Response  [" + reqTrackingId + "] : " + resp);
        log.debug("END makeCall()");
        return resp.getBody();
    }

    protected Void makeCall(String uri, HttpMethod callType, Map<String, List<String>> headers) {
        log.debug("START makeCall()");
        HttpHeaders httpHeader = this.getHeaders((MediaType)null, (Map)null);
        String reqTrackingId = this.addLVTrackingHeader(httpHeader);
        httpHeader.putAll(headers);
        HttpEntity entity = new HttpEntity(httpHeader);
        ResponseEntity<Void> resp = this.getRestTemplate().exchange(this.getFullUrl(uri), callType, entity, Void.class, new Object[0]);
        log.debug("Response: [" + reqTrackingId + "] " + resp);
        log.debug("END makeCall()");
        return (Void)resp.getBody();
    }

    protected Void makeCall(String uri, HttpMethod callType, Object requestObj, Map<String, List<String>> headers) {
        log.debug("START makeCall()");
        HttpHeaders httpHeader = this.getHeaders((MediaType)null, (Map)null);
        String reqTrackingId = this.addLVTrackingHeader(httpHeader);
        httpHeader.putAll(headers);
        HttpEntity entity = null;
        if (requestObj != null) {
            entity = new HttpEntity(requestObj, httpHeader);
        } else {
            entity = new HttpEntity(httpHeader);
        }

        ResponseEntity<Void> resp = this.getRestTemplate().exchange(this.getFullUrl(uri), callType, entity, Void.class, new Object[0]);
        log.debug("Response:  [" + reqTrackingId + "] " + resp);
        log.debug("END makeCall()");
        return (Void)resp.getBody();
    }

    protected <T> T makeCall(String uri, HttpMethod callType, Object requestObj, Class<T> responseType) {
        log.debug("START makeCall()");
        HttpHeaders httpHeader = this.getHeaders();
        String reqTrackingId = this.addLVTrackingHeader(httpHeader);
        HttpEntity entity = null;
        if (requestObj != null) {
            entity = new HttpEntity(requestObj, httpHeader);
        } else {
            entity = new HttpEntity(httpHeader);
        }

        ResponseEntity<T> resp = this.getRestTemplate().exchange(this.getFullUrl(uri), callType, entity, responseType, new Object[0]);
        log.debug("Response: [" + reqTrackingId + "] " + resp);
        log.debug("END makeCall()");
        return resp.getBody();
    }

    protected <T> T makeCall(String uri, HttpMethod callType, Object requestObj, ParameterizedTypeReference<T> parameterizedReturnType) {
        log.debug("START makeCall()");
        HttpHeaders httpHeader = this.getHeaders();
        String reqTrackingId = this.addLVTrackingHeader(httpHeader);
        HttpEntity entity = null;
        if (requestObj != null) {
            entity = new HttpEntity(requestObj, httpHeader);
        } else {
            entity = new HttpEntity(httpHeader);
        }

        ResponseEntity<T> resp = this.getRestTemplate().exchange(this.getFullUrl(uri), callType, entity, parameterizedReturnType, new Object[0]);
        log.debug("Response   [" + reqTrackingId + "] : " + resp);
        log.debug("END makeCall()");
        return resp.getBody();
    }

    protected <T> T makeCall(String uri, HttpMethod callType, Object requestObj, MediaType acceptMediaType, Class<T> responseType) {
        log.debug("START makeCall()");
        HttpHeaders httpHeader = this.getHeaders(acceptMediaType, (Map)null);
        String reqTrackingId = this.addLVTrackingHeader(httpHeader);
        HttpEntity entity = null;
        if (requestObj != null) {
            entity = new HttpEntity(requestObj, httpHeader);
        } else {
            entity = new HttpEntity(httpHeader);
        }

        ResponseEntity<T> resp = this.getRestTemplate().exchange(this.getFullUrl(uri), callType, entity, responseType, new Object[0]);
        log.debug("Response:   [" + reqTrackingId + "] " + resp);
        log.debug("END makeCall()");
        return resp.getBody();
    }

    protected <T> T makeCall(String uri, HttpMethod callType, MediaType acceptMediaType, Class<T> responseType) {
        log.debug("START makeCall()");
        HttpHeaders httpHeader = this.getHeaders(acceptMediaType, (Map)null);
        String reqTrackingId = this.addLVTrackingHeader(httpHeader);
        HttpEntity entity = null;
        entity = new HttpEntity(httpHeader);
        ResponseEntity<T> resp = this.getRestTemplate().exchange(this.getFullUrl(uri), callType, entity, responseType, new Object[0]);
        log.debug("Response:   [" + reqTrackingId + "] " + resp);
        log.debug("END makeCall()");
        return resp.getBody();
    }

    protected <T> T makeCall(String uri, HttpMethod callType, Class<T> responseType) {
        log.debug("START makeCall()");
        HttpHeaders httpHeader = this.getHeaders();
        String reqTrackingId = this.addLVTrackingHeader(httpHeader);
        HttpEntity entity = null;
        entity = new HttpEntity(httpHeader);
        ResponseEntity<T> resp = this.getRestTemplate().exchange(this.getFullUrl(uri), callType, entity, responseType, new Object[0]);
        log.debug("Response:  [" + reqTrackingId + "] " + resp);
        log.debug("END makeCall()");
        return resp.getBody();
    }

    protected <T> T makeCall(String uri, HttpMethod callType, ParameterizedTypeReference<T> parameterizedReturnType) {
        log.debug("START makeCall()");
        HttpHeaders httpHeader = this.getHeaders();
        String reqTrackingId = this.addLVTrackingHeader(httpHeader);
        HttpEntity entity = null;
        entity = new HttpEntity(httpHeader);
        ResponseEntity<T> resp = this.getRestTemplate().exchange(this.getFullUrl(uri), callType, entity, parameterizedReturnType, new Object[0]);
        log.debug("Response:  [" + reqTrackingId + "] " + resp);
        log.debug("END makeCall()");
        return resp.getBody();
    }

    protected <T> T makeCall(String uri, HttpMethod callType, Object requestObj, ParameterizedTypeReference<T> parameterizedReturnType, Map<String, List<String>> headers) {
        log.debug("START makeCall()");
        HttpHeaders httpHeader = this.getHeaders((MediaType)null, (Map)null);
        String reqTrackingId = this.addLVTrackingHeader(httpHeader);
        httpHeader.putAll(headers);
        HttpEntity entity = null;
        if (requestObj != null) {
            entity = new HttpEntity(requestObj, httpHeader);
        } else {
            entity = new HttpEntity(httpHeader);
        }

        ResponseEntity<T> resp = this.getRestTemplate().exchange(this.getFullUrl(uri), callType, entity, parameterizedReturnType, new Object[0]);
        log.debug("Response:  [" + reqTrackingId + "] " + resp);
        log.debug("END makeCall()");
        return resp.getBody();
    }

    protected <T> T makeCall(String uri, HttpMethod callType, ParameterizedTypeReference<T> parameterizedReturnType, Map<String, List<String>> headers) {
        log.debug("START makeCall()");
        HttpHeaders httpHeader = this.getHeaders();
        String reqTrackingId = this.addLVTrackingHeader(httpHeader);
        if (headers != null) {
            httpHeader.putAll(headers);
        }

        HttpEntity<String> entity = new HttpEntity(httpHeader);
        ResponseEntity<T> resp = this.getRestTemplate().exchange(this.getFullUrl(uri), callType, entity, parameterizedReturnType, new Object[0]);
        log.debug("Response: [" + reqTrackingId + "] " + resp);
        log.debug("END makeCall()");
        return resp.getBody();
    }

    protected <T> T makeCall(String uri, HttpMethod callType, Object requestObj, Class<T> responseType, Map<String, List<String>> headers) {
        log.debug("START makeCall()");
        HttpHeaders httpHeader = this.getHeaders((MediaType)null, (Map)null);
        String reqTrackingId = this.addLVTrackingHeader(httpHeader);
        httpHeader.putAll(headers);
        HttpEntity entity = null;
        if (requestObj != null) {
            entity = new HttpEntity(requestObj, httpHeader);
        } else {
            entity = new HttpEntity(httpHeader);
        }

        ResponseEntity<T> resp = this.getRestTemplate().exchange(this.getFullUrl(uri), callType, entity, responseType, new Object[0]);
        log.debug("Response: [" + reqTrackingId + "] " + resp);
        log.debug("END makeCall()");
        return resp.getBody();
    }

    protected HttpHeaders getHeaders() {
        return this.getHeaders((MediaType)null, (Map)null);
    }

    protected HttpHeaders getHeaders(Map<String, String> headers) {
        return this.getHeaders((MediaType)null, headers);
    }

    protected HttpHeaders getHeaders(MediaType overrideMediaType, Map<String, String> headers) {
        log.debug("START getHeaders()");
        HttpHeaders httpHeaders = new HttpHeaders();
        if (overrideMediaType != null) {
            httpHeaders.setAccept(Arrays.asList(overrideMediaType));
            httpHeaders.setContentType(this.acceptMimeType);
        } else {
            httpHeaders.setAccept(Arrays.asList(this.acceptMimeType));
            httpHeaders.setContentType(this.acceptMimeType);
        }

        if (headers != null) {
            Iterator var4 = headers.entrySet().iterator();

            while(var4.hasNext()) {
                Map.Entry<String, String> entry = (Map.Entry)var4.next();
                log.debug("Adding Header " + (String)entry.getKey() + " : " + (String)entry.getValue());
                httpHeaders.add((String)entry.getKey(), (String)entry.getValue());
            }
        }

        if (this.getAtgConfigKey() != null) {
            httpHeaders.put("atg-config-key", Arrays.asList(this.getAtgConfigKey()));
        }

        log.debug("END getHeaders()");
        return httpHeaders;
    }

    public static String getFullUrl(String hostname, String uri) {
        if (!StringUtils.isEmpty(hostname) && !StringUtils.isEmpty(uri)) {
            StringBuilder sb = new StringBuilder();
            sb.append(hostname);
            if (!hostname.endsWith("/") && !uri.startsWith("/")) {
                sb.append("/");
            }

            sb.append(uri.replaceAll("\\/\\/", "/"));
            log.debug("Making call with URL: " + sb.toString());
            return sb.toString();
        } else {
            return null;
        }
    }

    protected String getFullUrl(String uri) {
        return getFullUrl(this.getHostName(), uri);
    }

    protected String addLVTrackingHeader(HttpHeaders httpHeaders) {
        UUID uuid = UUID.randomUUID();
        String headerId = uuid.toString();
        if (httpHeaders != null) {
            httpHeaders.add("LV_REQUEST_TRACKING_ID", headerId);
        }

        return headerId;
    }

    public RestTemplate getRestTemplate() {
        return this.restTemplate;
    }

    public String getHostName() {
        return this.hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public void setAcceptMimeType(MediaType acceptMimeType) {
        this.acceptMimeType = acceptMimeType;
    }

    public MediaType getAcceptMimeType() {
        return this.acceptMimeType;
    }

    public String getAtgConfigKey() {
        return this.atgConfigKey;
    }

    public void setAtgConfigKey(String atgConfigKey) {
        this.atgConfigKey = atgConfigKey;
    }
}
