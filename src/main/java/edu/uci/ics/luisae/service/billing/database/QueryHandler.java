package edu.uci.ics.luisae.service.billing.database;

import edu.uci.ics.luisae.service.billing.Base.Headers;
import edu.uci.ics.luisae.service.billing.Base.Result;
import edu.uci.ics.luisae.service.billing.BillingService;
import edu.uci.ics.luisae.service.billing.core.ClassBuilder;
import edu.uci.ics.luisae.service.billing.core.QueryBuilder;
import edu.uci.ics.luisae.service.billing.logger.ServiceLogger;
import edu.uci.ics.luisae.service.billing.models.BillingClasses.Items;
import edu.uci.ics.luisae.service.billing.models.MovieModels.ThumbnailResponse;
import edu.uci.ics.luisae.service.billing.models.NormalResponse;
import edu.uci.ics.luisae.service.billing.models.PlaceResponse;
import edu.uci.ics.luisae.service.billing.models.RetrieveResponse;
import edu.uci.ics.luisae.service.billing.utilities.Param;
import edu.uci.ics.luisae.service.billing.utilities.Util;

import javax.ws.rs.core.Response;
import java.sql.*;
import java.util.ArrayList;

public class QueryHandler {

    public static boolean hasDuplicate(String query){
        try{
            PreparedStatement ps = BillingService.getCon().prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }catch(SQLException e){
            ServiceLogger.LOGGER.warning("warning with hasDuplicate in QueryHandler");
            return true;
        }
    }

    public static boolean movieExists(String movieId, String email){
        String[] movieIds = {movieId};
        ThumbnailResponse response = Intercommunication.getThumbnails(movieIds, email);
        return (response.getThumbnails() != null);
    }

    public static Response insert(String query, Param[] params, NormalResponse response, Headers heads){
        int count;
        try{
            PreparedStatement ps = Util.prepareStatement(query,params);
            count = ps.executeUpdate();
            if(count <= 0){
                response.setResult(Result.OPERATION_FAILED);
                return response.buildResponseWithHeaders(heads);
            }
            response.setResult(Result.CART_INSERT_SUCCESSFUL);
            ServiceLogger.LOGGER.info("Exiting Insert Endpoint Normally");
            return response.buildResponseWithHeaders(heads);
        }catch(SQLException e){
            ServiceLogger.LOGGER.warning("SQL error in insert QueryHandler: " + e.getMessage());
            response.setResult(Result.OPERATION_FAILED);
            return response.buildResponseWithHeaders(heads);
        }
    }

    public static Response update(String query, Param[] params, NormalResponse response,Headers heads){
        int count;
        try{
            PreparedStatement ps = Util.prepareStatement(query,params);
            count = ps.executeUpdate();
            if(count <= 0){
                response.setResult(Result.CART_ITEM_DOES_NOT_EXIST);
                return response.buildResponseWithHeaders(heads);
            }
            response.setResult(Result.CART_ITEM_UPDATED_SUCCESSFULLY);
            ServiceLogger.LOGGER.info("Exiting Update Endpoint Normally");
            return response.buildResponseWithHeaders(heads);
        }catch(SQLException e){
            ServiceLogger.LOGGER.warning("error occurred in update QueryHandler: " + e.getMessage());
            response.setResult(Result.OPERATION_FAILED);
            return response.buildResponseWithHeaders(heads);
        }
    }

    public static Response delete(String query, Param[] params, NormalResponse response,Headers heads){
        int count;
        try{
            PreparedStatement ps = Util.prepareStatement(query,params);
            count = ps.executeUpdate();
            if(count <= 0){
                response.setResult(Result.CART_ITEM_DOES_NOT_EXIST);
                return response.buildResponseWithHeaders(heads);
            }
            ServiceLogger.LOGGER.info("Exiting Delete Endpoint Normally");
            response.setResult(Result.CART_ITEM_DELETION_SUCCESSFUL);
            return response.buildResponseWithHeaders(heads);
        }catch(SQLException e){
            ServiceLogger.LOGGER.warning("error occurred in delete QueryHandler: " + e.getMessage());
            response.setResult(Result.OPERATION_FAILED);
            return response.buildResponseWithHeaders(heads);
        }
    }

    public static Response retrieve(String query, Param[] params, RetrieveResponse response,Headers heads){
        //todo WRONG get response as an object not string, then loop through array
        //todo first get RS of all cart items associated with email then
        //todo make an array list of all movie_ids you got with the email
        //todo then do one thumbnail endpoint request and finish building retrieve objects
        ArrayList<Items> items = new ArrayList<>();
        String[] movie_ids;
        Items[] finalItems;
        ThumbnailResponse thumbResponse;
        try{
            PreparedStatement ps = Util.prepareStatement(query,params);
            ResultSet rs = ps.executeQuery();
            items = ClassBuilder.buildFromCart(rs);
            if(items != null){
                movie_ids = ClassBuilder.buildMovieIds(items);
                thumbResponse = Intercommunication.getThumbnails(movie_ids, heads.getEmail());
                if(thumbResponse == null){
                    ServiceLogger.LOGGER.warning("Receieved a null from Intercommunication.getThumbnails");
                    response.setResult(Result.OPERATION_FAILED);
                    return response.buildResponseWithHeaders(heads);
                }
                ClassBuilder.buildUpItems(thumbResponse, items);
                finalItems = ClassBuilder.buildItemsArray(items);
                response.setItems(finalItems);
                response.setResult(Result.SHOPPING_CART_RETRIEVED);
                ServiceLogger.LOGGER.info("Exiting Retrieve Endpoint Normally");
                return response.buildResponseWithHeaders(heads);
            }
            else{
                response.setResult(Result.CART_ITEM_DOES_NOT_EXIST);
                return response.buildResponseWithHeaders(heads);
            }


        }catch(SQLException e){
            ServiceLogger.LOGGER.warning("error occurred in retrieve QueryHandler: " + e.getMessage());
            response.setResult(Result.OPERATION_FAILED);
            return response.buildResponseWithHeaders(heads);}

    }

