package edu.uci.ics.luisae.service.billing.core;

import edu.uci.ics.luisae.service.billing.logger.ServiceLogger;
import edu.uci.ics.luisae.service.billing.models.BillingClasses.Items;

public class SaleArithmetic {

    public static String calculateSum(Items[]items){
        float total = 0;
        for(Items item: items) {
            ServiceLogger.LOGGER.info("unit_price: " + item.getUnit_price() + "\ndiscount: " + item.getDiscount()
            + "\nquantity: " + item.getQuantity());
            total += ((item.getUnit_price() * (1 - item.getDiscount())) * item.getQuantity());
        }
        return String.valueOf(Math.round(total*100) / 100.0);
    }

}
