package com.markcrossen.familymap.ServerAccess;


public class BadConnectionException extends Exception {

    private int response_code;

    public BadConnectionException(int _response_code)
    {
        super();
        response_code = _response_code;
    }
    @Override
    public String getMessage()
    {
        return "bad connection with code " + Integer.toString(response_code) + ".";
    }
}
