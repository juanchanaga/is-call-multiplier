/*
 * Copyright (c) 2020. Luis Felipe Sosa Alvarez. All rights reserved to LiveVox.
 * Use this subject to license terms.
 *
 * IS-CALL-MULTIPLIER
 */

package com.livevox.multiplier.jobs;

import com.livevox.multiplier.domain.ClientInfo;
import com.livevox.multiplier.services.config.AtgConfigService;
import com.livevox.multiplier.services.impl.LvSessionRestServiceImpl;
import com.livevox.multiplier.domain.lvapi.ListRequest;
import com.livevox.multiplier.domain.JobRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Optional;

public class AbstractConfigWrapper <T> implements Serializable {

    private static final long serialVersionUID = 88873546799L;

    private static final Logger log = LoggerFactory.getLogger(AbstractConfigWrapper.class);

    protected AtgConfigService configService;

    protected JobRequest req;

    protected ClientInfo client;

    protected T service;




    public AbstractConfigWrapper(final JobRequest req, final Class cls ) throws Exception{
        this.req = req;
        this.configService = req.getConfigService();
        init(cls);
    }


    public AbstractConfigWrapper(final ListRequest lstReq, final AtgConfigService configService,
                                 final Class cls ) throws Exception{
        JobRequest newReq = new JobRequest(lstReq);
        newReq.setConfigService(configService );
        this.configService = newReq.getConfigService();
        init(cls);
    }


    private void init(final Class cls) throws Exception {
        if(cls == null || getConfigService() == null) {
            return;
        }
        setService( (T) generateService(cls) );
        setClient( getClientDetails() );
        populateClientSpecificConfig(getClient(), getService(), cls);
    }


    protected ClientInfo getClientDetails() {
        Optional<ClientInfo> clientDetails = configService.getClient(req.getClientId());
        if( clientDetails.isPresent() ) {
            return clientDetails.get();
        }
        return null;
    }


    protected Object generateService(Class cls) throws Exception{
        if(cls == null || getConfigService() == null) {
            return null;
        }
        try {
            Class lvSrvcCls = Class.forName(cls.getName());
            if (lvSrvcCls == null) {
                log.error("Job Wrapper failed to create a needed service for class: " +
                        cls.getName());
                return null;
            }
            Constructor<?> cstr = lvSrvcCls.getConstructor(AtgConfigService.class);
            return  cls.cast( cstr.newInstance(new Object[] { getConfigService() }) );
        } catch(Exception e) {
            log.error("An error happened trying to create class: "+cls.getName()+"  "+e);
            throw e;
        }
    }


    protected void populateClientSpecificConfig(ClientInfo client, Object srvc, Class cls) throws  Exception{
        if(client == null || srvc == null || cls == null) {
            return;
        }
        if(StringUtils.isNotBlank(client.getApiVersion())) {
            log.debug("Setting client specific version: "+client.getApiVersion()+
                    " for service: "+cls.getName());
            Method setVer = cls.getMethod("setVersion", new Class[] {String.class});
            setVer.invoke(srvc, new Object[] {client.getApiVersion()} );
        }
        if(StringUtils.isNotBlank(client.getApiHost())) {
            log.debug("Setting client specific host: "+client.getApiHost()+
                    " for service: "+cls.getName()+" for clientId: "+client.getId());
            Method setHst = cls.getMethod("setHostName", new Class[] {String.class});
            setHst.invoke(srvc, new Object[] {client.getApiHost()} );
        }
        if(StringUtils.isNotBlank(client.getApiToken())) {
            log.debug("Setting client specific host: "+client.getApiHost()+
                    " for service: "+cls.getName()+" for clientId: "+client.getId());
            Method setHst = cls.getMethod("setLvAuthToken", new Class[] {String.class});
            setHst.invoke(srvc, new Object[] {client.getApiToken()} );
        } else {
            String accessToken = configService.getRequiredProperty(
                    LvSessionRestServiceImpl.API_PROPERTY_PREFIX+"authToken");
            if(StringUtils.isEmpty(accessToken)) {
                throw new IllegalStateException("Client "+cls.getName()+
                        "does not have an access token set in config service, nor is " +
                        "the default access token property set.");
            }
            log.debug("Setting default access token: "+accessToken+" client: "+
                    client.getClientName()+ " for service: "+cls.getName()+
                    " for clientId: "+client.getId());
            Method setHst = cls.getMethod("setLvAuthToken", new Class[] {String.class});
            setHst.invoke(srvc, new Object[] {client.getApiHost()} );
        }
    }


    public AtgConfigService getConfigService() {
        return configService;
    }

    public void setConfigService(AtgConfigService configService) {
        this.configService = configService;
    }

    public ClientInfo getClient() {
        return client;
    }

    public void setClient(ClientInfo client) {
        this.client = client;
    }

    public T getService() {
        return service;
    }

    public void setService(T service) {
        this.service = service;
    }

    public JobRequest getReq() {
        return req;
    }

    public void setReq(JobRequest req) {
        this.req = req;
    }
}

