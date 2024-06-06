/*
 * Copyright (c) 2020. Luis Felipe Sosa Alvarez. All rights reserved to LiveVox.
 * Use this subject to license terms.
 *
 * IS-CALL-MULTIPLIER
 */

package com.livevox.commons.services.rest.session;

import com.livevox.commons.domain.LvSession;
import com.livevox.commons.exceptions.AuthenticationException;
import com.livevox.commons.exceptions.InvalidParametersException;
import com.livevox.commons.exceptions.UnauthorizedException;
import com.livevox.commons.services.config.AtgConfigService;
import com.livevox.commons.services.rest.RestServiceLvAbstract;
import com.livevox.commons.services.signin.SignInService;
import jakarta.xml.ws.WebServiceException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service
public class LvSessionRestServiceImpl extends RestServiceLvAbstract implements LvSessionRestService {
    private static final long serialVersionUID = -5977699113327L;
    public static final String API_PREFIX = "/session/";
    public static final String LOGIN_REST_URI_SUFFIX = "/login";
    public static final String LOGOUT_REST_URI_SUFFIX = "/logout/";
    public static final String IS_VALID_URI_SUFFIX = "/validate/";
    public static final String CHANGE_PASSWORD_URI_SUFFIX = "/password";

    @Autowired
    public LvSessionRestServiceImpl(AtgConfigService atgConfigService) {
        super(atgConfigService);
    }

    @Autowired
    public LvSessionRestServiceImpl(AtgConfigService atgConfigService, SignInService signInService) {
        super(atgConfigService, signInService);
    }

    public LvSession login(String clientName, String userName, String password, boolean agent, String apiVersionOverride) throws WebServiceException, AuthenticationException, InvalidParametersException {
        return this.login(clientName, userName, password, agent, apiVersionOverride, (String)null, (String)null);
    }

    public LvSession login(String clientName, String userName, String password, boolean agent, String apiVersionOverride, String hostNameOverride, String lvAuthTokenOverride) throws WebServiceException, AuthenticationException, InvalidParametersException {
        log.debug("Start login() ");
        if (!StringUtils.isEmpty(clientName) && !StringUtils.isEmpty(userName) && !StringUtils.isEmpty(password)) {
            LvSession session = new LvSession(clientName, userName, password, agent);
            LvSession resp = null;

            try {
                resp = (LvSession)this.makeLoginCall(this.getURI("/session/", "/login", apiVersionOverride, clientName), HttpMethod.POST, session, LvSession.class, clientName, hostNameOverride, lvAuthTokenOverride);
            } catch (HttpClientErrorException var11) {
                log.error("LV Session login call failed. ", var11);
                if (var11.getStatusCode() == null || !var11.getStatusCode().equals(HttpStatus.FORBIDDEN) && !var11.getStatusCode().equals(HttpStatus.UNAUTHORIZED) && !var11.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                    throw new InvalidParametersException();
                }

                throw new UnauthorizedException("Login was denied");
            } catch (Exception var12) {
                log.error("LV Session login call failed. " + var12);
                throw new UnauthorizedException();
            }

            if (resp == null) {
                log.error("LV Session login call failed.  The response was invalid. ");
                throw new WebServiceException("Login failed. The response was invalid ");
            } else {
                session.setClientId(resp.getClientId());
                session.setSessionId(resp.getSessionId());
                session.setUserId(resp.getUserId());
                session.setDaysUntilPasswordExpires(resp.getDaysUntilPasswordExpires());
                log.debug("End login() ");
                return session;
            }
        } else {
            log.error("LV Session login call failed. ClientName: " + clientName + " UserName: " + userName + " PasswordBlank: " + StringUtils.isEmpty(password));
            throw new InvalidParametersException();
        }
    }

    public LvSession login(String clientName, String userName, String password, boolean agent) throws WebServiceException, AuthenticationException, InvalidParametersException {
        return this.login(clientName, userName, password, agent, (String)null);
    }