    public static Response clear(String query, Param[] params, NormalResponse response,Headers heads){
        int count;
        try{
            PreparedStatement ps = Util.prepareStatement(query,params);
            count = ps.executeUpdate();
            if(count <= 0){
                response.setResult(Result.CART_ITEM_DOES_NOT_EXIST);
                return response.buildResponseWithHeaders(heads);
            }
            response.setResult(Result.SHOPPING_CART_CLEARED);
            ServiceLogger.LOGGER.info("Exiting Clear Endpoint Normally");
            return response.buildResponseWithHeaders(heads);
        }catch(SQLException e){
            ServiceLogger.LOGGER.warning("error occurred in clear QueryHandler: " + e.getMessage());
            response.setResult(Result.OPERATION_FAILED);
            return response.buildResponseWithHeaders(heads);
        }
    }

    public static void place(PlaceResponse response, RetrieveResponse retrieveResponse, String orderId, Headers heads){
        java.sql.Date sqlDate = new Date(System.currentTimeMillis());
        try{
            CallableStatement cs = BillingService.getCon().prepareCall(QueryBuilder.saleAndTransactionQuery());
            for(Items item: retrieveResponse.getItems()){
                cs.setString(1,item.getEmail());
                cs.setString(2,item.getMovie_id());
                cs.setInt(3,item.getQuantity());
                cs.setDate(4,sqlDate);
                cs.setString(5,orderId);
                cs.executeUpdate();
            }
        }catch(SQLException e){
            ServiceLogger.LOGGER.warning("error occurred in place QueryHandler: " + e.getMessage());
            response.setResult(Result.OPERATION_FAILED);
        }
        ServiceLogger.LOGGER.info("Exiting Place Endpoint normally");
    }

    public static void complete(NormalResponse response, String token, String captureId){
        int count;
        try{
            PreparedStatement ps = BillingService.getCon().prepareStatement(QueryBuilder.completeQuery(token,captureId));
            count = ps.executeUpdate();
            //i dont know yet what update will return if token is found and previous value equals new value
            if(count <= 0)
                response.setResult(Result.TOKEN_NOT_FOUND);

        }catch(SQLException e){
            ServiceLogger.LOGGER.warning("error occurred in complete QueryHandler: " + e.getMessage());
            response.setResult(Result.ORDER_CAN_NOT_COMPLETE);
        }
    }

    public static void deleteCart(NormalResponse response, String token){
        //int count;
        String email;
        try{
            PreparedStatement ps = BillingService.getCon().prepareStatement(QueryBuilder.getEmailQuery(token));
            ResultSet rs = ps.executeQuery();
            if(rs.next())
                email = rs.getString("email");
            else{
                ServiceLogger.LOGGER.warning("result set empty in deletecart in query handler");
                return;
            }
            PreparedStatement ps2 = BillingService.getCon().prepareStatement(QueryBuilder.clearQuery(email));
            ps2.executeUpdate();
        }catch(SQLException e){
            ServiceLogger.LOGGER.warning("error occurred in delete QueryHandler: " + e.getMessage());
            response.setResult(Result.ORDER_CAN_NOT_COMPLETE);
        }
    }

    public static String[] getOrderIds(String query){
        ArrayList<String> orderIdsAL = new ArrayList<>();
        String[] orderIds;
        try{
            PreparedStatement ps = BillingService.getCon().prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while(rs.next())
                orderIdsAL.add(rs.getString("token"));
            if(orderIdsAL.isEmpty())
                return null;
            orderIds = new String[orderIdsAL.size()];
            orderIdsAL.toArray(orderIds);
            ServiceLogger.LOGGER.info(orderIds.toString());
            return orderIds;
        }catch(Exception e){
            ServiceLogger.LOGGER.info("error occurred in getOrderIds QueryHandler");
            return null;
        }
    }

    public static Items[] getOrderItems(String query){
        Items[] orderItems;
        ArrayList<Items> orderItemsAL;
        try{
            PreparedStatement ps = BillingService.getCon().prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            orderItemsAL = ClassBuilder.buildOrderItems(rs);
            if(orderItemsAL != null)
                orderItems = ClassBuilder.buildItemsArray(orderItemsAL);
            else{return null;}
            return orderItems;

        }
        catch(Exception e){
            ServiceLogger.LOGGER.info("error occurred in getOrderItems QueryHandler");
            return null;}
    }







}
