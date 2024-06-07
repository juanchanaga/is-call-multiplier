/*
 * Copyright (c) 2020. Luis Felipe Sosa Alvarez. All rights reserved to LiveVox.
 * Use this subject to license terms.
 *
 * IS-CALL-MULTIPLIER
 */

package com.livevox.multiplier.dao;

import javax.sql.DataSource;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface PropertyDao extends Serializable {
    void setDataSource(DataSource var1);

    Map<String, String> getAllPropertyies() throws SQLException;

    boolean isProperty(String var1);

    boolean isPropertyRealtime(String var1) throws SQLException;

    boolean hasValue(String var1);

    boolean hasValueRealtime(String var1) throws SQLException;

    String getProperty(String var1);

    String getProperty(String var1, String var2);

    List<String> getMatchingProperty(String var1);

    String getPropertyRealtime(String var1) throws SQLException;

    String getRequiredProperty(String var1) throws IllegalStateException;

    String getRequiredPropertyRealtime(String var1) throws IllegalStateException, SQLException;

    boolean addProperty(String var1, String var2) throws SQLException;

    boolean addOrUpdateProperty(String var1, String var2) throws SQLException;

    boolean updateProperty(String var1, String var2) throws SQLException;

    boolean deleteProperty(String var1) throws SQLException;

    void setMinutesToCacheProperties(Integer var1);

    void forceCacheRefresh() throws SQLException;

    Integer getClientProperty(String var1, Integer var2);

    String getClientPropertyString(String var1, Integer var2);

    boolean saveClientProperty(String var1, Integer var2, String var3);

    boolean deleteClientProperty(String var1, Integer var2);

    boolean getClientPropertyBoolean(String var1, Integer var2);

    boolean isClientProperty(String var1, Integer var2);

    List<String> getPropertyList(String var1);

    List<String> getPropertyList(String var1, String var2);

    List<String> getRequiredPropertyList(String var1);

    List<String> getRequiredPropertyList(String var1, String var2);
}
