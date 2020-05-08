package edu.uci.ics.luisae.service.billing.resources;


import edu.uci.ics.luisae.service.billing.Base.Headers;
import edu.uci.ics.luisae.service.billing.Base.Result;
import edu.uci.ics.luisae.service.billing.core.LogicHandler;
import edu.uci.ics.luisae.service.billing.database.Intercommunication;
import edu.uci.ics.luisae.service.billing.logger.ServiceLogger;
import edu.uci.ics.luisae.service.billing.models.*;
import edu.uci.ics.luisae.service.billing.utilities.Util;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("billing")
public class BillingEndpoints {

    @Path("cart/insert")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response cartInsert(@Context HttpHeaders headers,
                               String jsonText){
        ServiceLogger.LOGGER.info("Entering Insert Endpoint");
        Headers heads = Util.createHeaders(headers);
        NormalResponse response = new NormalResponse();
        InsertUpdateRequest request = Util.modelMapper(jsonText,InsertUpdateRequest.class,response);
        if(request == null)
            return response.buildResponse();
        if(!Intercommunication.hasPrivilege(request.getEmail(),5)){
            response.setResult(Result.USER_NOT_FOUND);
            return response.buildResponse();
        }
        return LogicHandler.InsertHandler(request,response,heads);
    }


    @Path("cart/update")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response cartUpdate(@Context HttpHeaders headers, String jsonText){
        ServiceLogger.LOGGER.info("Entering Update Endpoint");
        Headers heads = Util.createHeaders(headers);
        NormalResponse response = new NormalResponse();
        InsertUpdateRequest request = Util.modelMapper(jsonText,InsertUpdateRequest.class,response);
        if(request == null)
            return response.buildResponse();
        return LogicHandler.UpdateHandler(request,response,heads);
    }

    @Path("cart/delete")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response cartDelete(@Context HttpHeaders headers, String jsonText){
        ServiceLogger.LOGGER.info("Entering Delete Endpoint");
        Headers heads = Util.createHeaders(headers);
        NormalResponse response = new NormalResponse();
        DeleteRequest request = Util.modelMapper(jsonText, DeleteRequest.class,response);
        if(request == null)
            return response.buildResponse();
        return LogicHandler.DeleteHandler(request,response,heads);
    }


    @Path("cart/retrieve")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response cartRetrieve(@Context HttpHeaders headers, String jsonText){
        ServiceLogger.LOGGER.info("Entering Retrieve Endpoint");
        Headers heads = Util.createHeaders(headers);
        RetrieveResponse response = new RetrieveResponse();
        RetrieveClearPlaceRequest request = Util.modelMapper(jsonText, RetrieveClearPlaceRequest.class,response);
        if(request == null)
            return response.buildResponse();
        return LogicHandler.RetrieveHandler(request,response,heads);
    }

    @Path("cart/clear")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response cartClear(@Context HttpHeaders headers, String jsonText){
        ServiceLogger.LOGGER.info("Entering Clear Endpoint");
        Headers heads = Util.createHeaders(headers);
        NormalResponse response = new NormalResponse();
        RetrieveClearPlaceRequest request = Util.modelMapper(jsonText,RetrieveClearPlaceRequest.class,response);
        if(request == null)
            return response.buildResponse();
        return LogicHandler.ClearHandler(request,response,heads);
    }

    @Path("order/place")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response orderPlace(@Context HttpHeaders headers, String jsonText){
        ServiceLogger.LOGGER.info("Entering Order Place Endpoint");
        Response postResponse;
        Headers heads = Util.createHeaders(headers);
        RetrieveResponse retrieveResponse = new RetrieveResponse();
        RetrieveClearPlaceRequest request = Util.modelMapper(jsonText, RetrieveClearPlaceRequest.class, retrieveResponse);
        PlaceResponse response =  new PlaceResponse();
        if(request == null)
            return response.buildResponse();
        postResponse = cartRetrieve(headers,jsonText);
        retrieveResponse = (RetrieveResponse) postResponse.getEntity();
        if(retrieveResponse == null){
            response.setResult(Result.CART_ITEM_DOES_NOT_EXIST);
            return response.buildResponse();
        }
        return LogicHandler.PlaceHandler(request,response,heads,retrieveResponse);
    }

    @Path("order/complete")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response orderComplete(@QueryParam("token")String token,@QueryParam("payer_id") String payer_id){
        CompleteRequest request = new CompleteRequest(token,payer_id);
        NormalResponse response = new NormalResponse();

        return LogicHandler.CompleteHandler(request,response);
        //todo finish complete and place; by writing database stuff
    }

    @Path("order/retrieve")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response orderRetrieve(@Context HttpHeaders headers, String jsonText){
        ServiceLogger.LOGGER.info("Entering Order Retrieve Endpoint");
        Headers heads = Util.createHeaders(headers);
        OrderRetrieveResponse response = new OrderRetrieveResponse();
        RetrieveClearPlaceRequest request =  Util.modelMapper(jsonText, RetrieveClearPlaceRequest.class,response);
        return LogicHandler.OrderRetrieveHandler(request,response,heads);
    }




    @Path("order/dummy")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public void orderDummy(){}






}
