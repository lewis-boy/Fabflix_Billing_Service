package edu.uci.ics.luisae.service.billing.core;

import com.braintreepayments.http.HttpResponse;
import com.braintreepayments.http.exceptions.HttpException;
import com.braintreepayments.http.serializer.Json;
import com.paypal.orders.*;
import edu.uci.ics.luisae.service.billing.database.Intercommunication;
import edu.uci.ics.luisae.service.billing.logger.ServiceLogger;
import edu.uci.ics.luisae.service.billing.models.PlaceResponse;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PaypalLogic {
    public static String createPayPalOrder(PayPalOrderClient orderClient, String total, PlaceResponse placeResponse){
        Order order;
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.checkoutPaymentIntent("CAPTURE");

        ApplicationContext applicationContext = new ApplicationContext().returnUrl("http://localhost:3000/billing/complete")
                .cancelUrl(Intercommunication.getBillingPath() + "/billing/order/dummy");
        orderRequest.applicationContext(applicationContext);
        List<PurchaseUnitRequest> purchaseUnits = new ArrayList<>();
        purchaseUnits.add(new PurchaseUnitRequest().amountWithBreakdown(new
                AmountWithBreakdown().currencyCode("USD").value(total)));
        orderRequest.purchaseUnits(purchaseUnits);

        OrdersCreateRequest request = new OrdersCreateRequest().requestBody(orderRequest);
        try{
            HttpResponse<Order> response = orderClient.client.execute(request);
            order = response.result();
            System.out.println("Order ID: " + order.id());
            order.links().forEach(link -> System.out.println(link.rel() + " => " +
                    link.method() + ":" + link.href()));
            //returnResponse.setApprove_url(order.links().get(2));
            placeResponse.setApprove_url(order.links().get(1).href());
            return order.id();

        }catch(IOException ioe){
            System.err.println("**************COULD NOT CREATE ORDER**************");
            if(ioe instanceof HttpException){
                HttpException he = (HttpException) ioe;
                System.out.println(he.getMessage());
                he.headers().forEach(x -> System.out.println(x + " :" + he.headers().header(x)));
            }
            else{
                ServiceLogger.LOGGER.warning("something went wrong on client side");
            }
        }
        return null;
    }

    public static String captureOrder(String orderID, PayPalOrderClient orderClient){
        Order order;
        OrdersCaptureRequest request = new OrdersCaptureRequest(orderID);
        try{
            HttpResponse<Order> response = orderClient.client.execute(request);
            order = response.result();
            ServiceLogger.LOGGER.info("Capture ID: " +
                    order.purchaseUnits().get(0).payments().captures().get(0).id());
            ServiceLogger.LOGGER.info("PayerID: " +
                    order.payer().payerId());
            order.purchaseUnits().get(0).payments().captures().get(0).links()
                    .forEach(link -> ServiceLogger.LOGGER.info(link.rel() + " => " +
                            link.method() + ":" + link.href()));
            return order.purchaseUnits().get(0).payments().captures().get(0).id();
        }catch(IOException ioe){
            if (ioe instanceof HttpException){
                HttpException he = (HttpException) ioe;
                ServiceLogger.LOGGER.warning(he.getMessage());
                he.headers().forEach(x -> ServiceLogger.LOGGER.warning(
                        x + " :" + he.headers().header(x)));
            }
            else{}
            return null;
        }
    }

    public static JSONObject getOrder(String orderID, PayPalOrderClient orderClient) throws IOException{
        OrdersGetRequest request = new OrdersGetRequest(orderID);
        HttpResponse<Order> response = orderClient.client.execute(request);
//        ServiceLogger.LOGGER.info("Test 3: " + (new Json().serialize(response.result().status())));
//        ServiceLogger.LOGGER.info("Test 4: " + (new Json().serialize(response.result().updateTime())));
        try {
            return new JSONObject(new Json().serialize(response.result()));
        }catch(Exception e){ServiceLogger.LOGGER.warning(e.getMessage()); return null;}
    }







}
