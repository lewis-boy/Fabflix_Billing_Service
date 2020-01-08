package edu.uci.ics.luisae.service.billing.Base;

import javax.ws.rs.core.Response.Status;

public enum Result {

    PLEVEL_OUT_OF_RANGE  (-14, "Privilege level out of valid range.", Status.BAD_REQUEST),
    TOKEN_INVALID_LENGTH (-13, "Token has invalid length.",Status.BAD_REQUEST),
    PASSWORD_INVALID_LENGTH (-12, "Password has invalid length.",Status.BAD_REQUEST),
    EMAIL_INVALID_FORMAT (-11, "Email address has invalid format",Status.BAD_REQUEST),
    EMAIL_INVALID_LENGTH (-10, "Email address has invalid length",Status.BAD_REQUEST),
    JSON_PARSE_EXCEPTION   (-3, "JSON Parse Exception.",   Status.BAD_REQUEST),
    JSON_MAPPING_EXCEPTION (-2, "JSON Mapping Exception.", Status.BAD_REQUEST),

    INTERNAL_SERVER_ERROR  (-1, "Internal Server Error.",  Status.INTERNAL_SERVER_ERROR),

    PASSWORD_DONT_MATCH             (11, "Passwords do not match.",Status.OK),
    PASSWORD_LENGTH_UNSATISFIED (12, "Password does not meet length requirements.", Status.OK),
    PASSWORD_CHARS_UNSATISFIED(13, "Password does not meet character requirements.", Status.OK),
    USER_NOT_FOUND(14,"User not found.",Status.OK),
    EMAIL_ALREADY_IN_USE(16,"Email already in use.", Status.OK),
    INVALID_QUANTITY(33,"Quantity has invalid value.",Status.OK),
    REGISTER_SUCCESS(110, "User registered successfully.", Status.OK),
    LOGGED_IN_SUCCESSFULLY(120, "User logged in successfully.",Status.OK),
    SESSION_ACTIVE(130, "Session is active.", Status.OK),
    SESSION_EXPIRED(131, "Session is expired", Status.OK),
    SESSION_CLOSED(132, "Session is closed.",Status.OK),
    SESSION_REVOKED(133, "Session is revoked.",Status.OK),
    SESSION_NOT_FOUND(134, "Session not found.",Status.OK),
    SUFFICIENT_PLEVEL(140,"User has sufficient privilege level.",Status.OK),
    INSUFFICIENT_PLEVEL(141,"User has insufficient privilege level.",Status.OK),
    MOVIE_FOUND(210,"Found movie(s) with search parameters.", Status.OK),
    MOVIE_NOT_FOUND(211, "No movies found with search parameters.", Status.OK),
    PEOPLE_FOUND(212, "Found people with the search parameters.",Status.OK),
    PEOPLE_NOT_FOUND(213,"No people found with search parameters.", Status.OK),
    DUPLICATE_INSERTION(311,"Duplicate insertion.",Status.OK),
    CART_ITEM_DOES_NOT_EXIST(312,"Shopping cart item does not exist.",Status.OK),
    ORDER_HISTORY_DOES_NOT_EXIST(313,"Order history does not exist...",Status.OK),
    ORDER_CREATION_FAILED(342,"Order creation failed.",Status.OK),
    CART_INSERT_SUCCESSFUL(3100,"Shopping cart item inserted successfully.",Status.OK),
    CART_ITEM_UPDATED_SUCCESSFULLY(3110,"Shopping cart item updated successfully.",Status.OK),
    CART_ITEM_DELETION_SUCCESSFUL(3120,"Shopping cart item deleted successfully.",Status.OK),
    SHOPPING_CART_RETRIEVED(3130,"Shopping cart retrieved successfully.",Status.OK),
    SHOPPING_CART_CLEARED(3140,"Shopping cart cleared successfully.",Status.OK),
    OPERATION_FAILED(3150,"Shopping cart operation failed.",Status.OK),
    ORDER_SUCCESSFUL(3400, "Order placed successfully.",Status.OK),
    ORDERS_RETRIEVED(3410,"Orders retrieved successfully.",Status.OK),
    ORDER_COMPLETED(3420,"Order is completed successfully.",Status.OK),
    TOKEN_NOT_FOUND(3421,"Token not found.",Status.OK),
    ORDER_CAN_NOT_COMPLETE(3422,"Order can not be completed.",Status.OK);







    private final int    resultCode;
    private final String message;
    private final Status status;

    Result(int resultCode, String message, Status status){
        this.resultCode = resultCode;
        this.message = message;
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }
    public String getMessage() {
        return message;
    }
    public int getResultCode() {
        return resultCode;
    }
}
