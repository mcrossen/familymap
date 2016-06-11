package com.markcrossen.familymap.ServerAccess;


public interface HTTPPoster {
    public void txData(String result);

    public void HTTPError(Exception error);
}
