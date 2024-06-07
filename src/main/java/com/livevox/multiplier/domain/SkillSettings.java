/*
 * Copyright (c) 2021. Luis Felipe Sosa Alvarez. All rights reserved to LiveVox.
 * Use this subject to license terms.
 *
 * IS-CALL-MULTIPLIER
 */

package com.livevox.multiplier.domain;

import com.livevox.multiplier.domain.stats.Skill;
import lombok.Data;

@Data
public class SkillSettings extends Skill {


    /**
     *
     */
    private static final long serialVersionUID = -2473891857595542779L;
    //Read Service Settings
    private Long screenPopId;
    private Boolean cycleSortDaily;
    private Integer serviceLevelSeconds;
    private Integer accountMaxAttemptsPerDay;
    private Integer dialingProfileId;
    private Integer dialingRestriction;
    private String dialingSort;
    private String zipAreaMismatch;
    private Integer voiceId;
    private Boolean scrubWireless;
    private String answeringMachineOption;
    private Boolean transferOptionEnabled;
    private Boolean crossRequeueable;
    private Boolean volumeControlEnabled;
    private Boolean acdScheduledCallbackPhReadonly;
    private String acdScheduledCallback;
    private Boolean acdScheduledCallbackPreviewEnabled;
    private Boolean acdPtpEnabled;
    private Boolean previewManualAllowed;
    private Boolean previewSkipAllowed;
    private Boolean previewConfirmDial;
    private Integer previewTimeout;
    private String previewDialAction;
    private Boolean callRecordingEnabled;
    private Boolean acctRealtimeDnc;
    private Boolean callAcceptanceEnabled;
    private Integer callAcceptanceTimeout;
    private Integer callerIdSourceId;
    private Boolean callbackPhoneSourceId;
    private Boolean dialTollFree;
    private Integer lcidPackageId;
    private Integer operatorPhoneSourceId;
    private Boolean leaveMessages;
}
