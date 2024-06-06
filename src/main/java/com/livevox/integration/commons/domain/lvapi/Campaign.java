/*
 * Copyright (c) 2020. Luis Felipe Sosa Alvarez. All rights reserved to LiveVox.
 * Use this subject to license terms.
 *
 * IS-CALL-MULTIPLIER
 */

package com.livevox.integration.commons.domain.lvapi;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.livevox.integration.commons.domain.stats.DefaultDao;
import lombok.Data;

import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Campaign extends DefaultDao {

    private static final long serialVersionUID = 2516253646716057224L;
    private String name;
    private Integer typeId;
    private Long serviceId;
    private String status;
    private Date uploadDate;

}
