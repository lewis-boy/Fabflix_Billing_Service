package edu.uci.ics.luisae.service.billing.core;

import edu.uci.ics.luisae.service.billing.Base.Headers;
import edu.uci.ics.luisae.service.billing.Base.Result;
import edu.uci.ics.luisae.service.billing.database.QueryHandler;
import edu.uci.ics.luisae.service.billing.logger.ServiceLogger;
import edu.uci.ics.luisae.service.billing.models.*;
import edu.uci.ics.luisae.service.billing.models.BillingClasses.TransactionModel;
import edu.uci.ics.luisae.service.billing.utilities.Param;
import org.json.JSONObject;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.ArrayList;

public class LogicHandler {

    public static Response InsertHandler(InsertUpdateRequest request, NormalResponse response, Headers heads){
        if(request.getQuantity() <= 0){
            response.setResult(Result.INVALID_QUANTITY);
            return response.buildResponseWithHeaders(heads);
        }
        String dupQuery = QueryBuilder.duplicateQuery(request.getEmail(),request.getMovie_id());
        if(QueryHandler.hasDuplicate(dupQuery)){
            response.setResult(Result.DUPLICATE_INSERTION);
            return response.buildResponseWithHeaders(heads);
        }
        if(!QueryHandler.movieExists(request.getMovie_id(), request.getEmail())){
            response.setResult(Result.OPERATION_FAILED);
            return response.buildResponseWithHeaders(heads);
        }
        String insertQuery = QueryBuilder.insertQuery(request);
        Param[] params =  ParamBuilder.insert(request);
        return QueryHandler.insert(insertQuery,params,response,heads);
    }

    public static Response UpdateHandler(InsertUpdateRequest request, NormalResponse response, Headers heads){
        if(request.getQuantity() <= 0){
            response.setResult(Result.INVALID_QUANTITY);
            return response.buildResponseWithHeaders(heads);
        }
        String updateQuery = QueryBuilder.updateQuery(request);
        Param[] params = ParamBuilder.update(request);
        return QueryHandler.update(updateQuery,params,response,heads);
    }

    public static Response DeleteHandler(DeleteRequest request, NormalResponse response, Headers heads){
        String deleteQuery = QueryBuilder.deleteQuery(request);
        Param[] params = ParamBuilder.delete(request);
        return QueryHandler.delete(deleteQuery,params,response,heads);
    }

    public static Response RetrieveHandler(RetrieveClearPlaceRequest request, RetrieveResponse response, Headers heads){
        String retrieveQuery = QueryBuilder.retrieveQuery(request);
        Param[] params = ParamBuilder.retrieve(request);
        return QueryHandler.retrieve(retrieveQuery,params,response, heads);
    }

    public static Response ClearHandler(RetrieveClearPlaceRequest request, NormalResponse response, Headers heads){
        String clearQuery = QueryBuilder.clearQuery(request);
        Param[] params = ParamBuilder.clear(request);
        return QueryHandler.clear(clearQuery,params,response, heads);
    }

    public static Response PlaceHandler(RetrieveClearPlaceRequest request, PlaceResponse response, Headers heads, RetrieveResponse retrieveResponse){
        String orderId;
        String total = SaleArithmetic.calculateSum(retrieveResponse.getItems());
        PayPalOrderClient orderClient = new PayPalOrderClient();
        orderId = PaypalLogic.createPayPalOrder(orderClient,total,response);
        if(orderId == null){
            response.setResult(Result.OPERATION_FAILED);
            return response.buildResponseWithHeaders(heads);
        }
        response.setToken(orderId);
        response.setResult(Result.ORDER_SUCCESSFUL);

        QueryHandler.place(response,retrieveResponse,orderId,heads);
        return response.buildResponseWithHeaders(heads);
    }

    public static Response CompleteHandler(CompleteRequest request, NormalResponse response){
        String captureId;
        PayPalOrderClient orderClient = new PayPalOrderClient();
        captureId = PaypalLogic.captureOrder(request.getToken(),orderClient);
        if(captureId == null){
            response.setResult(Result.ORDER_CAN_NOT_COMPLETE);
            return response.buildResponse();
        }
        response.setResult(Result.ORDER_COMPLETED);
        QueryHandler.complete(response,request.getToken(),captureId);
        QueryHandler.deleteCart(response,request.getToken());
        ServiceLogger.LOGGER.info("Exiting Complete Endpoint Normally");
        return response.buildResponse();
    }

    public static Response OrderRetrieveHandler(RetrieveClearPlaceRequest request, OrderRetrieveResponse response, Headers heads){
        TransactionModel[] transactionModels;
        ArrayList<TransactionModel> transactionModelsAL = new ArrayList<>();
        PayPalOrderClient orderClient = new PayPalOrderClient();
        String[] orderIds = QueryHandler.getOrderIds(QueryBuilder.getTokensforEmail(request.getEmail()));
        if(orderIds == null){
            response.setResult(Result.ORDER_HISTORY_DOES_NOT_EXIST);
            return response.buildResponseWithHeaders(heads);
        }
        if(orderIds.length == 0) {
            response.setResult(Result.ORDER_HISTORY_DOES_NOT_EXIST);
            return response.buildResponseWithHeaders(heads);
        }
//        for(String order : orderIds)
//            ServiceLogger.LOGGER.info(order);
        try {
            //for now keep it in the while loop, because info from prev iteration may linger, you should make a clear function
            //for TransactionModel
            for (String token : orderIds){
                TransactionModel t = new TransactionModel();
                JSONObject fullHistory = PaypalLogic.getOrder(token, orderClient);
                JsonParser.getCaptureId(fullHistory, t);
                JsonParser.getState(fullHistory, t);
                JsonParser.getAmount(fullHistory, t);
                JsonParser.getTransactionFee(fullHistory, t);
                JsonParser.getCreateTime(fullHistory, t);
                JsonParser.getUpdateTime(fullHistory, t);
                t.setItems(QueryHandler.getOrderItems(QueryBuilder.getOrderItems(token)));
                transactionModelsAL.add(t);

            }
        }catch(IOException e){
            ServiceLogger.LOGGER.warning("IO Exception from OrderRetrieve Handler: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        transactionModels = new TransactionModel[transactionModelsAL.size()];
        transactionModelsAL.toArray(transactionModels);
        response.setTransactionModel(transactionModels);
        response.setResult(Result.ORDERS_RETRIEVED);


        ServiceLogger.LOGGER.info("Exiting Order Retrieve Endpoint normally");
        return response.buildResponseWithHeaders(heads);
    }


}
