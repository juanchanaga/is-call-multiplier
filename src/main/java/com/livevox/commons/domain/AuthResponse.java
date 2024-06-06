/*
 * Copyright (c) 2020. Luis Felipe Sosa Alvarez. All rights reserved to LiveVox.
 * Use this subject to license terms.
 *
 * IS-CALL-MULTIPLIER
 */

package com.livevox.commons.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class AuthResponse extends Response implements Serializable {

    private static final long serialVersionUID = 3271644241788837L;


    private Long id;

    private Integer clientId;

    private String userName;

    private String clientName;

    private Date creationDate;

    private Date expirationDate;

    private boolean validToken;

    private Integer userId;

    private String application;

    private Boolean agent;

    private Integer daysUntilPasswordExpires;

    private User user;

    private String apiVersion;

    private List<LvSession> alternativeSessoins;

    private String salesForceId;

    private Boolean configApp = false;
}
