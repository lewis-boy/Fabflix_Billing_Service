package edu.uci.ics.luisae.service.billing.models;

import com.braintreepayments.http.serializer.Json;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.luisae.service.billing.Base.ResponseModel;

public class PlaceResponse extends ResponseModel {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty(value = "approve_url")
    private String approve_url;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty(value = "token")
    private String token;

    public PlaceResponse(){}

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getApprove_url() {
        return approve_url;
    }

    public void setApprove_url(String approve_url) {
        this.approve_url = approve_url;
    }
}
