package com.livevox.multiplier.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.livevox.multiplier.domain.lvapi.Campaign;
import lombok.Data;
import org.apache.commons.beanutils.BeanUtils;

@Data
@JsonInclude(Include.NON_NULL)
public class CampaignResponse extends Campaign {

    private static final long serialVersionUID = 72264260784754L;

    private Integer clientId;


    public CampaignResponse() {}


    public CampaignResponse(Campaign campaign) {
        if(campaign == null) {
            return;
        }
        try {
            BeanUtils.copyProperties(this, campaign);
        } catch (Exception e) {
            throw new IllegalStateException("Could not copy CampaignResponse object");
        }
    }
}
