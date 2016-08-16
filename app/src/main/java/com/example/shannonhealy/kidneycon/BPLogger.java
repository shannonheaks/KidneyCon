package com.example.shannonhealy.kidneycon;

/**
 * Created by shannonhealy on 15/08/16.
 */
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.example.shannonhealy.kidneycon.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BPLogger extends AppCompatActivity {

    EditText diastolic;
    EditText systolic;
    EditText bSugar;
    private String mEmail;
    JSONParser2 jsonParser2 = new JSONParser2();
    private UserVitalsTask vitalsTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bplog);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        mEmail = getIntent().getStringExtra("username");
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void submitVitals(View view){
        diastolic = (EditText)findViewById(R.id.diastolic);
        systolic = (EditText)findViewById(R.id.systolic);
        bSugar = (EditText)findViewById(R.id.bloodSugar);

        Log.d("dia", diastolic.getText().toString());
        Log.d("Sys", systolic.getText().toString());
        Log.d("bsug", bSugar.getText().toString());

        vitalsTask = new UserVitalsTask(mEmail,diastolic.getText().toString(),systolic.getText().toString(),bSugar.getText().toString());
        vitalsTask.execute((Void) null);

    }

    public class UserVitalsTask extends AsyncTask<Void, Void, Boolean> {

        private String mEmail;
        private String d;
        private String s;
        private String b;
        String fStat = "";



        UserVitalsTask(String email, String dys, String sys, String bs) {
            mEmail = email;
            s = sys;
            d = dys;
            b = bs;

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            List<NameValuePair> args = new ArrayList<NameValuePair>();
            args.add(new BasicNameValuePair("username", mEmail));
            args.add(new BasicNameValuePair("BP", d + "/" + s));
            args.add(new BasicNameValuePair("BS", b));


            String url_login_user = "http://ec2-52-209-206-101.eu-west-1.compute.amazonaws.com:8080/KidneyConnect/VitalsLog";

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
                String success = json.get("status").toString();
                if(success.equals("safe")){
                    fStat = "safe";

                    return true;
                }
                else if(success.equals("danger")){
                    fStat = "danger";

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
                finish();

                Context context = getApplicationContext();
                CharSequence text = fStat;
                int duration = Toast.LENGTH_LONG;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

                Intent redirect = new Intent(BPLogger.this, MainActivity.class);
                redirect.putExtra("username", mEmail.toString());
                BPLogger.this.startActivity(redirect);
            } else {

            }
        }

        @Override
        protected void onCancelled() {

        }
    }

}
