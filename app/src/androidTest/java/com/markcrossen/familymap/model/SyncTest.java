package com.markcrossen.familymap.model;

import android.util.Log;

import com.markcrossen.familymap.Constants;
import com.markcrossen.familymap.ServerAccess.HTTPPoster;
import com.markcrossen.familymap.ServerAccess.PostTask;
import com.markcrossen.familymap.ServerAccess.ServerInfo;

import junit.framework.TestCase;

import org.json.JSONObject;

/*
To get the test to function properly while still taking into account the asynchronous sync task, I
used a loop that I would never use in real code (except for perhaps programming embedded systems).
A lot of this code was done directly in activities or fragments, so i tested it as best as I could.
*/
public class SyncTest extends TestCase implements SyncActivity, HTTPPoster {

    //Change the following parameters as needed:
    private final static String USERNAME = "user";
    private final static String PASSWORD = "password";
    private final static String ADDRESS = "pandora.byu.edu";
    private final static String PORT = "8080";


    volatile boolean sync_finished = false; // the 'volatile' keyword indicates the variable will be accessed by different threads.
    Exception sync_error = null;
    ServerInfo server_info;
    Model model;

    public void SyncError(Exception e) // This method is part of a interface to handle data syncs
    {
        sync_error = e;
        sync_finished = true;
    }

    public void SyncDone() // This method is part of a interface to handle data syncs
    {
        sync_finished = true;
    }

    private void Login(String username, String password, String server, String port) // Drives the login asynchronous task
    {
        String post_data =
                "{\n" +
                        "username:" + username + ",\n" +
                        "password:" + password + "\n" +
                        "}";

        //update model with web server info
        server_info.setAddress(server);
        server_info.setPort(port);

        //access web service with async task
        PostTask task = new PostTask(this,
                "/user/login",
                post_data);

        task.execute();
    }

    @Override
    public void txData(String result) // The results from the login asynchronous task
    {
        try {
            JSONObject json_obj = new JSONObject(result);

            String person_id = json_obj.getString("personId");

            server_info.setAuth(json_obj.getString("Authorization"));
            Filter.getInstance().setUser(person_id);
            sync_finished = true;
        }
        catch(org.json.JSONException e)
        {
            Log.e(Constants.TAG, "Corrupt data received: " + result);
            HTTPError(e);
        }
    }

    @Override
    public void HTTPError(Exception error) //called when the asynchronous task fails
    {
        sync_error = error;
        sync_finished = true;
    }


    // This test case is also thoroughly used as part of the other tests.
    public void testSync() throws Exception
    {
        model = Model.getInstance();
        server_info = ServerInfo.getInstance();

        Login(USERNAME, PASSWORD, ADDRESS, PORT); // Login to the server.
        while (!sync_finished) // Loop until the asynchronous task is done.
        {
        }
        if (sync_error != null)
        {
            throw sync_error;
        }

        sync_finished = false;
        model.syncData(this); // Sync data with the server
        while (!sync_finished) // Loop until the asynchronous task is done.
        {
        }
        if (sync_error != null)
        {
            throw sync_error;
        }
    }
}
