/*
 * Copyright (c) 2020. Luis Felipe Sosa Alvarez. All rights reserved to LiveVox.
 * Use this subject to license terms.
 *
 * IS-CALL-MULTIPLIER
 */

package com.livevox.multiplier.domain.lvapi;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.livevox.multiplier.domain.enums.CountryType;
import com.livevox.integration.commons.domain.enums.DncType;
import com.livevox.integration.commons.domain.lvapi.Phone;
import com.livevox.integration.commons.domain.stats.DefaultDao;
import com.livevox.multiplier.utils.JsonMarshaller;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class Contact extends DefaultDao implements Cloneable, Serializable {

    private String account;
    private Boolean active;
    private BigDecimal balance;
    private Date initialLoad;
    private Date lastLoad;
    private DncType accountBlock;
    private String originalAccountNumber;
    private String accountToSpeak;
    private Person person;
    private Person guarantor;
    private List<Phone> phone;
    private String address1;
    private String address2;
    private String city;
    private String state;
    private CountryType country;
    private BigDecimal amountToSpeak;
    private Date modifyDate;
    private Date createDate;
    private Integer groupId;
    private Integer clientId;
    private Date dueDate;
    private CustomField[] customFields;
    private Boolean primaryEmailConsent;



    @Override
    public Contact clone() {
        try {
            return (Contact) super.clone();
        } catch (Exception e) {
            throw new IllegalStateException("Conact object failed to be cloned.", e);
        }
    }


    public String toJSON() throws JsonMappingException {
        return JsonMarshaller.ObjToJson(this);
    }

    public void addPhoneNumber(String phoneNumber, Integer order, DncType dncType) {
        if (!StringUtils.isEmpty(phoneNumber)) {
            Phone p = new Phone();
            p.setPhone(phoneNumber);
            p.setOrdinal(order);
            if(dncType != null) {
                p.setPhoneBlock(dncType);
            }
            if (phone == null) {
                phone = new ArrayList<>();
            }
            phone.add(p);
        }
    }

    public void setFirstName(String firstName) {
        if (firstName != null) {
            if (person == null) {
                person = new Person();
            }
            person.setFirstName(firstName);
        }
    }

    public void setLastName(String lastName) {
        if (lastName != null) {
            if (person == null) {
                person = new Person();
            }
            person.setLastName(lastName);
        }
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public String getAccountToSpeak() {
        return accountToSpeak;
    }

    public void setAccountToSpeak(String accountToSpeak) {
        this.accountToSpeak = accountToSpeak;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public CustomField[] getCustomFields() {
        return customFields;
    }

    public void setCustomFields(CustomField[] customFields) {
        this.customFields = customFields;
    }

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o, true);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this, true);
    }


    public Boolean getPrimaryEmailConsent() {
        return primaryEmailConsent;
    }


    public void setPrimaryEmailConsent(Boolean primaryEmailConsent) {
        this.primaryEmailConsent = primaryEmailConsent;
    }

}