    public LvSession login(String clientName, String userName, String password) throws WebServiceException, AuthenticationException {
        return this.login(clientName, userName, password, false, (String)null);
    }

    public LvSession login(String clientName, String userName, String password, String apiVersionOverride) throws WebServiceException, AuthenticationException {
        return this.login(clientName, userName, password, false, apiVersionOverride);
    }

    public LvSession login(LvSession session) throws WebServiceException, AuthenticationException {
        return this.login(session.getClientName(), session.getUserName(), session.getPassword(), session.isAgent(), session.getApiVersion(), session.getApiHost(), session.getApiToken());
    }

    public String getApiSessionId(String clientName, String userName, String password) throws WebServiceException, AuthenticationException {
        return this.getApiSessionId(clientName, userName, password, (String)null);
    }

    public String getApiSessionId(String clientName, String userName, String password, String apiVersionOverride) throws WebServiceException, AuthenticationException {
        LvSession session = this.login(clientName, userName, password, false, apiVersionOverride);
        if (session == null) {
            log.error("LV Session login call failed.  The response was invalid. ");
            throw new AuthenticationException("Login failed. The response was invalid ");
        } else {
            return session.getSessionId();
        }
    }

    public boolean isValidApiSession(String apiSessionId) throws WebServiceException, AuthenticationException {
        return this.isValidApiSession(apiSessionId, (String)null, (String)null);
    }

    public boolean isValidApiSession(String apiSessionId, String apiVersionOverride) throws WebServiceException, AuthenticationException {
        return this.isValidApiSession(apiSessionId, apiVersionOverride, (String)null);
    }

    public boolean isValidApiSession(String apiSessionId, String apiVersionOverride, String apiTokenOverride) throws WebServiceException, AuthenticationException {
        log.debug("Start isValidToken() ");
        if (StringUtils.isEmpty(apiSessionId)) {
            return false;
        } else {
            try {
                this.makeLvCall(this.getURI("/session/", "/validate/" + apiSessionId, apiVersionOverride), HttpMethod.GET, (Object)null, (Class)null, (MediaType)null, (Integer)null, (String)null, apiSessionId, (String)null, apiTokenOverride);
                log.debug("End isValidToken() ");
                return true;
            } catch (HttpClientErrorException var5) {
                if (var5.getStatusCode() != null && var5.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                    return false;
                } else {
                    log.error("LV isValidToken call failed. " + var5);
                    throw new UnauthorizedException();
                }
            } catch (Exception var6) {
                log.error("LV isValidToken call failed. " + var6);
                throw new UnauthorizedException();
            }
        }
    }

    public void logout(String apiSessionId) throws WebServiceException, UnauthorizedException {
        this.logout(apiSessionId, (String)null, (String)null);
    }

    public void logout(String apiSessionId, String apiVersionOverride) throws WebServiceException, UnauthorizedException {
        this.logout(apiSessionId, apiVersionOverride, (String)null);
    }

    public void logout(String apiSessionId, String apiVersionOverride, String apiLvTokenOverride) throws WebServiceException, UnauthorizedException {
        log.debug("Start logout() ");
        if (!StringUtils.isEmpty(apiSessionId)) {
            try {
                this.makeLvCall(this.getURI("/session/", "/logout/" + apiSessionId, apiVersionOverride), HttpMethod.DELETE, (Object)null, (Class)null, (Integer)null, (String)null, apiSessionId);
                log.debug("End logout() ");
            } catch (HttpClientErrorException var5) {
                if (var5.getStatusCode() == null || !var5.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                    log.error("LV logout call failed. " + var5);
                    throw new UnauthorizedException();
                }
            } catch (Exception var6) {
                log.error("LV logout call failed. " + var6);
                throw new WebServiceException();
            }
        }
    }

