package com.livevox.is.metricsservice.controller.rest.conf;


import com.livevox.is.metricsservice.domain.TermCode;

import java.io.Serializable;
import java.util.List;

public interface LvTermCodeRestService extends Serializable {
    public List<TermCode> getTermCodeList(Long skillId, Integer count, Integer offset, Integer clientId);

    public List<TermCode> getTermCodeList(Long skillId, Integer count, Integer offset, Integer clientId, String apiSessionId);

    public List<TermCode> getTermCodeList(Long skillId, Integer count, Integer offset, Integer clientId, String apiSessionId, String apiVersion);

    public List<TermCode> getAllTermCodes(Long skillId, Integer clientId, String apiSessionId);

    public List<TermCode> getAllTermCodes(Long skillId, Integer clientId, String apiSessionId, String apiVersion);

}
