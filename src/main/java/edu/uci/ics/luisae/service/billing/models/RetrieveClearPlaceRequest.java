package edu.uci.ics.luisae.service.billing.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.luisae.service.billing.Base.RequestModel;

public class RetrieveClearPlaceRequest extends RequestModel {
    @JsonProperty(value = "email", required = true)
    private String email;

    public RetrieveClearPlaceRequest(){}

    @JsonProperty(value = "email", required = true)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
