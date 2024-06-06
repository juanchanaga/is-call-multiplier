/*
 * Copyright (c) 2021. Luis Felipe Sosa Alvarez. All rights reserved to LiveVox.
 * Use this subject to license terms.
 *
 * IS-CALL-MULTIPLIER
 */

package com.livevox.integration.commons.utils;

import com.livevox.commons.domain.*;
import lombok.extern.slf4j.Slf4j;
import com.livevox.commons.domain.Permission.PermissionType;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ShareUtil implements Serializable {

    private static final long serialVersionUID = 7707907824323009978L;

    private ShareUtil(){}



    public static boolean isHidden(final List<Share> shares, final Integer clientId ) {
        if(shares == null || clientId == null) {
            return false;
        }
        for(Share s : shares) {
            if( isHidden(s, clientId) ) {
                return true;
            }
        }
        return false;
    }


    public static boolean isHidden(final List<Share> shares, final Integer clientId, String accessApp ) {
        if(shares == null || clientId == null) {
            return false;
        }
        for(Share s : shares) {
            if( isHidden(s, clientId, accessApp) ) {
                return true;
            }
        }
        return false;
    }


    public static boolean isHidden(final List<Share> shares, final Integer clientId, String accessApp,
                                   String customPermissionName ) {
        if(shares == null || clientId == null) {
            return false;
        }
        for(Share s : shares) {
            if( isHidden(s, clientId, accessApp, customPermissionName) ) {
                return true;
            }
        }
        return false;
    }


    public static boolean isHidden(final Access acc, final Integer clientId) {
        return isHidden(acc, clientId, null);
    }


    public static boolean isHidden(final Share share, final Integer clientId) {
        return isHidden(share, clientId, null);
    }


    public static boolean isHidden(final Share share,
                                   final Integer clientId, String accessApp) {
        return isHidden(share, clientId, accessApp, null);
    }


    public static boolean isHidden(final Access acc,
                                   final Integer clientId, String accessApp) {
        return isHidden(acc, clientId, accessApp, null);
    }


    public static boolean isHidden(final Share share, final Integer clientId,
                                   final String accessApp, final String customPermissionName) {
        return can(PermissionType.HIDE, share, clientId, accessApp,
                customPermissionName);
    }


    public static boolean isHidden(final Access acc, final Integer clientId,
                                   final String accessApp, final String customPermissionName) {
        return can(PermissionType.HIDE, acc, clientId, accessApp,
                customPermissionName);
    }


    public static boolean canUpdate(final List<Share> shares, final Integer clientId ) {
        if(shares == null || clientId == null) {
            return false;
        }
        for(Share s : shares) {
            if( canUpdate(s, clientId) ) {
                return true;
            }
        }
        return false;
    }


    public static boolean canUpdate(final List<Share> shares, final Integer clientId, String accessApp ) {
        if(shares == null || clientId == null) {
            return false;
        }
        for(Share s : shares) {
            if( canUpdate(s, clientId, accessApp) ) {
                return true;
            }
        }
        return false;
    }


    public static boolean canUpdate(final List<Share> shares, final Integer clientId, String accessApp,
                                    String customPermissionName ) {
        if(shares == null || clientId == null) {
            return false;
        }
        for(Share s : shares) {
            if( canUpdate(s, clientId, accessApp, customPermissionName) ) {
                return true;
            }
        }
        return false;
    }


    public static boolean canUpdate(final Access acc, final Integer clientId) {
        return canUpdate(acc, clientId, null);
    }


    public static boolean canUpdate(final Share share, final Integer clientId) {
        return canUpdate(share, clientId, null);
    }


    public static boolean canUpdate(final Access acc, final Integer clientId, String accessApp) {
        return canUpdate(acc, clientId, accessApp, null);
    }

    public static boolean canUpdate(final Share share, final Integer clientId, String accessApp) {
        return canUpdate(share, clientId, accessApp, null);
    }


    public static boolean canUpdate(final Share share, final Integer clientId, final String accessApp,
                                    final String customPermissionName) {
        return can(PermissionType.UPDATE, share, clientId, accessApp,
                customPermissionName);
    }


    public static boolean canUpdate(final Access acc, final Integer clientId, final String accessApp,
                                    final String customPermissionName) {
        return can(PermissionType.UPDATE, acc, clientId, accessApp,
                customPermissionName);
    }


    public static boolean canUpdate(final Share share, final Integer clientId, final String accessApp,
                                    final String customPermissionName,  final List<Client> clientConfigValues, final Long callCenterId,
                                    final Integer serviceId) {
        return can(PermissionType.UPDATE, share, clientId, accessApp, customPermissionName,
                clientConfigValues, callCenterId, serviceId);
    }


    public static boolean canUpdate(final Access acc, final Integer clientId, final String accessApp,
                                    final String customPermissionName,  final List<Client> clientConfigValues, final Long callCenterId,
                                    final Integer serviceId) {
        return can(PermissionType.UPDATE, acc, clientId, accessApp, customPermissionName,
                clientConfigValues, callCenterId, serviceId);
    }


    public static boolean canDelete(final List<Share> shares, final Integer clientId ) {
        if(shares == null || clientId == null) {
            return false;
        }
        for(Share s : shares) {
            if( canDelete(s, clientId) ) {
                return true;
            }
        }
        return false;
    }


    public static boolean canDelete(final List<Share> shares, final Integer clientId, String accessApp ) {
        if(shares == null || clientId == null) {
            return false;
        }
        for(Share s : shares) {
            if( canDelete(s, clientId, accessApp) ) {
                return true;
            }
        }
        return false;
    }


    public static boolean canDelete(final List<Share> shares, final Integer clientId, String accessApp,
                                    String customPermissionName ) {
        if(shares == null || clientId == null) {
            return false;
        }
        for(Share s : shares) {
            if( canDelete(s, clientId, accessApp, customPermissionName) ) {
                return true;
            }
        }
        return false;
    }


    public static boolean canDelete(final List<Share> shares, final Integer clientId, String accessApp,
                                    String customPermissionName,  final List<Client> clientConfigValues, final Long callCenterId,
                                    final Integer serviceId ) {
        if(shares == null || clientId == null) {
            return false;
        }
        for(Share s : shares) {
            if( canDelete(s, clientId, accessApp, customPermissionName, clientConfigValues, callCenterId, serviceId) ) {
                return true;
            }
        }
        return false;
    }


    public static boolean canDelete(final Access acc, final Integer clientId) {
        return canDelete(acc, clientId, null);
    }


    public static boolean canDelete(final Share share, final Integer clientId) {
        return canDelete(share, clientId, null);
    }


    public static boolean canDelete(final Share share, final Integer clientId, String accessApp) {
        return canDelete(share, clientId, accessApp, null);
    }


    public static boolean canDelete(final Access acc, final Integer clientId, String accessApp) {
        return canDelete(acc, clientId, accessApp, null);
    }


    public static boolean canDelete(final Access acc, final Integer clientId, final String accessApp,
                                    final String customPermissionName) {
        return can(PermissionType.DELETE, acc, clientId, accessApp,
                customPermissionName);
    }


    public static boolean canDelete(final Share share, final Integer clientId, final String accessApp,
                                    final String customPermissionName) {
        return can(PermissionType.DELETE, share, clientId, accessApp,
                customPermissionName);
    }


    public static boolean canDelete(final Access acc, final Integer clientId, final String accessApp,
                                    final String customPermissionName,  final List<Client> clientConfigValues, final Long callCenterId,
                                    final Integer serviceId) {
        return can(PermissionType.DELETE, acc, clientId, accessApp,
                customPermissionName, clientConfigValues, callCenterId, serviceId);
    }


    public static boolean canDelete(final Share share, final Integer clientId, final String accessApp,
                                    final String customPermissionName,  final List<Client> clientConfigValues, final Long callCenterId,
                                    final Integer serviceId) {
        return can(PermissionType.DELETE, share, clientId, accessApp,
                customPermissionName, clientConfigValues, callCenterId, serviceId);
    }


    public static boolean canAdd(final List<Share> shares, final Integer clientId ) {
        if(shares == null || clientId == null) {
            return false;
        }
        for(Share s : shares) {
            if( canAdd(s, clientId) ) {
                return true;
            }
        }
        return false;
    }


    public static boolean canAdd(final List<Share> shares, final Integer clientId, String accessApp ) {
        if(shares == null || clientId == null) {
            return false;
        }
        for(Share s : shares) {
            if( canAdd(s, clientId, accessApp) ) {
                return true;
            }
        }
        return false;
    }


    public static boolean canAdd(final List<Share> shares, final Integer clientId, String accessApp,
                                 String customPermissionName ) {
        if(shares == null || clientId == null) {
            return false;
        }
        for(Share s : shares) {
            if( canAdd(s, clientId, accessApp, customPermissionName) ) {
                return true;
            }
        }
        return false;
    }


    public static boolean canAdd(final List<Share> shares, final Integer clientId, String accessApp,
                                 String customPermissionName,  final List<Client> clientConfigValues, final Long callCenterId,
                                 final Integer serviceId ) {
        if(shares == null || clientId == null) {
            return false;
        }
        for(Share s : shares) {
            if( canAdd(s, clientId, accessApp, customPermissionName, clientConfigValues, callCenterId, serviceId) ) {
                return true;
            }
        }
        return false;
    }


    public static boolean canAdd(final Access acc, final Integer clientId) {
        return canAdd(acc, clientId, null);
    }


    public static boolean canAdd(final Share share, final Integer clientId) {
        return canAdd(share, clientId, null);
    }


    public static boolean canAdd(final Access acc,
                                 final Integer clientId, String accessApp) {
        return canAdd(acc, clientId, accessApp, null);
    }


    public static boolean canAdd(final Share share,
                                 final Integer clientId, String accessApp) {
        return canAdd(share, clientId, accessApp, null);
    }


    public static boolean canAdd(final Access acc, final Integer clientId, final String accessApp,
                                 final String customPermissionName) {
        return can(PermissionType.ADD, acc, clientId, accessApp,
                customPermissionName);
    }


    public static boolean canAdd(final Share share, final Integer clientId, final String accessApp,
                                 final String customPermissionName) {
        return can(PermissionType.ADD, share, clientId, accessApp,
                customPermissionName);
    }


    public static boolean canAdd(final Access acc, final Integer clientId, final String accessApp,
                                 final String customPermissionName,  final List<Client> clientConfigValues,
                                 final Long callCenterId, final Integer serviceId) {
        return can(PermissionType.ADD, acc, clientId, accessApp,
                customPermissionName, clientConfigValues, callCenterId, serviceId);
    }


    public static boolean canAdd(final Share share, final Integer clientId, final String accessApp,
                                 final String customPermissionName,  final List<Client> clientConfigValues,
                                 final Long callCenterId, final Integer serviceId) {
        return can(PermissionType.ADD, share, clientId, accessApp,
                customPermissionName, clientConfigValues, callCenterId, serviceId);
    }


    public static boolean canRead(final List<Share> shares, final Integer clientId ) {
        if(shares == null || clientId == null) {
            return false;
        }
        for(Share s : shares) {
            if( canRead(s, clientId) ) {
                return true;
            }
        }
        return false;
    }


    public static boolean canRead(final List<Share> shares, final Integer clientId, String accessApp ) {
        if(shares == null || clientId == null) {
            return false;
        }
        for(Share s : shares) {
            if( canRead(s, clientId, accessApp) ) {
                return true;
            }
        }
        return false;
    }


    public static boolean canRead(final List<Share> shares, final Integer clientId, String accessApp,
                                  String customPermissionName ) {
        if(shares == null || clientId == null) {
            return false;
        }
        for(Share s : shares) {
            if( canRead(s, clientId, accessApp, customPermissionName) ) {
                return true;
            }
        }
        return false;
    }


    public static boolean canRead(final List<Share> shares, final Integer clientId, String accessApp,
                                  String customPermissionName,  final List<Client> clientConfigValues,
                                  final Long callCenterId, final Integer serviceId ) {
        if(shares == null || clientId == null) {
            return false;
        }
        for(Share s : shares) {
            if( canRead(s, clientId, accessApp, customPermissionName, clientConfigValues,
                    callCenterId, serviceId) ) {
                return true;
            }
        }
        return false;
    }


    public static boolean canRead(final Access acc, final Integer clientId) {
        return canRead(acc, clientId, null);
    }


    public static boolean canRead(final Share share, final Integer clientId) {
        return canRead(share, clientId, null);
    }


    public static boolean canRead(final Access acc,
                                  final Integer clientId, String accessApp) {
        return canRead(acc, clientId, accessApp, null);
    }

    public static boolean canRead(final Share share,
                                  final Integer clientId, String accessApp) {
        return canRead(share, clientId, accessApp, null);
    }


    public static boolean canRead(final Share share, final Integer clientId, final String accessApp,
                                  final String customPermissionName) {
        return can(PermissionType.READ, share, clientId, accessApp,
                customPermissionName);
    }


    public static boolean canRead(final Access acc, final Integer clientId, final String accessApp,
                                  final String customPermissionName) {
        return can(PermissionType.READ, acc, clientId, accessApp,
                customPermissionName);
    }


    public static boolean canRead(final Access acc, final Integer clientId, final String accessApp,
                                  final String customPermissionName, final List<Client> clientConfigValues,
                                  final Long callCenterId, final Integer serviceId) {
        return can(PermissionType.READ, acc, clientId, accessApp,
                customPermissionName, clientConfigValues, callCenterId, serviceId);
    }


    public static boolean canRead(final Share share, final Integer clientId, final String accessApp,
                                  final String customPermissionName, final List<Client> clientConfigValues,
                                  final Long callCenterId, final Integer serviceId) {
        return can(PermissionType.READ, share, clientId, accessApp,
                customPermissionName, clientConfigValues, callCenterId, serviceId);
    }


    public static boolean can(final PermissionType type, final Share share,
                              final Integer clientId, final String accessApp, final String customPermissionName) {
        return can(type, share, clientId, accessApp, customPermissionName, null, null, null);
    }


    public static boolean can(final PermissionType type, final Access acc,
                              final Integer clientId, final String accessApp, final String customPermissionName) {
        return can(type, acc, clientId, accessApp, customPermissionName, null, null, null);
    }


    public static boolean can(final PermissionType type, final Share share,
                              final Integer clientId, final String accessApp, final String customPermissionName,
                              final List<Client> clientConfigValues, final Long callCenterId, final Integer serviceId) {
        if(clientId == null || share == null || type == null || share.getAccess() == null) {
            return false;
        }
        for(Access a : share.getAccess()) {
            if(can(type, a, clientId, accessApp, customPermissionName, clientConfigValues, callCenterId, serviceId) ) {
                return true;
            }
        }
        return false;
    }


    public static boolean can(final PermissionType type, final Access acc,
                              final Integer clientId, final String accessApp, final String customPermissionName,
                              final List<Client> clientConfigValues, final Long callCenterId, final Integer serviceId) {
        if(clientId == null || type == null || acc == null) {
            return false;
        }
        if(acc.getSharedClientId() != null && acc.getPermissions() != null &&
                acc.getSharedClientId().equals(clientId) ) {
            if(acc.getApplications() == null  || StringUtils.isBlank(accessApp) ||
                    ( accessApp != null && acc.hasApplication(accessApp) ) ) {
                for(Permission p : acc.getPermissions()) {
                    if( (p.getType() != null && p.getType().equals(type)) &&
                            ( (p.getName() == null || customPermissionName == null) ||
                                    p.getName().equals(customPermissionName)) ) {
                        if( hasConfigAccess(clientConfigValues, callCenterId, serviceId)  ) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }


    public static boolean hasConfigAccess(final List<Client> clientConfigValues, final Long callCenterId,
                                          final Integer serviceId) {
        if(clientConfigValues == null || (callCenterId == null && serviceId == null)  ) {
            return true;
        }
        for(Client clnt : clientConfigValues) {
            if(  callCenterId != null && serviceId == null && clnt.hasCallCenter(callCenterId) ) {
                return true;
            } else if(  serviceId != null && clnt.hasService(serviceId)) {
                return true;
            }
        }
        return false;
    }


    public static List<Integer> getClientIdsAuthroizedFor(List<Share> shares, PermissionType permission)  {
        return getClientIdsAuthroizedFor(shares,permission,null);
    }

    public static List<Integer> getClientIdsAuthroizedFor(List<Share> shares, PermissionType permission, String app) {
        if (shares == null || permission == null) {
            return null;
        }
        List<Integer> matchingIds = new ArrayList<>();
        for (Share s : shares) {
            if (StringUtils.isBlank(app)) {
                matchingIds.addAll(s.getClientIdsForPermissionType(permission));
            } else {
                matchingIds.addAll(s.getClientIdsForPermissionTypeAndApplication(permission, app));
            }
        }
        return matchingIds;
    }

}