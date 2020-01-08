package edu.uci.ics.luisae.service.billing.models.BillingClasses;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TransactionModel {
    @JsonProperty(value = "capture_id", required = true)
    private String capture_id;
    @JsonProperty(value = "state", required = true)
    private String state;
    @JsonProperty(value = "amount")
    private AmountModel amount;
    @JsonProperty(value = "transaction_fee")
    private TransactionFeeModel transaction_fee;
    @JsonProperty(value = "create_time", required = true)
    private String create_time;
    @JsonProperty(value = "update_time", required = true)
    private String update_time;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty(value = "items")
    private Items[] items;

    public TransactionModel(){}

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty(value = "items")
    public Items[] getItems() {
        return items;
    }

    public void setItems(Items[] items) {
        this.items = items;
    }

    @JsonProperty(value = "update_time", required = true)
    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    @JsonProperty(value = "create_time", required = true)
    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    @JsonProperty(value = "transaction_fee")
    public TransactionFeeModel getTransaction_fee() {
        return transaction_fee;
    }

    public void setTransaction_fee(TransactionFeeModel transaction_fee) {
        this.transaction_fee = transaction_fee;
    }

    @JsonProperty(value = "amount")
    public AmountModel getAmount() {
        return amount;
    }

    public void setAmount(AmountModel amount) {
        this.amount = amount;
    }

    @JsonProperty(value = "state", required = true)
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @JsonProperty(value = "capture_id", required = true)
    public String getCapture_id() {
        return capture_id;
    }

    public void setCapture_id(String capture_id) {
        this.capture_id = capture_id;
    }
}
