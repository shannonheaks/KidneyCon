package com.example.shannonhealy.kidneycon;


import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ljm7b on 3/4/2016.
 */
@TargetApi(Build.VERSION_CODES.CUPCAKE)
public class SendTokenToServer extends AsyncTask<Void, Void, Boolean> {

    private JSONParser2 jsonParser2 = new JSONParser2();
    private String token;
    private String email;

    SendTokenToServer(String tkn, String eml){
        token = tkn;
        email = eml;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        // TODO: attempt authentication against a network service.
        List<NameValuePair> args = new ArrayList<NameValuePair>();
        args.add(new BasicNameValuePair("token", token));
        args.add(new BasicNameValuePair("username", email));


        String url_login_user = "< TODO add_url_here >";
        Log.d("IN SEND TOKEN", token);

        JSONObject json;
        // getting JSON Object
        // Note that create product url accepts POST method
        try {
            json = jsonParser2.makeHttpRequest(url_login_user,
                    "POST", args);

            // check log cat from response
            Log.d("Create Response", json.toString());
        }
        catch(Exception e) {
            // there is an error with server connection
            return false;
        }


        try {
            String success = json.get("token").toString();
            if(success.equals("success")){
                return true;
            }

        } catch (Exception e) {
            return false;
        }


        // TODO: register the new account here.
        return false;
    }

    @Override
    protected void onPostExecute(final Boolean success) {

        if (success) {
            Log.d("token sent to server", " success");

        } else {
            Log.d("token sent to server", " fail");
        }
    }

    @Override
    protected void onCancelled() {
        Log.d("token sent to server", " cancel");
    }
}

