package edu.uci.ics.luisae.service.billing.database;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.luisae.service.billing.Base.Result;
import edu.uci.ics.luisae.service.billing.BillingService;
import edu.uci.ics.luisae.service.billing.logger.ServiceLogger;
import edu.uci.ics.luisae.service.billing.models.IdmModels.PrivilegeRequest;
import edu.uci.ics.luisae.service.billing.models.IdmModels.RegisterAndPrivilegeResponse;
import edu.uci.ics.luisae.service.billing.models.MovieModels.ThumbnailRequest;
import edu.uci.ics.luisae.service.billing.models.MovieModels.ThumbnailResponse;
import org.glassfish.jersey.jackson.JacksonFeature;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

public class Intercommunication {
    public static boolean hasPrivilege(String email, int plevel){
        PrivilegeRequest request = new PrivilegeRequest(email, plevel);
        RegisterAndPrivilegeResponse privilegeResponse;

        String servicePath = getIdmPath();
        String privilegePath = BillingService.getIdmConfigs().getPrivilegePath();
        Client client = ClientBuilder.newClient();
        client.register(JacksonFeature.class);

        WebTarget webTarget = client.target(servicePath).path(privilegePath);
        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON_TYPE);
        Response response = invocationBuilder.post(Entity.entity(request, MediaType.APPLICATION_JSON));

        try{
            ObjectMapper mapper = new ObjectMapper();
            String jsonText = response.readEntity(String.class);
            //todo fix privilegeResponse class so we dont have to do String.endsWith
            //todo problem has to do with mapping and result
            privilegeResponse = mapper.readValue(jsonText, RegisterAndPrivilegeResponse.class);
            if(jsonText.endsWith("140}"))
                privilegeResponse.setResult(Result.SUFFICIENT_PLEVEL);
            else
                privilegeResponse.setResult(Result.INSUFFICIENT_PLEVEL);
        }catch(IOException e){
            ServiceLogger.LOGGER.warning("Unable to map response to POJO: INTERCOMMUNICATION PRIVILEGE");
            return false;
        }
        return (privilegeResponse.getResultCode() == Result.SUFFICIENT_PLEVEL.getResultCode());
    }

    public static ThumbnailResponse getThumbnails(String[] movie_ids, String email){
        ThumbnailRequest request = new ThumbnailRequest();
        request.setMovie_ids(movie_ids);
        ThumbnailResponse thumbresponse;

        String servicePath = getMoviePath();
        String privilegePath = BillingService.getMoviesConfigs().getThumbnailPath();
        Client client = ClientBuilder.newClient();
        client.register(JacksonFeature.class);

        WebTarget webTarget = client.target(servicePath).path(privilegePath);
        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON_TYPE).header("email", email);
        Response response = invocationBuilder.post(Entity.entity(request, MediaType.APPLICATION_JSON));


        try{
            ObjectMapper mapper = new ObjectMapper();
            String jsonText = response.readEntity(String.class);
            thumbresponse = mapper.readValue(jsonText, ThumbnailResponse.class);
            return thumbresponse;
        }catch(IOException e){
            ServiceLogger.LOGGER.warning("Unable to map response to POJO: Intercommunication getThumbnails");
            return null;
        }

    }







    public static String getIdmPath(){
        return BillingService.getIdmConfigs().getScheme() + BillingService.getIdmConfigs().getHostName() + ":"
                + BillingService.getIdmConfigs().getPort() + BillingService.getIdmConfigs().getPath();
    }

    public static String getMoviePath(){
        return BillingService.getMoviesConfigs().getScheme() + BillingService.getMoviesConfigs().getHostName() + ":"
                + BillingService.getMoviesConfigs().getPort() + BillingService.getMoviesConfigs().getPath();
    }

    public static String getBillingPath(){
        return BillingService.getServiceConfigs().getScheme() + BillingService.getServiceConfigs().getHostName() + ":"
                + BillingService.getServiceConfigs().getPort() + BillingService.getServiceConfigs().getPath();
    }
}
