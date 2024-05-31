package com.livevox.is.metricsservice.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response implements Serializable {

    protected String apiSessionId;
    private Integer status;
    private Map<String, String> errorMessages;
    private Integer clientId;
    private Long id;

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

        ResponseStatus(Integer c) {
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
