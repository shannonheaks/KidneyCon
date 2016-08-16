package com.example.shannonhealy.kidneycon;

/**
 * Created by shannonhealy on 15/08/16.
 */
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ClinicTest extends AppCompatActivity {

    ListView listView;
    String mEmail;
    String mDate;
    GetLabInfoFromServer mLabInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinical_test_2);
        mEmail = getIntent().getStringExtra("username");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        mLabInfo = new GetLabInfoFromServer();
        mLabInfo.execute((Void)null);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public class GetLabInfoFromServer extends AsyncTask<Void, Void, Boolean> {

        private JSONParser2 jsonParser2 = new JSONParser2();
        ArrayList<String> jArrList = new ArrayList<>();
        JSONArray jArr;

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            List<NameValuePair> args = new ArrayList<NameValuePair>();
            args.add(new BasicNameValuePair("username", mEmail));


            String url_login_user = "http://ec2-52-209-206-101.eu-west-1.compute.amazonaws.com:8080/KidneyConnect/GetLabDate";

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
                String success = json.get("labReturn").toString();
                if(success.equals("success")){

                    jArr = (JSONArray)json.get("dates");

                    for(int i = 0; i < jArr.length(); ++i){
                        jArrList.add("Lab " + String.valueOf(i + 1) + " - " + jArr.get(i).toString().substring(0,10));
                    }

                    Log.d("JSONARRAY", jArr.toString());
                    //build string;
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

                ArrayAdapter adapter = new ArrayAdapter<String>(ClinicTest.this, R.layout.activity_listview, jArrList);

                listView = (ListView)findViewById(R.id.clinical_test_date_list);
                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        // When clicked, show a toast with the TextView text
                        try {
                            mDate = jArr.get(position).toString();
                        }catch (Exception e) {
                            Log.d("caught","Exception");
                        }
                        Intent myIntent = new Intent(ClinicTest.this, QRCodeAndDetailsActivity.class);
                        myIntent.putExtra("mEmail", mEmail);
                        myIntent.putExtra("mDate", mDate);
                        ClinicTest.this.startActivity(myIntent);
                    }
                });

            } else {
                Log.d("token sent to server", " fail");
            }
        }

        @Override
        protected void onCancelled() {
            Log.d("token sent to server", " cancel");
        }
    }
}

