/*
 * Copyright (c) 2020. Luis Felipe Sosa Alvarez. All rights reserved to LiveVox.
 * Use this subject to license terms.
 *
 * IS-CALL-MULTIPLIER
 */

package com.livevox.multiplier.services.config;

import com.livevox.multiplier.dao.PropertyDao;
import com.livevox.multiplier.domain.ClientInfo;
import com.livevox.multiplier.domain.ConfigProperty;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface AtgConfigService extends Serializable {

    Map<Integer, ClientInfo> getClients();

    String getProperty(String key);

    String getProperty(String key, Integer clientId);

    String getRequiredProperty(String key) throws IllegalStateException;

    String getRequiredProperty(String key, Integer clientId) throws IllegalStateException;

    public String getRequiredPropertyRealtime(String key) throws IllegalStateException, SQLException;

    boolean addClientProperty(String key, String value, Integer clientId);

    boolean updateClientProperty(String key, String value, Integer clientId);

    boolean deleteClientProperty(String key, Integer clientId);

    boolean addProperty(String key, String value);

    boolean addProperty(ConfigProperty property);

    boolean updateProperty(String key, String value);

    boolean deleteProperty(String key);

    List<ConfigProperty> getProperties();

    boolean updateProperty(ConfigProperty prop);

    void setProperties(List<ConfigProperty> properties);

    void setClients(List<ClientInfo> clients);

    void setClients(Map<Integer, ClientInfo> clients);

    List<ClientInfo> getClientsList();

    Optional<ClientInfo> getClient(Integer clientId);

    Optional<ClientInfo> getClient(String clientName);

    Optional<String> getNewClientSessionId(Integer clientId);

    void updateClientSessionId(Integer clientId, String apiSessionId);

    Optional<String> getClientApiToken(Integer clientId);

    Optional<String> getClientApiVersion(Integer clientId);

    Optional<String> getClientApiToken(String clientName);

    Optional<String> getClientApiVersion(String clientName);

    void refresh();

    Optional<String> getClientApiHost(String clientName);

    Optional<String> getClientApiHost(Integer clientId);

    public String getRequiredPropertyRealtime(String key, Integer clientid)
            throws IllegalStateException, SQLException;

    Optional<String> getClientApiToken(String clientName, String appName);

    Optional<String> getClientApiToken(Integer clientId, String appName);

    Optional<ClientInfo> getClient(Integer clientId, String clientName);

    PropertyDao getPropertyDao();


}
