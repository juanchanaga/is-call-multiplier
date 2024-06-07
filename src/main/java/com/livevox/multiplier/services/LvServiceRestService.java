/*
 * Copyright (c) 2021. Luis Felipe Sosa Alvarez. All rights reserved to LiveVox.
 * Use this subject to license terms.
 *
 * IS-CALL-MULTIPLIER
 */

package com.livevox.multiplier.services;

import com.livevox.multiplier.domain.SkillDetails;
import com.livevox.multiplier.domain.SkillPhoneSettings;
import com.livevox.multiplier.domain.SkillRelatedDetails;
import com.livevox.multiplier.domain.SkillSettings;
import com.livevox.multiplier.domain.stats.Skill;

import jakarta.xml.ws.WebServiceException;
import java.io.Serializable;
import java.util.List;

public interface LvServiceRestService extends Serializable {

    public SkillRelatedDetails getSkillRelatedDetails(Integer skillId, Integer clientId)
            throws WebServiceException;

    public List<Skill> getAllSkills(Integer callCenterId, Integer clientId)
            throws WebServiceException;

    public List<Skill> getAllSkills(Integer callCenterId, Integer clientId, String apiSessionId)
            throws WebServiceException;

    public List<Skill> getAllSkills(Integer callCenterId, Integer clientId,
                                    String apiSessionId, String apiVersion) throws WebServiceException;

    public List<Skill> getSkills(Integer callCenterId, Integer clientId, Integer count, Integer offset)
            throws WebServiceException;

    public List<Skill> getSkills(Integer callCenterId, Integer clientId, Integer count,
                                 Integer offset, boolean returnAll) throws WebServiceException ;

    public List<Skill> getSkills(Integer callCenterId, Integer clientId, String apiSessionId, Integer count,
                                 Integer offset, boolean returnAll) throws WebServiceException;

    public List<Skill> getSkills(Integer callCenterId, Integer clientId, String apiSessionId, Integer count,
                                 Integer offset, boolean returnAll, String apiVersion) throws WebServiceException;

    public SkillRelatedDetails getSkillRelatedDetails(Integer skillId, Integer clientId, String apiSessionId)
            throws WebServiceException;

    public SkillRelatedDetails getSkillRelatedDetails(Integer skillId, Integer clientId,
                                                      String apiSessionId, String apiVersion) throws WebServiceException;

    public SkillDetails getSkillGeneralProperties(Integer skillId, Integer clientId) throws WebServiceException;

    public SkillDetails getSkillGeneralProperties(Integer skillId, Integer clientId, String apiSessionId)
            throws WebServiceException;

    public SkillDetails getSkillGeneralProperties(Integer skillId, Integer clientId,
                                                  String apiSessionId, String apiVersion) throws WebServiceException;

    public SkillPhoneSettings getSkillPhoneSettings(Integer skillId, Integer clientId) throws WebServiceException;

    public SkillPhoneSettings getSkillPhoneSettings(Integer skillId, Integer clientId,
                                                    String apiSessionId) throws WebServiceException;

    public SkillPhoneSettings getSkillPhoneSettings(Integer skillId, Integer clientId,
                                                    String apiSessionId, String apiVersion) throws WebServiceException;

    public SkillSettings getSkillSettings(Integer skillId, Integer clientId) throws WebServiceException;

    public SkillSettings getSkillSettings(Integer skillId, Integer clientId,
                                          String apiSessionId) throws WebServiceException;

    public SkillSettings getSkillSettings(Integer skillId, Integer clientId,
                                          String apiSessionId, String apiVersion) throws WebServiceException;
}
