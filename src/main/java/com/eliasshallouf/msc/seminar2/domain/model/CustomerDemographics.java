package com.eliasshallouf.msc.seminar2.domain.model;

import jakarta.persistence.Column;

import java.io.Serializable;

public class CustomerDemographics implements Serializable {
    @Column Long customerTypeId;
    @Column String customerDesc;

    public Long getCustomerTypeId() {
        return customerTypeId;
    }

    public void setCustomerTypeId(Long customerTypeId) {
        this.customerTypeId = customerTypeId;
    }

    public String getCustomerDesc() {
        return customerDesc;
    }

    public void setCustomerDesc(String customerDesc) {
        this.customerDesc = customerDesc;
    }
}