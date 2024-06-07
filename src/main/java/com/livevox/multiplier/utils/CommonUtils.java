/*
 * Copyright (c) 2021. Luis Felipe Sosa Alvarez. All rights reserved to LiveVox.
 * Use this subject to license terms.
 *
 * IS-CALL-MULTIPLIER
 */

package com.livevox.multiplier.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;


import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLDecoder;
import java.net.UnknownHostException;
import java.util.*;
import java.util.jar.Manifest;

@Slf4j
public class CommonUtils implements Serializable {

    private static final long serialVersionUID = 39467843259474799L;

    private CommonUtils() {
    }

    public static File moveFileForProcessing(File file, String newPath) throws IOException {
        return moveFileForProcessing(file, (newPath != null) ? new File(newPath) : null);
    }

    public static File moveFileForProcessing(File file, File newLocation) throws IOException {
        if (file == null || newLocation == null || !file.exists()) {
            if (file != null) {
                log.error("Failed to move file " + file.getAbsolutePath() + " it did not exist.");
            }
            return null;
        }

        File newPath = new File(newLocation.getParent());
        if (!newPath.exists()) {
            newPath.mkdirs();
        }
        if (newLocation.isDirectory()) {
            newLocation = new File(newLocation.getAbsoluteFile() + File.separator + file.getName());
        }
        log.debug("Moving file : " + file.getAbsolutePath() + " to " + newLocation);
        if( file.renameTo(newLocation) ) {
            return newLocation;
        } else {
            throw new IOException("Moving file failed.");
        }


    }

    public static String addTimeStampToFileName(final String name) {
        if (name == null) {
            return null;
        }
        if (name.lastIndexOf('.') > -1) {
            String fileName = FilenameUtils.getBaseName(name);
            String fileExt = FilenameUtils.getExtension(name);
            return fileName + "-" + new Date().getTime() + "." + fileExt;
        } else {
            return name + new Date().getTime();
        }
    }

    public static List<String> parseCommaDelimitedString(String commaString) {
        if (commaString == null) {
            return null;
        }
        List<String> finalStrings = new ArrayList<>();
        String[] splitStrings = commaString.split(",");
        if (splitStrings == null) {
            log.warn("The supplied string could not be parsed");
            return null;
        }
        for (String splitString : splitStrings) {
            if (splitString != null) {
                finalStrings.add(splitString.trim());
            }
        }
        return finalStrings;
    }

    public static String getHostName() {
        if (System.getenv()
                .containsKey("COMPUTERNAME")) {
            return System.getenv("COMPUTERNAME");
        } else if (System.getenv()
                .containsKey("HOSTNAME")) {
            return System.getenv("HOSTNAME");
        } else {
            try {
                return InetAddress.getLocalHost()
                        .getHostName();
            } catch (UnknownHostException e) {
                log.error("Unable to get hostname", e);
                return "Unknown Host";
            }
        }
    }

    public static String getPublicIpAddress() {
        try {
            HttpClient client = HttpClientBuilder.create()
                    .build();
            HttpGet request = new HttpGet("http://checkip.amazonaws.com");
            HttpResponse response = client.execute(request);
            return StringUtils.replace(EntityUtils.toString(response.getEntity()), "\n", "");
        } catch (Exception e) {
            log.error("Unable to get public ip", e);
            return null;
        }
    }

    public static Manifest getManfiest(String value) {
        return getManfiest(value, "Implementation-Title");
    }

    public static Manifest getManfiest(String title, String attribute) {
        try {
            for (Enumeration<URL> resources = CommonUtils.class.getClassLoader()
                    .getResources("META-INF/MANIFEST.MF"); resources.hasMoreElements();) {
                Manifest manifest = new Manifest(resources.nextElement()
                        .openStream());
                if (StringUtils.equals(manifest.getMainAttributes()
                        .getValue(attribute), title)) {
                    return manifest;
                }
            }
        } catch (Exception e) {
            log.error("Unable to get manfiest ip", e);
        }
        return null;
    }

    public static String getMajorVersion(String version) {
        String[] vals = StringUtils.remove(version, "-SNAPSHOT")
                .split("\\.");
        return vals[0] + "." + vals[1];
    }

    /**
     * Parses URL query parameters into an accessible Map.
     * @param url
     * @return Map<String, List<String>>
     */
    public static Map<String, List<String>> getQueryParams(String url) {
        try {
            Map<String, List<String>> params = new HashMap<>();
            String[] urlParts = url.split("\\?");
            if (urlParts.length > 1) {
                String query = urlParts[1];
                for (String param : query.split("&")) {
                    String[] pair = param.split("=");
                    String key = URLDecoder.decode(pair[0], "UTF-8");
                    String value = "";
                    if (pair.length > 1) {
                        value = URLDecoder.decode(pair[1], "UTF-8");
                    }

                    List<String> values = params.get(key);
                    if (values == null) {
                        values = new ArrayList<String>();
                        params.put(key, values);
                    }
                    values.add(value);
                }
            }

            return params;
        } catch (UnsupportedEncodingException ex) {
            throw new AssertionError(ex);
        }
    }

}
