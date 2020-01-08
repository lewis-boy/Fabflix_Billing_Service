package edu.uci.ics.luisae.service.billing.models;

public class CompleteRequest {
    private String token;
    private String payer_id;

    public CompleteRequest(){}
    public CompleteRequest(String token, String payer_id){
        this.token = token;
        this.payer_id = payer_id;
    }

    public String getPayer_id() {
        return payer_id;
    }

    public void setPayer_id(String payer_id) {
        this.payer_id = payer_id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
