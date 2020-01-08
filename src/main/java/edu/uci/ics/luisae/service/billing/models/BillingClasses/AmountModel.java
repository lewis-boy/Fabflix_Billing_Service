package edu.uci.ics.luisae.service.billing.models.BillingClasses;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AmountModel {
    @JsonProperty(value = "total",required = true)
    private String total;
    @JsonProperty(value = "currency", required = true)
    private String currency;

    public AmountModel(){}

    @JsonProperty(value = "currency", required = true)
    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @JsonProperty(value = "total",required = true)
    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
