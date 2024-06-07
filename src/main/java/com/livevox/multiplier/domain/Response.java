/*
 * Copyright (c) 2020. Luis Felipe Sosa Alvarez. All rights reserved to LiveVox.
 * Use this subject to license terms.
 *
 * IS-CALL-MULTIPLIER
 */

package com.livevox.multiplier.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;


/**
 * The type Response.
 *
 * @autor Luis Felipe Sosa Alvarez lsosa@livevox.com
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response implements Serializable {

    private static final long serialVersionUID = 9216967110059145775L;
    /**
     * The Api session id.
     */
    protected String apiSessionId;
    private Integer status;
    private Map<String, String> errorMessages;
    private Integer clientId;
    private Long id;

    /**
     * Has errors boolean.
     *
     * @return the boolean
     */
    public boolean hasErrors() {
        return this.errorMessages != null && !this.errorMessages.isEmpty();
    }

    /**
     * Getter method that maps to sessionID for universalLogin external service
     *
     * @return apiSessionID
     */
    public String getSessionId() {
        return this.apiSessionId;
    }

    /**
     * Setter method that maps to sessionID for universalLogin external service
     *
     * @param apiSessionId
     */
    public void setSessionId(String apiSessionId) {
        this.apiSessionId = apiSessionId;
    }

    /**
     * Gets api session id.
     *
     * @return the api session id
     */
    public String getApiSessionId() {
        return this.apiSessionId;
    }

    /**
     * Sets api session id.
     *
     * @param apiSessionId the api session id
     */
    public void setApiSessionId(String apiSessionId) {
        this.apiSessionId = apiSessionId;
    }

    /**
     * Gets status.
     *
     * @return the status
     */
    public Integer getStatus() {
        return this.status;
    }

    /**
     * Sets status.
     *
     * @param status the status
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * Gets client id.
     *
     * @return the client id
     */
    public Integer getClientId() {
        return this.clientId;
    }

    /**
     * Sets client id.
     *
     * @param clientId the client id
     */
    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public Long getId() {
        return this.id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets error messages.
     *
     * @return the error messages
     */
    public Map<String, String> getErrorMessages() {
        return this.errorMessages;
    }

    /**
     * Sets error messages.
     *
     * @param errorMessages the error messages
     */
    public void setErrorMessages(Map<String, String> errorMessages) {
        this.errorMessages = errorMessages;
    }

    /**
     * Add error message.
     *
     * @param errorMessage the error message
     */
    public void addErrorMessage(String errorMessage) {
        this.addErrorMessage((String)null, errorMessage);
    }

    /**
     * Add error message.
     *
     * @param fieldId      the field id
     * @param errorMessage the error message
     */
    public void addErrorMessage(String fieldId, String errorMessage) {
        if (this.errorMessages == null) {
            this.errorMessages = new TreeMap();
        }

        if (fieldId == null) {
            fieldId = this.createKey().toString();
        }

        this.errorMessages.put(fieldId, errorMessage);
    }

    private Integer createKey() {
        if (this.errorMessages != null && this.errorMessages.keySet() != null) {
            for(int newKey = 0; newKey < 1000; ++newKey) {
                boolean keyExists = false;
                Iterator var3 = this.errorMessages.keySet().iterator();

                while(var3.hasNext()) {
                    String k = (String)var3.next();
                    if (k != null && k.equals(String.valueOf(newKey))) {
                        keyExists = true;
                        break;
                    }
                }

                if (!keyExists) {
                    return newKey;
                }
            }

            throw new IllegalStateException("Could not find a key to generate for error list.");
        } else {
            return 0;
        }
    }

    /**
     * The enum Response status.
     */
    public static enum ResponseStatus {
        /**
         * Sucess response status.
         */
        SUCESS(0),
        /**
         * Auth failed response status.
         */
        AUTH_FAILED(1),
        /**
         * System response status.
         */
        SYSTEM(2),
        /**
         * Bad request response status.
         */
        BAD_REQUEST(3),
        /**
         * General error response status.
         */
        GENERAL_ERROR(4),
        /**
         * Duplicate response status.
         */
        DUPLICATE(5),
        /**
         * Data integraty response status.
         */
        DATA_INTEGRATY(6);

        private Integer code;

        private ResponseStatus(Integer c) {
            this.code = c;
        }

        /**
         * Gets code.
         *
         * @return the code
         */
        public Integer getCode() {
            return this.code;
        }
    }
}
