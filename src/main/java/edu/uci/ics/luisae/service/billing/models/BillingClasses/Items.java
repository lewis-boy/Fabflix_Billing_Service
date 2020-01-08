package edu.uci.ics.luisae.service.billing.models.BillingClasses;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class Items {
    @JsonProperty(value = "email", required = true)
    private String email;
    @JsonProperty(value = "unit_price", required = true)
    private Float unit_price;
    @JsonProperty(value = "discount", required = true)
    private Float discount;
    @JsonProperty(value = "quantity", required = true)
    private Integer quantity;
    @JsonProperty(value = "movie_id", required = true)
    private String movie_id;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty(value = "movie_title", required = true)
    private String movie_title;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty(value = "sale_date")
    private String sale_date;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty(value = "backdrop_path")
    private String backdrop_path;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty(value = "poster_path")
    private String poster_path;

    public Items(){}
    public Items(String email, Float unit_price, Float discount, Integer quantity, String movie_id){
        this.email = email;
        this.unit_price = unit_price;
        this.discount = discount;
        this.quantity = quantity;
        this.movie_id = movie_id;
    }

    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty(value = "sale_date")
    public String getSale_date() {
        return sale_date;
    }

    public void setSale_date(String sale_date) {
        this.sale_date = sale_date;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty(value = "poster_path")
    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty(value = "backdrop_path")
    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    @JsonProperty(value = "movie_title", required = true)
    public String getMovie_title() {
        return movie_title;
    }

    public void setMovie_title(String movie_title) {
        this.movie_title = movie_title;
    }

    @JsonProperty(value = "movie_id", required = true)
    public String getMovie_id() {
        return movie_id;
    }

    public void setMovie_id(String movie_id) {
        this.movie_id = movie_id;
    }

    @JsonProperty(value = "quantity", required = true)
    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @JsonProperty(value = "discount", required = true)
    public Float getDiscount() {
        return discount;
    }

    public void setDiscount(Float discount) {
        this.discount = discount;
    }

    @JsonProperty(value = "unit_price", required = true)
    public Float getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(Float unit_price) {
        this.unit_price = unit_price;
    }

    @JsonProperty(value = "email", required = true)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