    public void logout(LvSession session) throws WebServiceException, AuthenticationException {
        if (session != null) {
            this.logout(session.getSessionId(), session.getApiVersion(), session.getApiToken());
        }

    }

    public void refreshExpiredSession(LvSession selectedHeader) throws WebServiceException {
        if (StringUtils.isEmpty(selectedHeader.getSessionId()) || selectedHeader.tokenExipred(this.minutesToForceCacheRefresh)) {
            try {
                selectedHeader.setSessionId(this.getApiSessionId(selectedHeader.getClientName(), selectedHeader.getUserName(), selectedHeader.getPassword()));
                selectedHeader.tokenUpdated();
            } catch (Exception var3) {
                log.error("Login to LV Session service failed.  " + var3);
                throw new WebServiceException("Login to LV Session service failed.  " + var3, var3);
            }
        }

    }

    public void refreshExpiredSessionSignIn(LvSession selectedHeader) throws WebServiceException {
        if (StringUtils.isEmpty(selectedHeader.getSessionId()) || selectedHeader.tokenExipred(this.minutesToForceCacheRefresh)) {
            if (StringUtils.isEmpty(selectedHeader.getApplication())) selectedHeader.setApplication("QCS");
            try {
                selectedHeader.setSessionId(signInService.login(selectedHeader.getClientName(),
                        selectedHeader.getUserName(), selectedHeader.getPassword(),
                        selectedHeader.getApplication()).getSessionId());
                selectedHeader.tokenUpdated();
            } catch (Exception var3) {
                log.error("Login to LV Session service failed.  " + var3);
                throw new WebServiceException("Login to LV Session service failed.  " + var3, var3);
            }
        }

    }

    protected boolean processAuthFailureException(String msg, LvSession session, Exception exp) throws WebServiceException {
        if (session != null && exp != null) {
            if (exp instanceof AuthenticationException) {
                log.warn("Authentication on API service failed. Getting a new header....");

                try {
                    session.setSessionId(this.getApiSessionId(session.getClientName(), session.getUserName(), session.getPassword()));
                } catch (Exception var5) {
                    log.error("Getting a new apiSessionId failed. " + var5);
                }

                return !StringUtils.isEmpty(session.getSessionId());
            } else {
                log.error(msg, exp);
                throw new WebServiceException(exp);
            }
        } else {
            return false;
        }
    }

    public boolean changePassword(String clientName, String userName, String currentPassword, String newPassword, boolean agent) {
        return this.changePassword(clientName, userName, currentPassword, newPassword, agent, (String)null);
    }

    public boolean changePassword(String clientName, String userName, String currentPassword, String newPassword, boolean agent, String apiVersionOverride) {
        LvSession session = new LvSession();
        session.setAgent(agent);
        session.setClientName(clientName);
        session.setUserName(userName);
        session.setPassword(currentPassword);
        session.setNewPassword(newPassword);
        session.setApiVersion(apiVersionOverride);
        return this.changePassword(session);
    }

    public boolean changePassword(LvSession request) {
        return this.changePassword(request, request.getApiVersion(), request.getApiHost(), request.getApiToken());
    }

    public boolean changePassword(LvSession request, String apiVersionOverride, String hostNameOverride, String lvAuthTokenOverride) {
        if (request != null && request.getClientName() != null) {
            LvSession session = new LvSession();
            session.setAgent(request.isAgent());
            session.setClientName(request.getClientName());
            session.setUserName(request.getUserName());
            session.setPassword(request.getPassword());
            session.setNewPassword(request.getNewPassword());

            try {
                this.makeLoginCall(this.getURI("/session/", "/password", apiVersionOverride, request.getClientName()), HttpMethod.POST, session, Void.class, request.getClientName(), hostNameOverride, lvAuthTokenOverride);
                return true;
            } catch (Exception var7) {
                log.error("Changing password failed. ", var7);
                throw new WebServiceException("Changing password failed. ");
            }
        } else {
            return false;
        }
    }
}
