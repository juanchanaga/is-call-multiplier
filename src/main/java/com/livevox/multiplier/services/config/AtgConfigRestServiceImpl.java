/*
 * Copyright (c) 2020. Luis Felipe Sosa Alvarez. All rights reserved to LiveVox.
 * Use this subject to license terms.
 *
 * IS-CALL-MULTIPLIER
 */

package com.livevox.multiplier.services.config;

import com.livevox.multiplier.dao.PropertyDao;
import com.livevox.multiplier.domain.ClientsAndPropertiesResponse;
import com.livevox.multiplier.services.RestServiceAbstract;
import jakarta.xml.ws.WebServiceException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Map;

@Service
public class AtgConfigRestServiceImpl extends RestServiceAbstract
        implements AtgConfigRestService, Serializable {


    /**
     *
     */
    private static final long serialVersionUID = 5970843899910003405L;

    public static final String HOST_PROPERTY_NAME = "com.livevox.commons.services.atgconfig.host";

    public static final String VERSION_PROPERTY_NAME = "com.livevox.commons.services.atgconfig.version";

    private String version = "v1_0";

    public static final String KEY_PROPERTY_NAME = "com.livevox.commons.services.atgconfig.key";

    public static final String API_KEY = "/atgconfig/api/lvSession/{clientId}";

    public static final String CONFIG_PROPERTIES = "/atgconfig/config";

    public static final String UPDATE_PROPERTIES = "/atgconfig/properties";

    public static final String CONFIG_CLIENTS_AND_PROPS = "/atgconfig/config/withClients";

    public static final String CONFIG_FEEDER_URI = "/atgconfig/config/feeder";

    public static final String KEY_VALID_URI = "/atgconfig/key/isValid";

    public static final String KEY_INFO_URI = "/atgconfig/key/details";

    public static final String APP_LIST = "/atgconfig/application";

    public static final String TOKENS = "/atgconfig/tokens";

    private String key;

    private String appVersion;


    public AtgConfigRestServiceImpl() {
        super();
    }
    
    public AtgConfigRestServiceImpl(PropertyDao propertyDao) {
        this();
        if (propertyDao == null) {
            log.warn("AtgConfigService was started without setting the propertiesDao. No property values can be set.");
            return;
        }
        String prop = propertyDao.getProperty(VERSION_PROPERTY_NAME);
        if (!StringUtils.isEmpty(prop)) {
            version = prop;
        }

        prop = propertyDao.getProperty(HOST_PROPERTY_NAME);
        if (!StringUtils.isEmpty(prop)) {
            setHostName(prop);
        }
        prop =  propertyDao.getProperty(KEY_PROPERTY_NAME);
        if (!StringUtils.isEmpty(prop)) {
            setKey(prop);
        }
    }


    public AtgConfigRestServiceImpl(String host, String key) {
        this();
        if (StringUtils.isEmpty(host) || StringUtils.isEmpty(key)) {
            throw new IllegalStateException("The config service host and key can not be NULL or empty.");
        }
        setHostName(host);
        setKey(key);
    }


    public AtgConfigRestServiceImpl(String host, String key, String version) {
        this(host, key);
        setVersion(version);
    }


    public AtgConfigRestServiceImpl(String host, String key, String version, String appVersion) {
        this(host, key, version);
        setAppVersion(appVersion);
    }


    @Override
    public String getApiSession(Integer clientId) throws WebServiceException {
        log.debug("Start getApiSession() ");
        if (key == null) {
            throw new WebServiceException("Unable to retrieve api session, key was null");
        }
        String resp;
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString("/" + version + API_KEY);
            resp = makeCall(builder.buildAndExpand(clientId)
                    .toUriString(), HttpMethod.GET, null, String.class);
        } catch (Exception e) {
            log.error("Error finding api session. ", e);
            throw new WebServiceException("Error finding api session. ");
        }
        if (resp == null) {
            log.error("Unable to app config. The response was invalid. ");
            throw new WebServiceException("api session retrieval failed. The response was invalid ");
        }
        log.debug("End getApiSession() ");
        return resp;
    }


    @Override
    public ClientsAndPropertiesResponse getAppConfigInfo() throws WebServiceException {
        log.debug("Start getAppConfigInfo() ");
        if (key == null) {
            throw new WebServiceException("Unable to retrieve app config, key was null");
        }
        ClientsAndPropertiesResponse resp;
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString("/" +
                    version + CONFIG_CLIENTS_AND_PROPS );

            if (appVersion != null) {
                builder.queryParam("version", appVersion);
            }
            resp = makeCall(builder.buildAndExpand()
                    .toUriString(), HttpMethod.GET, null, ClientsAndPropertiesResponse.class);
        } catch (Exception e) {
            log.error("Error finding app config. ", e);
            throw new WebServiceException("Error finding app config. ");
        }
        if (resp == null) {
            log.error("Unable to app config. The response was invalid. ");
            throw new WebServiceException("app config failed. The response was invalid ");
        }
        log.debug("End getAppConfigInfo() ");
        return resp;
    }

    @Override
    protected HttpHeaders getHeaders(MediaType overrideMediaType, Map<String, String> headers) {
        HttpHeaders httpHeaders = super.getHeaders(overrideMediaType, headers);
        httpHeaders.put(CONFIG_KEY_HEADER_NAME, Arrays.asList(key));
        return httpHeaders;
    }


    @Autowired(required = false)
    @Qualifier("appVersion")
    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String appVersion() {
        return appVersion;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

}
