package com.livevox.is.metricsservice.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.livevox.integration.commons.domain.stats.DefaultDao;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TermCode extends DefaultDao implements Serializable {
    private Integer termCategoryId;
    private String termCategoryName;
    private String resultName;
    private Integer lvResultId;
    private Integer serviceId;
    private String name;
    private String action;
    private Integer reportOrder;
    private Boolean previewDialEnabled;
    private Integer clientId;

    public TermCode() {
    }

    public Integer getTermCategoryId() {
        return this.termCategoryId;
    }

    public String getTermCategoryName() {
        return this.termCategoryName;
    }

    public String getResultName() {
        return this.resultName;
    }

    public Integer getLvResultId() {
        return this.lvResultId;
    }

    public Integer getServiceId() {
        return this.serviceId;
    }

    public String getName() {
        return this.name;
    }

    public String getAction() {
        return this.action;
    }

    public Integer getReportOrder() {
        return this.reportOrder;
    }

    public Boolean getPreviewDialEnabled() {
        return this.previewDialEnabled;
    }

    public Integer getClientId() {
        return this.clientId;
    }

    public void setTermCategoryId(final Integer termCategoryId) {
        this.termCategoryId = termCategoryId;
    }

    public void setTermCategoryName(final String termCategoryName) {
        this.termCategoryName = termCategoryName;
    }

    public void setResultName(final String resultName) {
        this.resultName = resultName;
    }

    public void setLvResultId(final Integer lvResultId) {
        this.lvResultId = lvResultId;
    }

    public void setServiceId(final Integer serviceId) {
        this.serviceId = serviceId;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setAction(final String action) {
        this.action = action;
    }

    public void setReportOrder(final Integer reportOrder) {
        this.reportOrder = reportOrder;
    }

    public void setPreviewDialEnabled(final Boolean previewDialEnabled) {
        this.previewDialEnabled = previewDialEnabled;
    }

    public void setClientId(final Integer clientId) {
        this.clientId = clientId;
    }

    public String toString() {
        return "TermCode(termCategoryId=" + this.getTermCategoryId() + ", termCategoryName=" + this.getTermCategoryName() + ", resultName=" + this.getResultName() + ", lvResultId=" + this.getLvResultId() + ", serviceId=" + this.getServiceId() + ", name=" + this.getName() + ", action=" + this.getAction() + ", reportOrder=" + this.getReportOrder() + ", previewDialEnabled=" + this.getPreviewDialEnabled() + ", clientId=" + this.getClientId() + ")";
    }

    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof TermCode)) {
            return false;
        } else {
            TermCode other = (TermCode)o;
            if (!other.canEqual(this)) {
                return false;
            } else if (!super.equals(o)) {
                return false;
            } else {
                Object this$termCategoryId = this.getTermCategoryId();
                Object other$termCategoryId = other.getTermCategoryId();
                if (this$termCategoryId == null) {
                    if (other$termCategoryId != null) {
                        return false;
                    }
                } else if (!this$termCategoryId.equals(other$termCategoryId)) {
                    return false;
                }

                Object this$lvResultId = this.getLvResultId();
                Object other$lvResultId = other.getLvResultId();
                if (this$lvResultId == null) {
                    if (other$lvResultId != null) {
                        return false;
                    }
                } else if (!this$lvResultId.equals(other$lvResultId)) {
                    return false;
                }

                label119: {
                    Object this$serviceId = this.getServiceId();
                    Object other$serviceId = other.getServiceId();
                    if (this$serviceId == null) {
                        if (other$serviceId == null) {
                            break label119;
                        }
                    } else if (this$serviceId.equals(other$serviceId)) {
                        break label119;
                    }

                    return false;
                }

                label112: {
                    Object this$reportOrder = this.getReportOrder();
                    Object other$reportOrder = other.getReportOrder();
                    if (this$reportOrder == null) {
                        if (other$reportOrder == null) {
                            break label112;
                        }
                    } else if (this$reportOrder.equals(other$reportOrder)) {
                        break label112;
                    }

                    return false;
                }

                Object this$previewDialEnabled = this.getPreviewDialEnabled();
                Object other$previewDialEnabled = other.getPreviewDialEnabled();
                if (this$previewDialEnabled == null) {
                    if (other$previewDialEnabled != null) {
                        return false;
                    }
                } else if (!this$previewDialEnabled.equals(other$previewDialEnabled)) {
                    return false;
                }

                Object this$clientId = this.getClientId();
                Object other$clientId = other.getClientId();
                if (this$clientId == null) {
                    if (other$clientId != null) {
                        return false;
                    }
                } else if (!this$clientId.equals(other$clientId)) {
                    return false;
                }

                label91: {
                    Object this$termCategoryName = this.getTermCategoryName();
                    Object other$termCategoryName = other.getTermCategoryName();
                    if (this$termCategoryName == null) {
                        if (other$termCategoryName == null) {
                            break label91;
                        }
                    } else if (this$termCategoryName.equals(other$termCategoryName)) {
                        break label91;
                    }

                    return false;
                }

                Object this$resultName = this.getResultName();
                Object other$resultName = other.getResultName();
                if (this$resultName == null) {
                    if (other$resultName != null) {
                        return false;
                    }
                } else if (!this$resultName.equals(other$resultName)) {
                    return false;
                }

                Object this$name = this.getName();
                Object other$name = other.getName();
                if (this$name == null) {
                    if (other$name != null) {
                        return false;
                    }
                } else if (!this$name.equals(other$name)) {
                    return false;
                }

                Object this$action = this.getAction();
                Object other$action = other.getAction();
                if (this$action == null) {
                    if (other$action != null) {
                        return false;
                    }
                } else if (!this$action.equals(other$action)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(final Object other) {
        return other instanceof TermCode;
    }

    public int hashCode() {
        boolean PRIME = true;
        int result = super.hashCode();
        Object $termCategoryId = this.getTermCategoryId();
        result = result * 59 + ($termCategoryId == null ? 43 : $termCategoryId.hashCode());
        Object $lvResultId = this.getLvResultId();
        result = result * 59 + ($lvResultId == null ? 43 : $lvResultId.hashCode());
        Object $serviceId = this.getServiceId();
        result = result * 59 + ($serviceId == null ? 43 : $serviceId.hashCode());
        Object $reportOrder = this.getReportOrder();
        result = result * 59 + ($reportOrder == null ? 43 : $reportOrder.hashCode());
        Object $previewDialEnabled = this.getPreviewDialEnabled();
        result = result * 59 + ($previewDialEnabled == null ? 43 : $previewDialEnabled.hashCode());
        Object $clientId = this.getClientId();
        result = result * 59 + ($clientId == null ? 43 : $clientId.hashCode());
        Object $termCategoryName = this.getTermCategoryName();
        result = result * 59 + ($termCategoryName == null ? 43 : $termCategoryName.hashCode());
        Object $resultName = this.getResultName();
        result = result * 59 + ($resultName == null ? 43 : $resultName.hashCode());
        Object $name = this.getName();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        Object $action = this.getAction();
        result = result * 59 + ($action == null ? 43 : $action.hashCode());
        return result;
    }
}
