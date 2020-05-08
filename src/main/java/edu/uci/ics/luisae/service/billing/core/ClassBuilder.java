package edu.uci.ics.luisae.service.billing.core;

import edu.uci.ics.luisae.service.billing.logger.ServiceLogger;
import edu.uci.ics.luisae.service.billing.models.BillingClasses.Items;
import edu.uci.ics.luisae.service.billing.models.MovieModels.ThumbnailResponse;
import edu.uci.ics.luisae.service.billing.utilities.Util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ClassBuilder {

    public static ArrayList<Items> buildFromCart(ResultSet rs){
        ArrayList<Items> items = new ArrayList<>();
        try{
            while(rs.next()){
                items.add(new Items(
                        rs.getString("email"),
                        rs.getFloat("unit_price"),
                        rs.getFloat("discount"),
                        rs.getInt("quantity"),
                        rs.getString("movie_id")
                ));
            }
            if(items.isEmpty())
                return null;
        }catch(SQLException e){
            ServiceLogger.LOGGER.warning("SQLError while trying to build from Cart " + e.getMessage());
            return null;
        }
        return items;
    }

    public static String[] buildMovieIds(ArrayList<Items> items){
        String[] movie_ids = new String[items.size()];
        for(int i = 0; i < items.size();i++)
            movie_ids[i] = items.get(i).getMovie_id();
        return movie_ids;
    }


    public static void buildUpItems(ThumbnailResponse response, ArrayList<Items> items){
        for(int i = 0; i < items.size();i++){
            items.get(i).setMovie_title(response.getThumbnails()[i].getTitle());
            items.get(i).setBackdrop_path(response.getThumbnails()[i].getBackdrop_path());
            items.get(i).setPoster_path(response.getThumbnails()[i].getPoster_path());
        }
    }

    public static Items[] buildItemsArray(ArrayList<Items> items){
        Items[] returnItems = new Items[items.size()];
        for(int i = 0; i < items.size();i++)
            returnItems[i] = items.get(i);
        return returnItems;
    }

    public static ArrayList<Items> buildOrderItems(ResultSet rs){
        ArrayList<Items> items = new ArrayList<>();
        try{
            while(rs.next()){
                items.add(Util.modelMapper(rs.getString("Order"), Items.class));
            }
            if(items.isEmpty())
                return null;
        }catch(SQLException e){
            ServiceLogger.LOGGER.warning("SQLError while trying to build from OrderItems " + e.getMessage());
            return null;
        }
        return items;
    }






}
