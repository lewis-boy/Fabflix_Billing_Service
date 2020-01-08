package edu.uci.ics.luisae.service.billing.core;

import edu.uci.ics.luisae.service.billing.logger.ServiceLogger;
import edu.uci.ics.luisae.service.billing.models.BillingClasses.AmountModel;
import edu.uci.ics.luisae.service.billing.models.BillingClasses.TransactionFeeModel;
import edu.uci.ics.luisae.service.billing.models.BillingClasses.TransactionModel;
import org.json.JSONArray;
import org.json.JSONObject;

public class JsonParser {

    public static void getAmount(JSONObject obj, TransactionModel t){
        AmountModel am = new AmountModel();
        try {
            JSONArray test = (JSONArray) obj.get("purchase_units");
            JSONObject payments = (JSONObject) test.get(0);
            JSONObject captures = (JSONObject) payments.get("payments");
            JSONArray amount = (JSONArray) captures.get("captures");
            JSONObject firstAmount = (JSONObject) amount.get(0);
            JSONObject amountMap =  (JSONObject) firstAmount.get("amount");
            am.setCurrency(amountMap.getString("currency_code"));
            am.setTotal(amountMap.getString("value"));
            t.setAmount(am);
        }catch(Exception e){
            ServiceLogger.LOGGER.warning(e.getMessage());
        }
    }

    public static void getTransactionFee(JSONObject obj, TransactionModel t){
        TransactionFeeModel tfm = new TransactionFeeModel();
        try {
            JSONArray test = (JSONArray) obj.get("purchase_units");
            JSONObject payments = (JSONObject) test.get(0);
            JSONObject captures = (JSONObject) payments.get("payments");
            JSONArray amount = (JSONArray) captures.get("captures");
            JSONObject firstAmount = (JSONObject) amount.get(0);
            JSONObject breakdown =  (JSONObject) firstAmount.get("seller_receivable_breakdown");
            JSONObject pf = (JSONObject) breakdown.get("paypal_fee");
            tfm.setCurrency(pf.getString("currency_code"));
            tfm.setValue(pf.getString("value"));
            t.setTransaction_fee(tfm);

        }catch(Exception e){
            ServiceLogger.LOGGER.warning(e.getMessage());
        }
    }

    public static void getCreateTime(JSONObject obj, TransactionModel t){
        try {
            JSONArray test = (JSONArray) obj.get("purchase_units");
            JSONObject payments = (JSONObject) test.get(0);
            JSONObject captures = (JSONObject) payments.get("payments");
            JSONArray amount = (JSONArray) captures.get("captures");
            JSONObject firstAmount = (JSONObject) amount.get(0);
            t.setCreate_time((String)firstAmount.get("create_time"));
        }catch(Exception e){
            ServiceLogger.LOGGER.warning(e.getMessage());
        }
    }

    public static void getCaptureId(JSONObject obj, TransactionModel t){
        try {
            JSONArray test = (JSONArray) obj.get("purchase_units");
            JSONObject payments = (JSONObject) test.get(0);
            JSONObject captures = (JSONObject) payments.get("payments");
            JSONArray amount = (JSONArray) captures.get("captures");
            JSONObject firstAmount = (JSONObject) amount.get(0);
            t.setCapture_id ((String) firstAmount.get("id"));
        }catch(Exception e){
            ServiceLogger.LOGGER.warning(e.getMessage());
        }
    }

    public static void getState(JSONObject obj, TransactionModel t){
        try {
            t.setState(obj.getString("status"));
        }catch(Exception e){
            ServiceLogger.LOGGER.warning(e.getMessage());
        }
    }


    public static void getUpdateTime(JSONObject obj, TransactionModel t){
        try {
            t.setUpdate_time(obj.getString("update_time"));
        }catch(Exception e){
            ServiceLogger.LOGGER.warning(e.getMessage());
        }
    }






}
