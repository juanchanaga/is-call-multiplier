/*
 * Copyright (c) 2021. Luis Felipe Sosa Alvarez. All rights reserved to LiveVox.
 * Use this subject to license terms.
 *
 * IS-CALL-MULTIPLIER
 */

package com.livevox.multiplier.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.livevox.multiplier.domain.lvapi.CallCenter;
import com.livevox.multiplier.domain.lvapi.CallDetail;
import com.livevox.multiplier.domain.lvapi.CallRecording;
import com.livevox.multiplier.domain.stats.Skill;
import com.livevox.integration.commons.domain.stats.TermCode;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Client implements Serializable, Comparable<Client> {

    public static final long serialVersionUID = 62011575507L;

    private Integer id;

    private String clientName;

    private String clientCode;

    private List<CallCenter> callCenters;

    private List<Skill> services;

    private List<TermCode> termCodes;

    private List<CallDetail> calls;

    private List<CallRecording> recordings;

    public void sortCallCenters() {
        if(getCallCenters() != null) {
            Collections.sort(callCenters);
            for(CallCenter cc: callCenters) {
                cc.sortServices();
            }
        }

    }

    public int compareTo(Client otherClient) {
        if(otherClient == null || (otherClient.getClientName() == null && getClientName() == null)  ||
                (getClientName().equals(otherClient.getClientName()))  ) {
            return 0;
        }

        if(getClientName() != null && otherClient.getClientName()  == null) {
            return 1;
        }

        if(getClientName() == null && otherClient.getClientName()  != null) {
            return -1;
        }

        return getClientName().compareTo(otherClient.getClientName() );
    }

    public boolean hasCallCenter(Long callCenterId) {
        if(callCenterId == null || callCenters == null) {
            return false;
        }
        if(  getCallCenter(callCenterId) != null) {
            return true;
        }
        return false;
    }


    public CallCenter getCallCenter(Long callCenterId) {
        if(callCenterId == null || callCenters == null) {
            return null;
        }
        for(CallCenter cc : callCenters) {
            if(cc.getCallCenterId() != null &&
                    cc.getCallCenterId().equals(callCenterId)) {
                return cc;
            }
        }
        return null;
    }


    public boolean hasService(Integer id) {
        if(id == null || callCenters == null) {
            return false;
        }
        if(  getService(id) != null) {
            return true;
        }
        return false;
    }


    public Skill getService(Integer id) {
        if(id == null || callCenters == null) {
            return null;
        }
        for(CallCenter cc : callCenters) {
            Skill srvc = cc.getService(id);
            if(srvc != null) {
                return srvc;
            }
        }
        return null;
    }

    public List<Skill> findAllServices() {
        if( callCenters == null) {
            return null;
        }
        List<Skill> serviceList = new ArrayList<>();
        for(CallCenter cc : callCenters) {
            serviceList.addAll(cc.getServices());
        }
        return serviceList;
    }


    public String getCallCenterName(Long callCenterId) {
        CallCenter cc = getCallCenter(callCenterId);
        if(cc == null) {
            return null;
        }
        return cc.getName();
    }


    public Long getCallCenterId(String callCenterName) {
        List<CallCenter> ccList = getCallCenters();
        if(ccList == null) {
            return null;
        }
        for(CallCenter cc : ccList ) {
            if(cc.getName().equals(callCenterName)){
                return cc.getCallCenterId();
            }
        }
        return null;
    }


    public String getServiceName(Integer serviceId) {
        Skill srvc = getService(serviceId);
        if(srvc == null) {
            return null;
        }
        return srvc.getName();
    }


    public Integer getServiceId(String serviceName) {
        List<Skill> srvList = getServices();
        if(srvList == null) {
            return null;
        }
        for(Skill srvc : srvList ) {
            if(srvc.getName().equals(serviceName)){
                return srvc.getCallCenterId();
            }
        }
        return null;
    }


    public List<Integer> findAllServiceIds() {
        if(findAllServices() == null) {
            return null;
        }
        List<Integer> idsList = new ArrayList<>();
        for(Skill srvc : findAllServices() ) {
            if(srvc != null && srvc.getCallCenterId() != null){
                idsList.add(srvc.getCallCenterId());
            }
        }
        return idsList;
    }


    public List<Long> findAllCallCenterIds() {
        if(getCallCenters() == null) {
            return null;
        }
        List<Long> idsList = new ArrayList<>();
        for(CallCenter cc : getCallCenters() ) {
            if(cc != null && cc.getCallCenterId() != null){
                idsList.add(cc.getCallCenterId());
            }
        }
        return idsList;
    }

    public void addCallCenters(CallCenter callCenter) {
        if(this.callCenters == null) {
            callCenters = new ArrayList<>();
        }
        callCenters.add(callCenter);
    }

    public void addServices(Skill service) {
        if(this.services == null) {
            services = new ArrayList<>();
        }
        services.add(service);
    }

    public void addTermCodes(TermCode termCode) {
        if(this.termCodes == null) {
            termCodes = new ArrayList<>();
        }
        termCodes.add(termCode);
    }

    public void addCalls(CallDetail call) {
        if(this.calls == null) {
            calls = new ArrayList<>();
        }
        calls.add(call);
    }


    public void addCalls(CallRecording recording) {
        if(this.recordings == null) {
            recordings = new ArrayList<>();
        }
        recordings.add(recording);
    }

}
