/*
 * Copyright (c) 2020. Luis Felipe Sosa Alvarez. All rights reserved to LiveVox.
 * Use this subject to license terms.
 *
 * INTEGRATION-CALL-MULTIPLIER-SERVICE
 */

package com.livevox.is.metricsservice.exceptions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"cause", "localizedMessage", "message", "stackTrace", "suppressed", "error"})
public class AtgRuntimeException extends RuntimeException {
    private static final long serialVersionUID = -5590780108976141064L;
    private static final int HTTP_STATUS = 500;
    private static final String ERROR_MESSAGE = "Internal Server Error";
    private String errorMsg;
    private Integer httpStatus;

    public AtgRuntimeException() {
        this.setErrorMsg(this.getError());
    }

    public AtgRuntimeException(String message, int httpStatus) {
        super(message);
        this.setErrorMsg(message);
        this.setHttpStatus(httpStatus);
    }

    public AtgRuntimeException(String message) {
        super(message);
        this.setErrorMsg(message);
    }

    public AtgRuntimeException(Exception e) {
        super(e);
        this.setErrorMsg(e.getClass().getName() + " " + e.getMessage());
    }

    public AtgRuntimeException(Throwable e) {
        super(e);
        this.setErrorMsg(e.getClass().getName() + " " + e.getMessage());
    }

    @JsonIgnore
    public synchronized Throwable getCause() {
        return super.getCause();
    }

    @JsonIgnore
    public StackTraceElement[] getStackTrace() {
        return super.getStackTrace();
    }

    @JsonIgnore
    public Throwable fillInStackTrace() {
        return this;
    }

    public int getHttpStatus() {
        return this.httpStatus != null ? this.httpStatus : 500;
    }

    public String getError() {
        return "Internal Server Error";
    }

    public ResponseEntity<Object> asResponseEntity() {
        return new ResponseEntity(this, HttpStatus.valueOf(this.getHttpStatus()));
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getErrorMsg() {
        return StringUtils.isEmpty(this.errorMsg) ? this.getError() : this.errorMsg;
    }

    public void setHttpStatus(Integer httpStatus) {
        this.httpStatus = httpStatus;
    }
}
