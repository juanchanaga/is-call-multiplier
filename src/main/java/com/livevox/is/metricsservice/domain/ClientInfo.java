package com.livevox.is.metricsservice.domain;

import com.livevox.is.metricsservice.domain.config.ConfigToken;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.List;

public class ClientInfo extends LvSession {

    private static final long serialVersionUID = 952895662011575507L;
    private String ftpUserName;
    private String ftpPassword;
    private String ftpHost;

    private List<ConfigToken> apiAccessTokens = new ArrayList<>();

    public ClientInfo() {

    }

    public ClientInfo(LvSession session) {
        setClientId(session.getClientId());
        setClientName(session.getClientName());
        setPassword(session.getPassword());
        setSessionId(session.getSessionId());
        setUserName(session.getUserName());
        setTokenLastUpdated(session.getTokenLastUpdated());
        setApiToken(session.getApiToken());
        setApiVersion(session.getApiVersion());
        setId(session.getId());
        setCreatedDate(session.getCreatedDate());
        setModifiedDate(session.getModifiedDate());
        setModifiedBy(session.getModifiedBy());
        setCreatedBy(session.getCreatedBy());
    }

    public LvSession toLvSession() {
        LvSession session = new LvSession();
        session.setClientId(getClientId());
        session.setClientName(getClientName());
        session.setPassword(getPassword());
        session.setSessionId(getSessionId());
        session.setUserName(getUserName());
        session.setTokenLastUpdated(getTokenLastUpdated());
        session.setApiToken(getApiToken());
        session.setApiVersion(getApiVersion());
        session.setCreatedDate(getCreatedDate());
        session.setModifiedDate(getModifiedDate());
        session.setModifiedBy(getModifiedBy());
        session.setCreatedBy(getCreatedBy());
        return session;
    }

    public String getFtpUserName() {
        return ftpUserName;
    }

    public void setFtpUserName(String ftpUsername) {
        this.ftpUserName = ftpUsername;
    }

    public String getFtpPassword() {
        return ftpPassword;
    }

    public void setFtpPassword(String ftpPassword) {
        this.ftpPassword = ftpPassword;
    }

    public String getFtpHost() {
        return ftpHost;
    }

    public void setFtpHost(String ftpHost) {
        this.ftpHost = ftpHost;
    }

    @Override
    public String toString() {
        return "Client " + getClientName() + " " + getClientId();
    }



    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o, true);
    }


    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this, true);
    }

    public List<ConfigToken> getApiAccessTokens() {
        return apiAccessTokens;
    }

    public void setApiAccessTokens(List<ConfigToken> apiAccessTokens) {
        this.apiAccessTokens = apiAccessTokens;
    }

    public String getApiAccessTokenForApp(String appName) {
        if (appName != null && apiAccessTokens != null && !apiAccessTokens.isEmpty()) {
            for (ConfigToken token : apiAccessTokens) {
                if (token.getApplications() != null && token.getApplications()
                        .contains(appName)) {
                    return token.getApiAccessToken();
                }
            }
        }
        return getApiToken();
    }



}
