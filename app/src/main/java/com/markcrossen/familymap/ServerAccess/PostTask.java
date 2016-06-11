package com.markcrossen.familymap.ServerAccess;

import android.os.AsyncTask;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PostTask extends AsyncTask<Void, Void, String> {

    private HTTPPoster who_called_me;
    private String post_data;
    private String handle;

    public PostTask(HTTPPoster _who_called_me, String _handle, String _post_data)
    {
        super();
        who_called_me = _who_called_me;
        handle = _handle;
        post_data = _post_data;
    }

    @Override
    protected String doInBackground(Void... params)
    {
        try {
            ServerInfo server_info = ServerInfo.getInstance();
            URL url = server_info.getURL(handle);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            //set the authorization token if necessary
            if (server_info.getAuth() != null)
            {
                connection.addRequestProperty("Authorization", server_info.getAuth());
            }

            connection.connect();

            // Write post data to request body
            OutputStream requestBody = connection.getOutputStream();
            requestBody.write(post_data.getBytes());
            requestBody.close();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {

                // Get response body input stream
                InputStream responseBody = connection.getInputStream();

                // Read response body bytes
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int length;
                while ((length = responseBody.read(buffer)) != -1) {
                    baos.write(buffer, 0, length);
                }

                // Convert response body bytes to a string
                return baos.toString();
            }
            else {
                throw new BadConnectionException(connection.getResponseCode());
            }

        } catch (Exception e) {
            error = e;
            return null;
        }
    }

    private Exception error;

    @Override
    protected void onPostExecute(String result)
    {
        if (error == null)
        {
            who_called_me.txData(result);
        }
        else
        {
            who_called_me.HTTPError(error);
        }
    }
}
