package edu.uci.ics.luisae.service.billing.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.paypal.orders.Order;
import edu.uci.ics.luisae.service.billing.Base.ResponseModel;
import edu.uci.ics.luisae.service.billing.models.BillingClasses.TransactionModel;

public class OrderRetrieveResponse extends ResponseModel {
    @JsonProperty(value = "transactions")
    private TransactionModel[] transactionModel;

    public OrderRetrieveResponse(){}

    @JsonProperty(value = "transactions")
    public TransactionModel[] getTransactionModel() {
        return transactionModel;
    }

    public void setTransactionModel(TransactionModel[] transactionModel) {
        this.transactionModel = transactionModel;
    }
}
