/*
 * Copyright (c) 2020. Luis Felipe Sosa Alvarez. All rights reserved to LiveVox.
 * Use this subject to license terms.
 *
 * IS-CALL-MULTIPLIER
 */

package com.livevox.multiplier.services.config;

import com.livevox.multiplier.dao.PropertyDao;
import com.livevox.multiplier.domain.ClientInfo;
import com.livevox.multiplier.domain.ClientsAndPropertiesResponse;
import com.livevox.multiplier.domain.ConfigProperty;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class AtgConfigServiceImpl implements AtgConfigService, Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 4514425776103457924L;
    protected static final Logger log = LoggerFactory.getLogger(AtgConfigServiceImpl.class);
    @Autowired(required = false)
    private AtgConfigRestService atgConfigRestService;
    @Autowired(required = false)
    private PropertyDao propertyDao;

    private volatile Map<Integer, ClientInfo> clients = new HashMap<>();

    private volatile Map<String, ConfigProperty> properties = new HashMap<>();

    public AtgConfigServiceImpl() {}


    public AtgConfigServiceImpl(AtgConfigRestService atgConfigRestService) {
        this.atgConfigRestService = atgConfigRestService;
    }


    @PostConstruct
    public void initialize() {
        log.info("Config Service: Using AtgConfigRestService: {}  Using ClientDao: {}  Using PropertyDao {}", (atgConfigRestService != null),
                (atgConfigRestService == null), (propertyDao != null));
        refresh();
    }

    @Override
    public void refresh() {
        if (atgConfigRestService != null) {
            ClientsAndPropertiesResponse config = atgConfigRestService.getAppConfigInfo();
            setClients(config.getClients());
            setProperties(config.getProperties());
        }
    }

    @Override
    public List<ClientInfo> getClientsList() {
        return new ArrayList<>(getClients().values());
    }

    @Override
    public Map<Integer, ClientInfo> getClients() {
        return clients;
    }

    @Override
    public Optional<ClientInfo> getClient(Integer clientId, String clientName){
        if(clientId != null) {
            return Optional.ofNullable(getClients().get(clientId));
        }
        if(clientName != null) {
            for (ClientInfo client : getClients().values()) {
                if (StringUtils.equalsIgnoreCase(client.getClientName(), clientName)) {
                    return Optional.of(client);
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<ClientInfo> getClient(Integer clientId) {
        return this.getClient(clientId, null);
    }

    @Override
    public Optional<ClientInfo> getClient(String clientName) {
        return this.getClient(null, clientName);
    }

    private Optional<String> getClientVersion(Optional<ClientInfo> client) {
        if (client.isPresent()) {
            return Optional.ofNullable(client.get()
                    .getApiVersion());
        }
        return Optional.empty();
    }

    @Override
    public Optional<String> getClientApiVersion(Integer clientId) {
        return getClientVersion(getClient(clientId));
    }

    @Override
    public Optional<String> getClientApiVersion(String clientName) {
        return getClientVersion(getClient(clientName));
    }

    @Override
    public Optional<String> getClientApiToken(String clientName) {
        return getClientToken(getClient(clientName), null);
    }

    @Override
    public Optional<String> getClientApiToken(Integer clientId) {
        return getClientToken(getClient(clientId), null);
    }

    @Override
    public Optional<String> getClientApiToken(String clientName, String appName) {
        return getClientToken(getClient(clientName), appName);
    }

    @Override
    public Optional<String> getClientApiToken(Integer clientId, String appName) {
        return getClientToken(getClient(clientId), appName);
    }

    private Optional<String> getClientToken(Optional<ClientInfo> client, String appName) {
        if (client.isPresent()) {
            return Optional.ofNullable(client.get()
                    .getApiAccessTokenForApp(appName));
        }
        return Optional.empty();
    }

    @Override
    public Optional<String> getClientApiHost(Integer clientId) {
        return getClientApiHost(getClient(clientId));
    }

    @Override
    public Optional<String> getClientApiHost(String clientName) {
        return getClientApiHost(getClient(clientName));
    }

    private Optional<String> getClientApiHost(Optional<ClientInfo> client) {
        if (client.isPresent()) {
            return Optional.ofNullable(client.get()
                    .getApiHost());
        }
        return Optional.empty();
    }

    @Override
    public Optional<String> getNewClientSessionId(Integer clientId) {
        if (atgConfigRestService != null) {
            return Optional.ofNullable(atgConfigRestService.getApiSession(clientId));
        }
        return Optional.empty();
    }

    @Override
    public void updateClientSessionId(Integer clientId, final String apiSessionId) {
        getClient(clientId).ifPresent(client -> {
            client.setSessionId(apiSessionId);
            client.setTokenLastUpdated(Calendar.getInstance());
        });
    }

    public void setAtgConfigService(AtgConfigRestService atgConfigService) {
        this.atgConfigRestService = atgConfigService;
    }

    public void setPropertyDao(PropertyDao propertyDao) {
        this.propertyDao = propertyDao;
    }

    @Override
    public List<ConfigProperty> getProperties() {
        if( properties == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(properties.values());
    }

    @Override
    public void setClients(List<ClientInfo> clients) {
        if (clients != null) {
            this.clients = clients.stream()
                    .collect((Collectors.toMap(ClientInfo::getClientId, Function.identity())));
        }
    }

    @Override
    public void setProperties(List<ConfigProperty> properties) {
        if (properties != null) {
            this.properties = properties.stream()
                    .collect((Collectors.toMap(ConfigProperty::getKey, Function.identity())));
        }
    }

    @Override
    public void setClients(Map<Integer, ClientInfo> clients) {
        this.clients = clients;
    }

    @Override
    public boolean addClientProperty(String key, String value, Integer clientId) {
        Optional<ConfigProperty> property = getConfigProperty(key);
        if (property.isPresent()) {
            property.get()
                    .addClientValue(value, clientId);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateClientProperty(String key, String value, Integer clientId) {
        Optional<ConfigProperty> property = getConfigProperty(key);
        if (property.isPresent()) {
            property.get()
                    .updateClientValue(value, clientId);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteClientProperty(String key, Integer clientId) {
        Optional<ConfigProperty> property = getConfigProperty(key);
        if (property.isPresent()) {
            property.get()
                    .removeClientValue(clientId);
            return true;
        }
        return false;
    }

    @Override
    public boolean addProperty(String key, String value) {
        ConfigProperty property = new ConfigProperty();
        property.setKey(key);
        property.setValue(value);
        return addProperty(property);
    }

    @Override
    public boolean addProperty(ConfigProperty property) {
        properties.put(property.getKey(), property);
        return true;
    }

    @Override
    public boolean updateProperty(String key, String value) {
        Optional<ConfigProperty> property = getConfigProperty(key);
        if (property.isPresent()) {
            property.get()
                    .setValue(value);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateProperty(ConfigProperty prop) {
        Optional<ConfigProperty> property = getConfigProperty(prop.getId());
        if (property.isPresent()) {
            property.get()
                    .setValue(prop.getValue());
            property.get()
                    .setKey(prop.getKey());
            return true;
        }
        return false;
    }


    @Override
    public boolean deleteProperty(String key) {
        return properties.remove(key) != null;
    }

    private Optional<ConfigProperty> getConfigProperty(String key) {
        if(properties == null ) {
            return Optional.empty();
        }
        return Optional.ofNullable(properties.get(key));
    }


    private Optional<ConfigProperty> getConfigProperty(Long id) {
        return properties.values()
                .stream()
                .filter(prop -> prop.getId()
                        .equals(id))
                .findAny();
    }


    @Override
    public String getProperty(String key) {
        log.trace("getting property {}",key);
        Optional<ConfigProperty> property = getConfigProperty(key);
        if (property.isPresent()) {
            log.trace("property found, returning optional");
            return property.get()
                    .getValue();
        } else if (propertyDao != null) {
            log.trace("property not found, checking property dao ");
            return propertyDao.getProperty(key);
        } else {
            log.trace("unable to find property {}",key);
            return null;
        }
    }

    @Override
    public String getProperty(String key, Integer clientId) {
        Optional<ConfigProperty> property = getConfigProperty(key);
        if (property.isPresent()) {
            return property.get()
                    .getClientValue(clientId);
        } else if (propertyDao != null) {
            return propertyDao.getClientPropertyString(key, clientId);
        } else {
            log.trace("unable to find property {}", key);
            return null;
        }
    }

    @Override
    public String getRequiredProperty(String key) throws IllegalStateException {
        String value = getProperty(key);
        if (StringUtils.isEmpty(value)) {
            throw new IllegalStateException("Required property (" + key + ") was not set.");
        }
        return value;
    }

    @Override
    public String getRequiredProperty(String key, Integer clientId) throws IllegalStateException {
        String value = getProperty(key, clientId);
        if (StringUtils.isEmpty(value)) {
            throw new IllegalStateException("Required property (" + key + "," + clientId + ") was not set.");
        }
        return value;
    }


    @Override
    @Transactional(readOnly = true)
    public String getRequiredPropertyRealtime(String key) throws IllegalStateException, SQLException {
        refresh();
        return getRequiredProperty(key);
    }

    @Override
    @Transactional(readOnly = true)
    public String getRequiredPropertyRealtime(String key, Integer clientid)
            throws IllegalStateException, SQLException {
        refresh();
        return getRequiredProperty(key, clientid);
    }

    public AtgConfigRestService getAtgConfigRestService() {
        return atgConfigRestService;
    }

    public void setAtgConfigRestService(AtgConfigRestService atgConfigRestService) {
        this.atgConfigRestService = atgConfigRestService;
    }

    @Override
    public PropertyDao getPropertyDao() {
        return propertyDao;
    }
}
