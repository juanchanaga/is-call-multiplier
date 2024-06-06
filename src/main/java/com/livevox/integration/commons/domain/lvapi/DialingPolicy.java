/*
 * Copyright (c) 2020. Luis Felipe Sosa Alvarez. All rights reserved to LiveVox.
 * Use this subject to license terms.
 *
 * IS-CALL-MULTIPLIER
 */

package com.livevox.integration.commons.domain.lvapi;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.livevox.integration.commons.domain.enums.DayOfWeek;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class DialingPolicy extends ListRequest implements Serializable {

    private Long id;
    private Long profileId;
    private DayOfWeek dayOfWeek;
    private Boolean doNotDial;
    private Boolean doNotLeaveMessage;
    private String state;
    private String areaCode;
    private Date startTime;
    private Date endTime;
    private Boolean clearAreaCode;
    private Boolean clearTimeRange;

    public boolean validCreate() {
        return (profileId != null && dayOfWeek != null && doNotDial != null &&
                doNotLeaveMessage != null);
    }

    public boolean validUpdate() {
        return  ( validCreate() && id != null );
    }
}
