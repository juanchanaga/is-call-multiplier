/*
 * Copyright (c) 2020. Luis Felipe Sosa Alvarez. All rights reserved to LiveVox.
 * Use this subject to license terms.
 *
 * IS-CALL-MULTIPLIER
 */

package com.livevox.integration.commons.domain.lvapi;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.livevox.integration.commons.domain.stats.DefaultDao;
import com.livevox.integration.commons.domain.stats.Skill;
import lombok.Data;

import java.util.List;

@JsonInclude(Include.NON_NULL)
@Data
public class ScreenPop extends DefaultDao {

    private static final long serialVersionUID = 863431824451740829L;

    private String description;
    private List<Skill> associatedService;
    private List<ScreenPopDetail> infoField;
    private List<ScreenPopDetail> dotNetData;
    private String name;
    private Integer clientId;
    private Boolean associatedToClient;
    private Boolean notesEnabled;
    private Boolean callRecordControlEnabled;
    private Boolean displayNotReadySubCodes;
    private Boolean acctNumberRequired;

}
