/*
 * Copyright (c) 2020. Luis Felipe Sosa Alvarez. All rights reserved to LiveVox.
 * Use this subject to license terms.
 *
 * IS-CALL-MULTIPLIER
 */

package com.livevox.multiplier.domain.lvapi;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.livevox.multiplier.domain.enums.AddressType;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Address {

    private Long id;
    private AddressType type;
    private String address;
    private String address2;
    private String city;
    private String state;
    private String postalCode;
    private String county;
    private String country;
}
