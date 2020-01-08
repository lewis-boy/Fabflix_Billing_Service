package edu.uci.ics.luisae.service.billing.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.luisae.service.billing.models.DeleteRequest;

public class InsertUpdateRequest extends DeleteRequest {
    @JsonProperty(value = "quantity", required = true)
    private Integer quantity;

    public InsertUpdateRequest(){}

    @JsonProperty(value = "quantity", required = true)
    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
