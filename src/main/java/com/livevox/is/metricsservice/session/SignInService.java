/*
 * Copyright (c) 2020. Luis Felipe Sosa Alvarez. All rights reserved to LiveVox.
 * Use this subject to license terms.
 *
 * INTEGRATION-CALL-MULTIPLIER-SERVICE
 */

package com.livevox.is.metricsservice.session;

import com.livevox.is.metricsservice.domain.AuthResponse;
import com.livevox.is.metricsservice.domain.LvSession;
import com.livevox.is.metricsservice.domain.Response;
import com.livevox.is.metricsservice.domain.dao.PropertyDao;
import com.livevox.is.metricsservice.exceptions.AtgRuntimeException;
import com.livevox.is.metricsservice.exceptions.InvalidParametersException;
import com.livevox.is.metricsservice.exceptions.UnauthorizedException;
import com.livevox.is.metricsservice.service.config.AtgConfigService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.io.Serializable;

@Service
public class SignInService extends RestServiceAbstract implements Serializable {


    private static final long serialVersionUID = -5977699113327L;

    public static final String HOST_PROPERTY_NAME = "com.livevox.commons.services.rest.signinservice.host";

    public static final String VERSION_PROPERTY_NAME = "com.livevox.commons.services.signinservice.version";

    private String version = "v1_0";

    public static final String SIGNIN_LOGIN_URI = "/signin/universalLogin/";

    public static final String SIGNIN_AUTHENTICATED_URI = "/signin/isAuthenticated/";

    public static final String SIGNIN_LOGOUT_URI = "/signin/logout/";



    public SignInService() {}


    @Autowired(required = false)
    public SignInService(AtgConfigService configSrvc) {
        if(configSrvc == null) {
            log.warn("SignIn service was started without setting the config service.   No configuration values can be set.");
            return;
        }
        String prop = configSrvc.getProperty(VERSION_PROPERTY_NAME);
        if(!StringUtils.isEmpty(prop)) {
            version = prop;
        }
        prop = configSrvc.getProperty(HOST_PROPERTY_NAME);
        if(!StringUtils.isEmpty(prop)) {
            setHostName(prop);
        }
    }


    @Autowired(required = false)
    @Deprecated
    public SignInService(PropertyDao properties) {
        if(properties == null) {
            log.warn("SignIn service was started without setting the propertiesDao.   No property values can be set.");
            return;
        }
        String prop = properties.getProperty(VERSION_PROPERTY_NAME);
        if(!StringUtils.isEmpty(prop)) {
            version = prop;
        }
        prop = properties.getProperty(HOST_PROPERTY_NAME);
        if(!StringUtils.isEmpty(prop)) {
            setHostName(prop);
        }
    }


    public AuthResponse login(final String clientName, final String userName,
                              final String password, final String application) throws AtgRuntimeException, UnauthorizedException,
            InvalidParametersException {

        return login(clientName, userName, password, application, false);
    }


    public AuthResponse login(final String clientName, final String userName,
                              final String password, final String application, final Boolean agent)
            throws AtgRuntimeException, UnauthorizedException, InvalidParametersException {
        LvSession loginReq = new LvSession();
        loginReq.setClientName(clientName);
        loginReq.setUserName(userName);
        loginReq.setPassword(password);
        loginReq.setApplication(application);
        loginReq.setAgent(agent);
        return login(loginReq);
    }


