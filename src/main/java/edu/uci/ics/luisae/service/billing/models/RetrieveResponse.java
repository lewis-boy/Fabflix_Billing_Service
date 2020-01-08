package edu.uci.ics.luisae.service.billing.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.luisae.service.billing.Base.ResponseModel;
import edu.uci.ics.luisae.service.billing.models.BillingClasses.Items;

public class RetrieveResponse extends ResponseModel {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty(value = "items")
    Items[] items;

    public RetrieveResponse(){}

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty(value = "items")
    public Items[] getItems() {
        return items;
    }

    public void setItems(Items[] items) {
        this.items = items;
    }
}
