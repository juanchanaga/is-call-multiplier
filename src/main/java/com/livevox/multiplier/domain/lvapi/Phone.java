/*
 * Copyright (c) 2020. Luis Felipe Sosa Alvarez. All rights reserved to LiveVox.
 * Use this subject to license terms.
 *
 * IS-CALL-MULTIPLIER
 */

package com.livevox.multiplier.domain.lvapi;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.livevox.multiplier.domain.enums.DncType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Phone {
    public enum PhoneNumberType {
        CELL,
        LANDLINE;
    }
    public enum PhoneType {
        PERSONAL,
        WORK;
    }
    private Long id;
    private String phone;
    private PhoneNumberType phoneNumberType;
    private PhoneType phoneType;
    private Integer ordinal;
    private String description;
    private DncType phoneBlock;
    private Boolean cellConsent;
    private Boolean smsConsent;

    public Phone(String phoneNumber) {
        setPhone(phoneNumber);
    }


    public Phone(String phoneNumber, Integer ordinal) {
        this(phoneNumber);
        setOrdinal( ordinal );
    }


    public void setPhone(String phoneNumber) {
        if(phoneNumber != null ) {
            this.phone = phoneNumber.replaceAll("[^\\d]", "");
        }
        this.phone = phoneNumber;
    }

}
