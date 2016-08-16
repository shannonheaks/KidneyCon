package com.example.shannonhealy.kidneycon;

/**
 * Created by shannonhealy on 15/08/16.
 */
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ClinicInformation extends AppCompatActivity {
    JSONParser2 jsonParser2 = new JSONParser2();
    JSONObject jObj;
    TextView header;
    TextView address;
    TextView hours;
    TextView email;
    TextView phone;
    VisitFormTask vTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        header = (TextView)findViewById(R.id.header);

        address = (TextView)findViewById(R.id.address);
        hours = (TextView)findViewById(R.id.hours);
        email = (TextView)findViewById(R.id.email);
        phone = (TextView)findViewById(R.id.phone);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        vTask = new VisitFormTask();
        vTask.execute((Void) null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public class VisitFormTask extends AsyncTask<Void, Void, Boolean> {

        VisitFormTask() {

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            List<NameValuePair> args = new ArrayList<NameValuePair>();
//
            args.add(new BasicNameValuePair("username", "admin"));
//
//            Log.d("thee array", lStr.toString());
//            args.add(new BasicNameValuePair("objects", lStr.toString()));
            String url_login_user = "http://ec2-52-209-206-101.eu-west-1.compute.amazonaws.com:8080/KidneyConnect/GetClinicInformation";


            JSONObject json;
            // getting JSON Object
            // Note that create product url accepts POST method
            try {
                json = jsonParser2.makeHttpRequest(url_login_user,
                        "POST", args);
                jObj = json;
                // check log cat from response
                Log.d("Create Response", json.toString());
            } catch (Exception e) {
                // there is an error with server connection
                return false;
            }


            try {
                String success = json.get("getInfo").toString();
                if (success.equals("success")) {

                    return true;
                }

            } catch (Exception e) {
                return false;
            }

            return false;
            // TODO: register the new account here.
        }

        @Override
        protected void onPostExecute(final Boolean success) {

            if (success) {

                try{
                    header.setText(jObj.getString("clinicName"));
                    address.setText(jObj.getString("clinicAddress"));
                    hours.setText("Hours: " + jObj.getString("clinicHours"));
                    phone.setText("Phone: " + jObj.getString("clinicPhone"));
                    email.setText("Email: " + jObj.getString("clinicEmail"));
                }catch(Exception e){
                    Log.d("exception", "Clinic info");
                }


            } else {

            }
        }
    }

}
