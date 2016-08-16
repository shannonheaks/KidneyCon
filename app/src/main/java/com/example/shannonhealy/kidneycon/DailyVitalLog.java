package com.example.shannonhealy.kidneycon;

/**
 * Created by shannonhealy on 15/08/16.
 */
import android.content.Context;
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

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DailyVitalLog extends AppCompatActivity {

    private String mEmail;
    private patientVitalDetails vitalDetails;
    JSONParser2 jsonParser2 = new JSONParser2();
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    //private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_vital_log);
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

    public void submitSelfReport(View V) {
        //Generate json
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        EditText bloodSugarText = (EditText) findViewById(R.id.EditTextbloodSugar);
        EditText temperatureText = (EditText) findViewById(R.id.EditTextTemperature);
        EditText bloodPressureText = (EditText) findViewById(R.id.EditTextBloodPressure);
        //EditText pregTestText = (EditText) findViewById(R.id.EditTextPregTest);
        EditText howAreYouFeelingText = (EditText) findViewById(R.id.EditTextHowareyoufeeling);
        EditText bloodPressureTextDia = (EditText) findViewById(R.id.EditTextBloodPressureDia);

        String test = bloodSugarText.getText().toString();
        if(bloodSugarText.getText().toString().equals("") || temperatureText.getText().toString().equals("") ||
                bloodPressureText.getText().toString().equals("") || howAreYouFeelingText.getText().toString().equals("") ||
                bloodPressureTextDia.getText().toString().equals("")){
            fieldsNotComplete();
        }
        else {

            String finBP = bloodPressureText.getText().toString() + "/" + bloodPressureTextDia.getText().toString();

            vitalDetails = new patientVitalDetails(mEmail, bloodSugarText.getText().toString(), temperatureText.getText().toString(), finBP,
                    howAreYouFeelingText.getText().toString());
            vitalDetails.execute((Void) null);
        }

    }

    public void fieldsNotComplete(){

        Context context = getApplicationContext();
        CharSequence text = "Please complete all fields.";
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

    }


    class patientVitalDetails extends AsyncTask<Void,Void,Boolean>
    {
        private String mEmail;
        private String bloodSugar;
        private String bodyTemp;
        private String bloodPressure;
        //private String pregnency;
        private String feeling;
        String fStat = "";

        patientVitalDetails(String email, String bs, String temp, String bp,String feel)
        {
            mEmail = email;
            bloodSugar = bs;
            bodyTemp = temp;
            bloodPressure = bp;
            //pregnency = preg;
            feeling = feel;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            List<NameValuePair> aList = new ArrayList<NameValuePair>();
            aList.add(new BasicNameValuePair("username", mEmail));
            aList.add(new BasicNameValuePair("BS", bloodSugar));
            aList.add(new BasicNameValuePair("TEMP", bodyTemp));
            aList.add(new BasicNameValuePair("BP", bloodPressure));
            //aList.add(new BasicNameValuePair("PREGNANT", pregnency));
            aList.add(new BasicNameValuePair("FEELING", feeling));

            Log.d("list", aList.toString());

            String url_login_user = "http://ec2-52-209-206-101.eu-west-1.compute.amazonaws.com:8080/KidneyConnect/VitalsLog";

            JSONObject json;
            try{
                json = jsonParser2.makeHttpRequest(url_login_user,"POST",aList);

                Log.d("json return", json.toString());
            }
            catch (Exception e)
            {
                // there is an error with server connection
                return false;
            }


            try {
                String success = json.get("status").toString();
                if (success.equals("safe")) {
                    fStat = "Levels are Safe";

                    return true;
                } else if (success.equals("danger")) {
                    fStat = "Levels are in Danger";

                    return true;
                }

            } catch (Exception e) {
                return false;
            }

            return false;
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

//                Intent redirect = new Intent(DailyVitalLog.this, MainActivity.class);
//                redirect.putExtra("username", mEmail.toString());
//                DailyVitalLog.this.startActivity(redirect);
            } else {

            }
        }
    }
}