    public AuthResponse login(final LvSession loginReq) throws AtgRuntimeException,
            UnauthorizedException, InvalidParametersException {
        log.debug("Start login() ");
        if(loginReq == null || StringUtils.isEmpty(loginReq.getClientName()) ||
                StringUtils.isEmpty(loginReq.getPassword()) ||
                StringUtils.isEmpty(loginReq.getUserName()) ) {
            throw new InvalidParametersException("Missing required fields.");
        }
        //
        //  Need an instances of the passed request object that only has the
        //  fields passed in this call.
        //
        LvSession safeReq = new LvSession(loginReq.getClientName(), loginReq.getUserName(),
                loginReq.getPassword(), loginReq.isAgent() );
        safeReq.setApplication(loginReq.getApplication());
        AuthResponse resp =null;
        try {
            resp =  makeCall("/"+version +SIGNIN_LOGIN_URI, HttpMethod.POST, safeReq,
                    AuthResponse.class);
        } catch(HttpClientErrorException httpExp) {
            log.error("Shared client data API call failed. ", httpExp);
            if(httpExp.getStatusCode() != null && httpExp.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                throw new AtgRuntimeException("API Service not avialable");
            } else if(httpExp.getStatusCode() != null && (
                    httpExp.getStatusCode().equals(HttpStatus.NOT_ACCEPTABLE) ||
                            httpExp.getStatusCode().equals(HttpStatus.BAD_REQUEST) )   ) {
                throw new InvalidParametersException();
            } else if(httpExp.getStatusCode() != null && httpExp.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {
                throw new UnauthorizedException("Invalid login.");
            } else {
                throw new AtgRuntimeException("API Service not avialable");
            }
        }catch(Exception e) {
            log.error("Signin client data API call failed. ", e);
            throw new AtgRuntimeException("API Service call failed.");
        }
        if(resp == null) {
            log.error("Signin client data API call failed.  The response was invalid. ");
            throw new AtgRuntimeException("Shared client data API call failed. The response was invalid ");
        }
        if(resp.getStatus() == null) {
            throw new AtgRuntimeException("Invalid response");
        } else if(resp.getStatus().equals(Response.ResponseStatus.AUTH_FAILED.getCode())) {
            throw new UnauthorizedException("Invalid response");
        } else if(resp.getStatus().equals(Response.ResponseStatus.GENERAL_ERROR.getCode())) {
            throw new AtgRuntimeException("Unknown error");
        } else if(resp.getStatus().equals(Response.ResponseStatus.SYSTEM.getCode())) {
            throw new AtgRuntimeException("Sysetm error");
        }
        if(resp.getStatus().equals(Response.ResponseStatus.SUCESS.getCode())) {
            log.debug("End login() ");
            return resp;
        } else {
            throw new AtgRuntimeException("Unknown Error");
        }
    }


    public Response logout(final String token) throws AtgRuntimeException,
            InvalidParametersException {
        log.debug("Start logout() ");
        if(StringUtils.isEmpty(token)) {
            throw new InvalidParametersException("A token is requreid to logout");
        }
        Response resp =null;
        try {
            resp =  makeCall("/"+version + SIGNIN_LOGOUT_URI + token +"/",
                    HttpMethod.DELETE, Response.class);
        } catch(HttpClientErrorException httpExp) {
            log.error("Logging out failed. ", httpExp);
            if(httpExp.getStatusCode() != null && httpExp.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                throw new AtgRuntimeException("API SignIn Service not avialable");
            } else if(httpExp.getStatusCode() != null && httpExp.getStatusCode().equals(HttpStatus.NOT_ACCEPTABLE)) {
                throw new InvalidParametersException();
            } else if(httpExp.getStatusCode() != null && httpExp.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {
                throw new UnauthorizedException("Invalid or expired token.");
            }else {
                throw new AtgRuntimeException("API SignIn Service not avialable");
            }
        }catch(Exception e) {
            log.error("SignIn client data API call failed. ", e);
            throw new AtgRuntimeException("API SignIn Service call failed.");
        }
        if(resp == null) {
            log.error("Signin client data API call failed.  The response was invalid. ");
            throw new AtgRuntimeException("Signin client data API call failed. The response was invalid ");
        }
        if(resp.getStatus() == null) {
            throw new AtgRuntimeException("Invalid response");
        } else if(resp.getStatus().equals(Response.ResponseStatus.AUTH_FAILED.getCode())) {
            throw new UnauthorizedException("Invalid response");
        } else if(resp.getStatus().equals(Response.ResponseStatus.DATA_INTEGRATY.getCode())) {
            throw new InvalidParametersException();
        } else if(resp.getStatus().equals(Response.ResponseStatus.DUPLICATE.getCode())) {
            throw new InvalidParametersException();
        } else if(resp.getStatus().equals(Response.ResponseStatus.GENERAL_ERROR.getCode())) {
            throw new AtgRuntimeException("Unknown error");
        } else if(resp.getStatus().equals(Response.ResponseStatus.SYSTEM.getCode())) {
            throw new AtgRuntimeException("Sysetm error");
        }

        if(resp.getStatus().equals(Response.ResponseStatus.SUCESS.getCode())) {
            log.debug("End logout() ");
            return resp;
        } else {
            throw new AtgRuntimeException("Unknown Error");
        }
    }


    public AuthResponse isAuthenticated(final String token) throws
            AtgRuntimeException, UnauthorizedException, InvalidParametersException {
        log.debug("Start isAuthenticated() ");
        if( token == null ) {
            throw new InvalidParametersException("Can not verifiy a NULL token.");
        }
        AuthResponse resp =null;
        try {
            resp =  makeCall("/"+version + SIGNIN_AUTHENTICATED_URI + token +"/",
                    HttpMethod.GET, AuthResponse.class);
        } catch(HttpClientErrorException httpExp) {
            log.error("Verifying token failed. ", httpExp);
            if(httpExp.getStatusCode() != null && httpExp.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                throw new AtgRuntimeException("API SignIn Service not avialable");
            } else if(httpExp.getStatusCode() != null && httpExp.getStatusCode().equals(HttpStatus.NOT_ACCEPTABLE)) {
                throw new InvalidParametersException();
            } else if(httpExp.getStatusCode() != null && httpExp.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {
                throw new UnauthorizedException("Invalid or expired token.");
            }else {
                throw new AtgRuntimeException("API SignIn Service not avialable");
            }
        }catch(Exception e) {
            log.error("SignIn client data API call failed. ", e);
            throw new AtgRuntimeException("API SignIn Service call failed.");
        }
        if(resp == null) {
            log.error("SignIn client data API call failed.  The response was invalid. ");
            throw new AtgRuntimeException("SignIn client data API call failed. The response was invalid ");
        }
        if(resp.getStatus() == null) {
            throw new AtgRuntimeException("Invalid response");
        } else if(resp.getStatus().equals(Response.ResponseStatus.AUTH_FAILED.getCode())) {
            throw new UnauthorizedException("Invalid response");
        } else if(resp.getStatus().equals(Response.ResponseStatus.DATA_INTEGRATY.getCode())) {
            throw new InvalidParametersException();
        } else if(resp.getStatus().equals(Response.ResponseStatus.DUPLICATE.getCode())) {
            throw new InvalidParametersException();
        } else if(resp.getStatus().equals(Response.ResponseStatus.GENERAL_ERROR.getCode())) {
            throw new AtgRuntimeException("Unknown error");
        } else if(resp.getStatus().equals(Response.ResponseStatus.SYSTEM.getCode())) {
            throw new AtgRuntimeException("Sysetm error");
        }

        if(resp.getStatus().equals(Response.ResponseStatus.SUCESS.getCode())) {
            log.debug("End isAuthenticated() ");
            return resp;
        } else {
            throw new AtgRuntimeException("Unknown Error");
        }
    }


    public String getVersion() {
        return version;
    }


    public void setVersion(String version) {
        this.version = version;
    }


}

