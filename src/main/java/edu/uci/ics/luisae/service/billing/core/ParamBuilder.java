package edu.uci.ics.luisae.service.billing.core;

import edu.uci.ics.luisae.service.billing.models.DeleteRequest;
import edu.uci.ics.luisae.service.billing.models.InsertUpdateRequest;
import edu.uci.ics.luisae.service.billing.models.RetrieveClearPlaceRequest;
import edu.uci.ics.luisae.service.billing.utilities.Param;

import java.sql.Types;

public class ParamBuilder {

    public static Param[] insert(InsertUpdateRequest request) {
        Param[] params = new Param[]{
                Param.create(Types.VARCHAR, request.getEmail()),
                Param.create(Types.VARCHAR, request.getMovie_id()),
                Param.create(Types.INTEGER, request.getQuantity())};
        return params;
    }
    public static Param[] update(InsertUpdateRequest request){
        Param[] params = new Param[]{
                Param.create(Types.INTEGER, request.getQuantity()),
                Param.create(Types.VARCHAR, request.getEmail()),
                Param.create(Types.VARCHAR, request.getMovie_id())};
        return params;
    }

    public static Param[] delete(DeleteRequest request){
        Param[] params = new Param[]{
                Param.create(Types.VARCHAR, request.getEmail()),
                Param.create(Types.VARCHAR, request.getMovie_id())};
        return params;
    }

    public static Param[] retrieve(RetrieveClearPlaceRequest request){
        Param[] params = new Param[]{
                Param.create(Types.VARCHAR, request.getEmail())};
        return params;
    }

    public static Param[] clear(RetrieveClearPlaceRequest request){
        Param[] params = new Param[]{
                Param.create(Types.VARCHAR, request.getEmail())};
        return params;
    }


}
