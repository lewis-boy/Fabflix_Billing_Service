package edu.uci.ics.luisae.service.billing.core;

import edu.uci.ics.luisae.service.billing.models.DeleteRequest;
import edu.uci.ics.luisae.service.billing.models.InsertUpdateRequest;
import edu.uci.ics.luisae.service.billing.models.RetrieveClearPlaceRequest;

public class QueryBuilder {
    //todo fix to get rid of variables not used

    public static String duplicateQuery(String email, String movieId) {
        return "select 1 from cart where email = '" + email +"' && movie_id = '" + movieId +"';";
    }

    public static String insertQuery(InsertUpdateRequest request){
        return "insert into cart(email,movie_id,quantity) values(?,?,?);";
    }
    public static String updateQuery(InsertUpdateRequest request){
        return "update cart set quantity = ? where email = ? && movie_id = ?;";
    }

    public static String deleteQuery(DeleteRequest request){
        return "delete from cart where email = ? && movie_id = ?;";
    }


    public static String retrieveQuery(RetrieveClearPlaceRequest request){
        return "select c.email,mp.unit_price,mp.discount,c.quantity,c.movie_id" +
                " from cart as c join movie_price as mp " +
                "on c.movie_id = mp.movie_id where email = ?;";
    }

    public static String clearQuery(RetrieveClearPlaceRequest request){
        return "delete from cart where email = ?;";
    }
    public static String clearQuery(String email){
        return "delete from cart where email = '"+email+"';";
    }

    public static String saleAndTransactionQuery(){
        return "{call insert_sales_transactions(?,?,?,?,?)}";
    }

    public static String completeQuery(String token, String captureId){
        return "UPDATE transaction SET capture_id = '"+ captureId +"' WHERE token = '"+ token +"';";
    }

    public static String getEmailQuery(String token){
        return "select s.email from sale as s join transaction as t on s.sale_id=t.sale_id where t.token = '"+token+"';";
    }

    public static String getTokensforEmail(String email){
        return "select t.token from sale as s join transaction as t on s.sale_id=t.sale_id where s.email = '"+email+"' && t.capture_id is not null;";
    }

    public static String getOrderItems(String token){
        String query = "select JSON_OBJECT('movie_id', mp.movie_id,\n" +
                "                    'email', info.email,\n" +
                "                    'quantity', info.quantity,\n" +
                "                    'sale_date', info.sale_date,\n" +
                "                    'unit_price', mp.unit_price,\n" +
                "                    'discount', mp.discount) as 'Order' from (select s.movie_id,s.email,s.quantity,s.sale_date from transaction as t join sale s on t.sale_id = s.sale_id where t.token = '"+
                token + "') as info join\n" +
        "     movie_price as mp on info.movie_id=mp.movie_id";


        return query;
    }


}
