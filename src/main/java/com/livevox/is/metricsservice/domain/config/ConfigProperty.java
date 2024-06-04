package com.livevox.is.metricsservice.domain.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.livevox.integration.commons.domain.stats.DefaultDao;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConfigProperty extends DefaultDao {
    private String key;
    private String value;
    private Long appId;
    private Integer clientId;
    private Long propId;
    private String appName;
    private String dataType;
    private Map<Integer, String> clientValues;

    public ConfigProperty() {
    }

    public ConfigProperty(String key, String value) {
        this.setKey(key);
        this.setValue(value);
    }

    public ConfigProperty(String key, String value, Integer clientId) {
        this(key, value);
        this.setClientId(clientId);
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getClientId() {
        return this.clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public Long getPropId() {
        return this.propId;
    }

    public void setPropId(Long propId) {
        this.propId = propId;
    }

    public Long getAppId() {
        return this.appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public String getAppName() {
        return this.appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getClientValue(Integer clientId) {
        if (this.clientValues != null) {
            Optional<String> clientProp = Optional.ofNullable(this.clientValues.get(clientId));
            return (String)clientProp.orElseGet(() -> {
                return this.value;
            });
        } else {
            return this.value;
        }
    }

    public void addClientValue(String value, Integer clientId) {
        if (this.clientValues == null) {
            this.clientValues = new HashMap();
        }

        this.clientValues.put(clientId, value);
    }

    public void updateClientValue(String value, Integer clientId) {
        if (this.clientValues != null) {
            this.clientValues.put(clientId, value);
        }

    }

    public void removeClientValue(Integer clientId) {
        if (this.clientValues != null) {
            this.clientValues.remove(clientId);
        }

    }

    public String getDataType() {
        return this.dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String toString() {
        return "key: " + this.key + " value: " + this.value + " client values " + this.clientValues;
    }

    public Map<Integer, String> getClientValues() {
        return this.clientValues;
    }

    public void setClientValues(Map<Integer, String> clientValues) {
        this.clientValues = clientValues;
    }

    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o, true);
    }

    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this, true);
    }
}
