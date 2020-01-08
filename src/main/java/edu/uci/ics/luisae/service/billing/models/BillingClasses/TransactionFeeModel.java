package edu.uci.ics.luisae.service.billing.models.BillingClasses;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TransactionFeeModel {
    @JsonProperty(value = "value",required = true)
    private String value;
    @JsonProperty(value = "currency",required = true)
    private String currency;

    public TransactionFeeModel(){}

    @JsonProperty(value = "currency",required = true)
    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @JsonProperty(value = "value",required = true)
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
