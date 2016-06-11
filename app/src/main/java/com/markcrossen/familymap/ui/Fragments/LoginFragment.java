package com.markcrossen.familymap.ui.Fragments;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.markcrossen.familymap.model.Filter;
import com.markcrossen.familymap.ui.Activities.LoginActivity;
import com.markcrossen.familymap.Constants;
import com.markcrossen.familymap.R;
import com.markcrossen.familymap.ServerAccess.GetTask;
import com.markcrossen.familymap.ServerAccess.HTTPGetter;
import com.markcrossen.familymap.ServerAccess.HTTPPoster;
import com.markcrossen.familymap.ServerAccess.PostTask;
import com.markcrossen.familymap.ServerAccess.ServerInfo;

import org.json.JSONObject;


public class LoginFragment extends Fragment implements HTTPPoster, HTTPGetter {

    private Button button;
    private EditText UserNameInput;
    private EditText PasswordInput;
    private EditText AddressInput;
    private EditText PortInput;

    private ServerInfo server_info;

    private Context context;
    private LoginActivity parent_activity;

    public LoginFragment() {
        // Required empty public constructor
        server_info = ServerInfo.getInstance();
    }

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public void setloginActivity(LoginActivity parent)
    {
        parent_activity = parent;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        button = (Button)v.findViewById(R.id.SignInButton);
        //create listener here
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSignInClicked();
            }
        });

        UserNameInput = (EditText)v.findViewById(R.id.UserNameInput);
        PasswordInput = (EditText)v.findViewById(R.id.PasswordInput);
        AddressInput = (EditText)v.findViewById(R.id.AddressInput);
        PortInput = (EditText)v.findViewById(R.id.PortInput);

        return v;
    }

    private void onSignInClicked()
    {
        onSignInClicked(
                UserNameInput.getText().toString(),
                PasswordInput.getText().toString(),
                AddressInput.getText().toString(),
                PortInput.getText().toString());
    }

    private void onSignInClicked(String username, String password, String server, String port)
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
    public void txData(String result)
    {
        try {
            JSONObject json_obj = new JSONObject(result);

            String person_id = json_obj.getString("personId");

            server_info.setAuth(json_obj.getString("Authorization"));
            Filter.getInstance().setUser(person_id);

            GetTask task = new GetTask(this,
                    "/person/" + person_id);

            task.execute();
        }
        catch(org.json.JSONException e)
        {
            Log.e(Constants.TAG, "Corrupt data received: " + result);
            HTTPError(e);
        }
    }

    @Override
    public void HTTPError(Exception error)
    {
        Log.e(Constants.TAG, "Unable to login. " + error.getMessage());
        Toast.makeText(context, "ERROR: unable to login.", Toast.LENGTH_LONG).show();
    }

    @Override
    public void rxData(String result)
    {
        try {
            JSONObject json_obj = new JSONObject(result);

            Toast.makeText(context, "welcome " + json_obj.getString("firstName") + " " + json_obj.getString("lastName"), Toast.LENGTH_LONG).show();

            if (parent_activity != null)
            {
                parent_activity.loginSuccessful();
            }

        } catch(org.json.JSONException e) {

            Log.e(Constants.TAG, "Corrupt data received: " + result);
            HTTPError(e);
        }
    }
}