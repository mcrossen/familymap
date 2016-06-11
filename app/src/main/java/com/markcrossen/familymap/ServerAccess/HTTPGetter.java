package com.markcrossen.familymap.ServerAccess;

public interface HTTPGetter {
    public void rxData(String data);

    public void HTTPError(Exception error);
}
