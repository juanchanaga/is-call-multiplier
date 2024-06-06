/*
 * Copyright (c) 2020. Luis Felipe Sosa Alvarez. All rights reserved to LiveVox.
 * Use this subject to license terms.
 *
 * IS-CALL-MULTIPLIER
 */

package com.livevox.integration.commons.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class JsonMarshaller {

    private static final Logger log = LoggerFactory.getLogger(JsonMarshaller.class);


    public static <T> T jsonToObj(String json, TypeReference<?> type) throws JsonMappingException {
        if(json == null || type == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        setConfigOptions(mapper);
        try {
            return (T) mapper.readValue(new StringReader(json), type);
        } catch (Exception e) {
            log.error("An error occured Marshalling an object. "+e+" "+e.getMessage());
            throw JsonMappingException.from(mapper.getDeserializationContext(),
                    "Marshalling object of type "+type.getClass().getName()+" failed. ");
        }
    }


    public static <T> T jsonToObj(String json, JavaType type) throws JsonMappingException {
        if(json == null || type == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        setConfigOptions(mapper);
        try {
            return mapper.readValue(new StringReader(json), type) ;
        } catch (Exception e) {
            log.error("An error occured Marshalling an object. "+e+" "+e.getMessage());
            throw JsonMappingException.from(mapper.getDeserializationContext(),
                    "Marshalling object of type "+type.getClass().getName()+" failed. ");
        }
    }


    public static <T> T jsonToObj(String json, Class<T> clsType) throws JsonMappingException {
        if(json == null || clsType == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        setConfigOptions(mapper);
        try {
            return mapper.readValue(new StringReader(json), clsType) ;
        } catch (Exception e) {
            log.error("An error occured Marshalling an object. "+e+" "+e.getMessage());
            throw JsonMappingException.from(mapper.getDeserializationContext(),
                    "Marshalling object of type "+clsType.getName()+" failed. ");
        }
    }


    public static String ObjToJson(Object obj) throws JsonMappingException {
        if(obj == null ) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        setConfigOptions(mapper);
        StringWriter stringWriter = new StringWriter();
        try {
            mapper.writeValue(stringWriter, obj);
            return stringWriter.toString();
        } catch (Exception e) {
            log.error("An error occured unMarshalling object "+obj+"   "+e+" "+e.getMessage());
            throw JsonMappingException.from(mapper.getDeserializationContext(),
                    "Marshalling object of type "+obj.getClass().getName()+" failed. ");
        }
    }


    public static void setConfigOptions(ObjectMapper mapper) {
        if(mapper == null) {
            return;
        }
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        mapper.configure(SerializationFeature.WRAP_EXCEPTIONS, false);
        mapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
        mapper.configure(SerializationFeature.FLUSH_AFTER_WRITE_VALUE, true);
        mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        mapper.configure(DeserializationFeature.READ_ENUMS_USING_TO_STRING, true);
        mapper.configure(DeserializationFeature.WRAP_EXCEPTIONS, false);
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);

    }

    /**
     * Accepts a String formatted in key-value pairs, in the format "accountNumber=123&clientId=12345"
     * and maps these fields to a Java object.
     */
    public static <T> T kvpToObject(String keyValueText, Class<T> clsType) throws IOException {
        if (keyValueText == null || clsType == null) {
            return null;
        }
        String[] keyValuePairs = keyValueText.split("&");
        Map<String, String> map = new HashMap<>();
        for (String pair : keyValuePairs) {
            String[] entry = pair.split("=");
            try {
                map.put(entry[0].trim(), entry[1].trim());
            } catch (ArrayIndexOutOfBoundsException e) {
                log.error("Problem splitting key-value pair for key: " + entry[0]);
                throw e;
            }
        }
        ObjectMapper mapper = new ObjectMapper();
        setConfigOptions(mapper);
        String jsonFromMap = null;
        try {
            jsonFromMap = mapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            log.error("Problem writing map to JSON: " + cleanException(e.getMessage()));
            throw e;
        }
        try {
            return mapper.readValue(jsonFromMap, clsType);
        } catch (IOException e) {
            log.error("Problem mapping JSON to Object: " + cleanException(e.getMessage()));
            throw e;
        }
    }

    public static String cleanException(String msg) {
        // This targets UnrecognizedPropertyException specifically, cutting off message after unrecognized field problem
        if (msg.contains("ignorable")) {
            String[] parts = msg.split("(?<=ignorable)");
            return parts[0];
        }

        // Just in case replacements
        msg = msg.replaceAll("(\\d{12})(\\d{4})", "************$2"); // Removing most of credit card number
        msg = msg.replaceAll("(?i)(\"password\":)(\".*?\")", "$1\"********\""); // Remove password

        return msg;
    }

}

