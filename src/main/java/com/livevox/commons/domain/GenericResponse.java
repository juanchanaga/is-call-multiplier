/*
 * Copyright (c) 2020. Luis Felipe Sosa Alvarez. All rights reserved to LiveVox.
 * Use this subject to license terms.
 *
 * IS-CALL-MULTIPLIER
 */

package com.livevox.commons.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Generic response.
 *
 * @param <T> the type parameter
 * @autor Luis Felipe Sosa Alvarez lsosa@livevox.com
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GenericResponse <T> extends Response implements Serializable {

    private static final long serialVersionUID = 6967189743775L;
    private List<T> data;
    private Integer totalCount;

    /**
     * Instantiates a new Generic response.
     */
    public GenericResponse() {
    }

    /**
     * Instantiates a new Generic response.
     *
     * @param data the data
     */
    @JsonCreator
    public GenericResponse(@JsonProperty("data") List<T> data) {
        this.data = data;
    }

    /**
     * Gets data.
     *
     * @return the data
     */
    public List<T> getData() {
        return this.data;
    }

    /**
     * Sets data.
     *
     * @param data the data
     */
    public void setData(List<T> data) {
        this.data = data;
        if (data != null) {
            this.totalCount = data.size();
        }

    }

    /**
     * Add data.
     *
     * @param t the t
     */
    public void addData(T t) {
        if (this.data == null) {
            this.data = new ArrayList();
        }

        this.data.add(t);
        this.totalCount = this.data.size();
    }

    /**
     * Gets total count.
     *
     * @return the total count
     */
    public Integer getTotalCount() {
        return this.totalCount;
    }

    /**
     * Sets total count.
     *
     * @param totalCount the total count
     */
    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

}
