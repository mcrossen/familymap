package com.markcrossen.familymap.ServerAccess;

import java.net.URL;

public class ServerInfo {
    private static ServerInfo server_info = new ServerInfo();

    private ServerInfo()
    {
    }

    public static ServerInfo getInstance()
    {
        return server_info;
    }

    private String address;

    private String port;

    private String auth;

    public URL getURL(String handle) throws java.net.MalformedURLException
    {
        return new URL("http://" + address + ":" + port + handle);
    }

    public void setAddress(String _address)
    {
        address = _address;
    }

    public void setPort(String _port)
    {
        port = _port;
    }

    public void setAuth(String _auth)
    {
        auth = _auth;
    }

    public String getAuth()
    {
        return auth;
    }

    public boolean isLoggedOn()
    {
        return address != null && port != null && auth != null;
    }

    public void clearLogin()
    {
        address = null;
        port = null;
        auth = null;
    }
}
