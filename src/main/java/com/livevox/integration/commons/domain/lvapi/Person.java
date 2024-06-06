/*
 * Copyright (c) 2020. Luis Felipe Sosa Alvarez. All rights reserved to LiveVox.
 * Use this subject to license terms.
 *
 * IS-CALL-MULTIPLIER
 */

package com.livevox.integration.commons.domain.lvapi;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Date;
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Person {

    public enum Gender {
        MALE,
        FEMALE;
    }

    private Long id;
    private String firstName;
    private String lastName;
    private String middleName;
    private Gender gender;
    private Phone phone1;
    private Phone phone2;
    private Phone phone3;
    private Phone phone4;
    private Phone phone5;
    private Address address;
    private Address address1;
    private Address address2;
    private Address address3;
    private Address address4;
    private Address address5;
    private String email;
    private Email email1;
    private Email email2;
    private Email email3;
    private Email email4;
    private Email email5;
    private String zipCode;
    private Date birthday;
    private Date dateOfBirth;
    private String accountNumber;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Person person = (Person) o;

        return new EqualsBuilder()
                .append(id, person.id)
                .append(firstName, person.firstName)
                .append(lastName, person.lastName)
                .append(middleName, person.middleName)
                .append(gender, person.gender)
                .append(phone1, person.phone1)
                .append(phone2, person.phone2)
                .append(phone3, person.phone3)
                .append(phone4, person.phone4)
                .append(phone5, person.phone5)
                .append(address, person.address)
                .append(address1, person.address1)
                .append(address2, person.address2)
                .append(address3, person.address3)
                .append(address4, person.address4)
                .append(address5, person.address5)
                .append(email, person.email)
                .append(email1, person.email1)
                .append(email2, person.email2)
                .append(email3, person.email3)
                .append(email4, person.email4)
                .append(email5, person.email5)
                .append(zipCode, person.zipCode)
                .append(birthday, person.birthday)
                .append(dateOfBirth, person.dateOfBirth)
                .append(accountNumber, person.accountNumber)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(firstName)
                .append(lastName)
                .append(middleName)
                .append(gender)
                .append(phone1)
                .append(phone2)
                .append(phone3)
                .append(phone4)
                .append(phone5)
                .append(address)
                .append(address1)
                .append(address2)
                .append(address3)
                .append(address4)
                .append(address5)
                .append(email)
                .append(email1)
                .append(email2)
                .append(email3)
                .append(email4)
                .append(email5)
                .append(zipCode)
                .append(birthday)
                .append(dateOfBirth)
                .append(accountNumber)
                .toHashCode();
    }
}
