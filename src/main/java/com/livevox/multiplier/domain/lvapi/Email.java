/*
 * Copyright (c) 2020. Luis Felipe Sosa Alvarez. All rights reserved to LiveVox.
 * Use this subject to license terms.
 *
 * IS-CALL-MULTIPLIER
 */

package com.livevox.multiplier.domain.lvapi;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.livevox.multiplier.domain.enums.EmailType;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Email {

    private Long id;
    private Integer order;
    private String email;
    private EmailType type;
    public Email() {}
    public Email(String email) {
        this.email = email;
    }
}
