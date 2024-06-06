/*
 * Copyright (c) 2020. Luis Felipe Sosa Alvarez. All rights reserved to LiveVox.
 * Use this subject to license terms.
 *
 * IS-CALL-MULTIPLIER
 */

package com.livevox.integration.commons.domain.lvapi;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.livevox.integration.commons.domain.stats.DefaultDao;
import com.livevox.integration.commons.domain.stats.IdType;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class Agent extends DefaultDao implements Serializable {

    private static final long serialVersionUID = 7076775166194013510L;
    private Long iId;
    private String loginId;
    private String firstName;
    private String lastName;
    private String phone;
    private Long wrapUpTime;
    private Boolean homeAgent;
    private Boolean active;
    private Boolean locked;
    private Integer clientId;
    private Date passwordLastModified;
    private List<IdType> assignedService;
}
