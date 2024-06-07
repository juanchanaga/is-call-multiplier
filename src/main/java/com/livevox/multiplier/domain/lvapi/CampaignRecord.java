/*
 * Copyright (c) 2020. Luis Felipe Sosa Alvarez. All rights reserved to LiveVox.
 * Use this subject to license terms.
 *
 * IS-CALL-MULTIPLIER
 */

package com.livevox.multiplier.domain.lvapi;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.livevox.integration.commons.domain.lvapi.Account;
import com.livevox.integration.commons.domain.lvapi.Phone;
import lombok.Data;
import org.apache.commons.collections.CollectionUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class CampaignRecord implements Serializable {

    public static final String DELIMITER = "|";

    private String phone1;
    private String phone2;
    private String account;
    private String firstName;
    private String lastName;
    private String dob;
    private String email;
    private String ssn;
    private Integer serviceId;
    private Timestamp lastPaymentDate;
    private BigDecimal totalAmount;
    private BigDecimal minimumPaymentAmount;
    private String accountToSpeak;
    private BigDecimal amountToSpeak;
    private String language;
    private String extra1;
    private String extra2;
    private String extra3;
    private String extra4;
    private String extra5;
    private String extra6;
    private String extra7;
    private String extra8;
    private String extra9;
    private String extra11;
    private String extra12;
    private String extra13;
    private String extra14;
    private String extra15;
    private String extra16;
    private String extra19;
    private String extra20;
    private String originalAccountNumber;
    private Integer practiceId;
    private String clientPracticeId;
    private Integer templateId;
    private String practicePhone;
    private String practicePhoneAlternate;
    private String operatorNumber;
    private Integer placeOfServiceId;
    private Integer altLanguage1;
    private Integer altLanguage2;
    private Integer altLanguage3;
    private String languageCallback1;
    private String languageCallback2;
    private String languageCallback3;

    public CampaignRecord() {
    }

    public CampaignRecord(CallDetail call) {
        this();
        if (call == null) {
            return;
        }
        setAccount(call.getAccount());
        setOriginalAccountNumber(call.getOriginalAccount());
        setPhone1(call.getPhoneDialed());
        setAccountToSpeak(call.getAccount());
        setOperatorNumber(call.getOperatorPhone());
        setEmail(call.getEmail());
    }

    public CampaignRecord(Account acct) {
        this();
        if (acct == null) {
            return;
        }
        setAccount(acct.getAccount());
        setOriginalAccountNumber(acct.getOrigAccount());
        if (acct.getPhoneNumbers() != null) {
            populatePhoneFieldsFromPhoneList(acct.getPhoneNumbers());
        }
        setAccountToSpeak(acct.getAccountToSpeak());
        if (acct.getPerson() != null && acct.getPerson().getDateOfBirth() != null) {
            setDob(String.valueOf(acct.getPerson().getBirthday().getTime()));
        }
        setAccountToSpeak(acct.getAccountToSpeak());
        setAmountToSpeak(acct.getAmountToSpeak());
        setFirstName(acct.getFirstName());
        setLastName(acct.getLastName());
        setTotalAmount(acct.getBalance());
        if (CollectionUtils.isNotEmpty(acct.getEmails())) {
            setEmail(acct.getEmails().get(0).getEmail());
        }
    }


    public CampaignRecord(Contact cnt) {
        this();
        if (cnt == null) {
            return;
        }
        setAccount(cnt.getAccount());
        setOriginalAccountNumber(cnt.getOriginalAccountNumber());
        if (cnt.getPhone() != null) {
            populatePhoneFieldsFromPhoneList(cnt.getPhone());
        }
        setAccountToSpeak(cnt.getAccountToSpeak());
        if (cnt.getPerson() != null) {
            if( cnt.getPerson().getDateOfBirth() != null) {
                setDob(String.valueOf(cnt.getPerson().getBirthday().getTime()));
            }
            setFirstName(cnt.getPerson().getFirstName() );
            setLastName(cnt.getPerson().getLastName());
        }
        setAccountToSpeak(cnt.getAccountToSpeak());
        setAmountToSpeak(cnt.getAmountToSpeak());
        setTotalAmount(cnt.getBalance());

    }
    /**
     * The platform uses the phone1 field for the primary phone number and
     * phone2 as a pipe delimited list of additional phone numbers. This method
     * populates those fields based on the List passed to it. The first phone
     * goes to phone1, the additional fields are made into a pipe delimited list
     * put in to phone2
     *
     * @param phoneList
     */
    public void populatePhoneFieldsFromStrList(List<String> phoneList) {
        if (phoneList == null) {
            return;
        }
        if (phoneList.size() > 0) {
            setPhone1(phoneList.get(0));
        }
        if (phoneList.size() > 1) {
            setPhone2(phoneList.get(1));
        }
    }

    /**
     * The platform uses the phone1 field for the primary phone number and
     * phone2 as a pipe delimited list of additional phone numbers. This method
     * populates those fields based on the List passed to it. The first phone
     * goes to phone1, the additional fields are made into a pipe delimited list
     * put in to phone2
     *
     * @param phoneList
     */
    public void populatePhoneFieldsFromPhoneList(List<Phone> phoneList) {
        if (phoneList == null) {
            return;
        }
        if (phoneList.size() > 0) {
            setPhone1(phoneList.get(0).getPhone());
        }
        if (phoneList.size() > 1) {
            setPhone2(phoneList.get(1).getPhone());
        }
    }
}
