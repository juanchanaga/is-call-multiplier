/*
 * Copyright (c) 2020. Luis Felipe Sosa Alvarez. All rights reserved to LiveVox.
 * Use this subject to license terms.
 *
 * INTEGRATION-CALL-MULTIPLIER-SERVICE
 */

package com.livevox.is.metricsservice.session;


import com.livevox.is.metricsservice.domain.LvSession;
import com.livevox.is.metricsservice.exceptions.AuthenticationException;
import com.livevox.is.metricsservice.exceptions.InvalidParametersException;
import com.livevox.is.metricsservice.exceptions.UnauthorizedException;

import javax.xml.ws.WebServiceException;
import java.io.Serializable;

public interface LvSessionRestService extends Serializable {
    LvSession login(String var1, String var2, String var3) throws WebServiceException, AuthenticationException;

    LvSession login(String var1, String var2, String var3, String var4) throws WebServiceException, AuthenticationException;

    LvSession login(String var1, String var2, String var3, boolean var4, String var5, String var6, String var7) throws WebServiceException, AuthenticationException, InvalidParametersException;

    String getApiSessionId(String var1, String var2, String var3) throws WebServiceException, AuthenticationException;

    String getApiSessionId(String var1, String var2, String var3, String var4) throws WebServiceException, AuthenticationException;

    boolean isValidApiSession(String var1) throws WebServiceException, AuthenticationException;

    boolean isValidApiSession(String var1, String var2) throws WebServiceException, AuthenticationException;

    boolean isValidApiSession(String var1, String var2, String var3) throws WebServiceException, AuthenticationException;

    void logout(String var1) throws WebServiceException, UnauthorizedException;

    void logout(String var1, String var2) throws WebServiceException, UnauthorizedException;

    void logout(LvSession var1) throws WebServiceException, AuthenticationException;

    void logout(String var1, String var2, String var3) throws WebServiceException, UnauthorizedException;

    boolean changePassword(String var1, String var2, String var3, String var4, boolean var5);

    boolean changePassword(String var1, String var2, String var3, String var4, boolean var5, String var6);

    boolean changePassword(LvSession var1);

    LvSession login(String var1, String var2, String var3, boolean var4) throws WebServiceException, AuthenticationException;

    LvSession login(LvSession var1) throws WebServiceException, AuthenticationException;

    LvSession login(String var1, String var2, String var3, boolean var4, String var5) throws WebServiceException, AuthenticationException;

    void refreshExpiredSession(LvSession var1) throws WebServiceException;

    boolean changePassword(LvSession var1, String var2, String var3, String var4);
}
