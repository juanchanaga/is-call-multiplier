package com.livevox.is.metricsservice.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class Skill  implements Serializable, Comparable<Skill> {

    private Integer callCenterId;
    private Integer skillId;
    private String name;
    private List<TermCode> termCodes;
    private String direction;

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        return EqualsBuilder.reflectionEquals(this, obj, false, Skill.class);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(7, 61, this, false, Skill.class);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
    }

    @Override
    public int compareTo(Skill otherSkill) {
        if(otherSkill == null || (otherSkill.getName() == null && getName() == null)  ||
                (getName().equals(otherSkill.getName()))  ) {
            return 0;
        }

        if(getName() != null && otherSkill.getName()  == null) {
            return 1;
        }

        if(getName() == null && otherSkill.getName()  != null) {
            return -1;
        }

        return getName().compareTo(otherSkill.getName());
    }

    /**
     * Add term code.
     *
     * @param termCode the term code
     */
    public void addTermCode(TermCode termCode) {
        if(this.termCodes == null) {
            this.termCodes = new ArrayList<>();
        }
        this.termCodes.add(termCode);
    }


    /**
     * Has term code boolean.
     *
     * @param tcCode the tc code
     * @return the boolean
     */
    public boolean hasTermCode(String tcCode) {
        if(termCodes == null || tcCode == null) {
            return false;
        }
        if(getTermCode(tcCode) != null) {
            return true;
        }
        return false;
    }

    /**
     * Gets term code.
     *
     * @param tcCode the tc code
     * @return the term code
     */
    public TermCode getTermCode(String tcCode) {
        if(termCodes == null || tcCode == null) {
            return null;
        }
        for(TermCode tc : termCodes) {
            if(tc.getName() != null && tc.getName().equals(tcCode)) {
                return tc;
            }
        }
        return null;
    }

    /**
     * Has term code id boolean.
     *
     * @param id the id
     * @return the boolean
     */
    public boolean hasTermCodeId(Long id) {
        if(termCodes == null || id == null) {
            return false;
        }
        if(getTermCodeById(id) != null) {
            return true;
        }
        return false;
    }

    /**
     * Gets term code by id.
     *
     * @param id the id
     * @return the term code by id
     */
    public TermCode getTermCodeById(Long id) {
        if(termCodes == null || id == null) {
            return null;
        }
        for(TermCode tc : termCodes) {
            if(tc.getId() != null && tc.getId().equals(id)) {
                return tc;
            }
        }
        return null;
    }

    /**
     * Has result code id boolean.
     *
     * @param resultCodeId the result code id
     * @return the boolean
     */
    public boolean hasResultCodeId(Integer resultCodeId) {
        if(termCodes == null || resultCodeId == null) {
            return false;
        }
        for(TermCode tc : termCodes) {
            if(tc.getLvResultId() != null && tc.getLvResultId().equals(resultCodeId)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets service id.
     *
     * @return the service id
     */
    public Integer getServiceId() {
        return skillId;
    }

    /**
     * Sets service id.
     *
     * @param skillId the skill id
     */
    public void setServiceId(Integer skillId) {
        this.skillId = skillId;
    }

    /**
     * Get id integer.
     *
     * @return the integer
     */
    public Integer getId(){
        return skillId;
    }

}
