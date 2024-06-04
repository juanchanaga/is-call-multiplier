package com.livevox.is.metricsservice.domain;

import com.livevox.integration.commons.domain.stats.DefaultDao;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class LvSession extends DefaultDao implements Serializable {

    private Integer clientId;

    private Integer userId;

    private String sessionId;

    private String clientName;

    private String application;

    private String userName;

    private String password;

    private Boolean agent;

    private Integer daysUntilPasswordExpires;

    private String newPassword;

    private String apiVersion;

    private String apiToken;

    private String apiHost;

    private List<LvSession> alternativeSessoins;

    private String salesForceId;

    private Calendar tokenLastUpdated;

    private String atgSecurityToken;

    private Calendar atgSecurityTokenLastUpdated;

    private Boolean forceSessionUnique;

    private String dataCenter;

    public LvSession(){
        agent = false;
    }

    public LvSession(String clientName, String userName, String password, boolean agent) {
        this.clientName = clientName;
        this.userName = userName;
        this.password = password;
        this.agent = agent;
    }

    public LvSession(String clientName, String userName, String password) {
        this(clientName, userName, password, false);
    }

    public LvSession(String clientName, String userName, String password, String apiToken) {
        this(clientName,userName,password);
        this.setApiToken(apiToken);
    }

    public LvSession(String clientName, String userName, String password, String apiToken, String application) {
        this(clientName,userName,password);
        this.setApiToken(apiToken);
        setApplication(application);
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public boolean isAgent() {
        if(agent == null){
            return false;
        }
        return agent;
    }

    public void setAgent(Boolean agent) {
        if(agent == null){
            this.agent = false;
        }else{
            this.agent = agent;
        }
    }


    public Integer getDaysUntilPasswordExpires() {
        return daysUntilPasswordExpires;
    }


    public void setDaysUntilPasswordExpires(Integer daysUntilPasswordExpires) {
        this.daysUntilPasswordExpires = daysUntilPasswordExpires;
    }

    public String getNewPassword() {
        return newPassword;
    }


    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }


    public String getApplication() {
        return application;
    }


    public void setApplication(String application) {
        this.application = application;
    }


    public String getApiVersion() {
        return apiVersion;
    }


    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }


    public List<LvSession> getAlternativeSessoins() {
        return alternativeSessoins;
    }


    public void setAlternativeSessoins(List<LvSession> alternativeSessoins) {
        this.alternativeSessoins = alternativeSessoins;
    }


    public void addAlternativeSessoin(LvSession newAlternativeSessoins) {
        if(this.alternativeSessoins == null) {
            this.alternativeSessoins = new ArrayList<>();
        }
        alternativeSessoins.add(newAlternativeSessoins);
    }

    public String getSalesForceId() {
        return salesForceId;
    }


    public void setSalesForceId(String salesForceId) {
        this.salesForceId = salesForceId;
    }


    public Calendar getTokenLastUpdated() {
        return tokenLastUpdated;
    }


    public void setTokenLastUpdated(Calendar tokenLastUpdated) {
        this.tokenLastUpdated = tokenLastUpdated;
    }


    public void tokenUpdated() {
        tokenLastUpdated = Calendar.getInstance();
    }


    public boolean tokenExipred(int minutesToLive) {
        if (tokenLastUpdated == null || minutesToLive < 1 || StringUtils.isEmpty(sessionId)) {
            return true;
        }
        Calendar now = Calendar.getInstance();
        now.add( Calendar.MINUTE, (minutesToLive * (-1)) );
        return (tokenLastUpdated.before(now));
    }


    public String getApiToken() {
        return apiToken;
    }


    public void setApiToken(String apiToken) {
        this.apiToken = apiToken;
    }


    public String getAtgSecurityToken() {
        return atgSecurityToken;
    }


    public void setAtgSecurityToken(String atgSecurityToken) {
        this.atgSecurityToken = atgSecurityToken;
    }


    public Calendar getAtgSecurityTokenLastUpdated() {
        return atgSecurityTokenLastUpdated;
    }


    public void setAtgSecurityTokenLastUpdated(Calendar atgSecurityTokenLastUpdated) {
        this.atgSecurityTokenLastUpdated = atgSecurityTokenLastUpdated;
    }


    public void atgSecurityTokenUpdated() {
        atgSecurityTokenLastUpdated = Calendar.getInstance();
    }


    public boolean atgSecurityTokenExipred(int minutesToLive) {
        if (atgSecurityTokenLastUpdated == null || minutesToLive < 1 || StringUtils
                .isEmpty(atgSecurityToken)) {
            return true;
        }
        Calendar now = Calendar.getInstance();
        now.add(Calendar.MINUTE, (minutesToLive * (-1)));
        return (atgSecurityTokenLastUpdated.before(now));
    }


    public String getApiHost() {
        return apiHost;
    }


    public void setApiHost(String apiHost) {
        this.apiHost = apiHost;
    }


    public String getDataCenter() {
        return dataCenter;
    }


    public void setDataCenter(String dataCenter) {
        this.dataCenter = dataCenter;
    }


    public Boolean getForceSessionUnique() {
        return forceSessionUnique;
    }


    public void setForceSessionUnique(Boolean forceSessionUnique) {
        this.forceSessionUnique = forceSessionUnique;
    }


    public Boolean hasForcedUniqueSession() {
        return (getForceSessionUnique() != null && getForceSessionUnique() );
    }


    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o, true);
    }


    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this, true);
    }


}
