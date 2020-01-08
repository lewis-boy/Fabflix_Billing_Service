package edu.uci.ics.luisae.service.billing.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DeleteRequest extends RetrieveClearPlaceRequest{
    @JsonProperty(value = "movie_id", required = true)
    private String movie_id;

    public DeleteRequest(){}

    @JsonProperty(value = "movie_id", required = true)
    public String getMovie_id() {
        return movie_id;
    }

    public void setMovie_id(String movie_id) {
        this.movie_id = movie_id;
    }
}
