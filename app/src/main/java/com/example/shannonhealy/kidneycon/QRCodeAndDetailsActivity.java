package com.example.shannonhealy.kidneycon;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class QRCodeAndDetailsActivity extends AppCompatActivity {

    String mEmail;
    String mDate;
    GetLabInfoFromServer mLabInfo;
    String informationOnQRCodeString="Empty";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_qr_code);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        Intent intent = getIntent();
        mEmail = intent.getStringExtra("mEmail");
        mDate = intent.getStringExtra("mDate");


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

    public static Bitmap encodeToQrCode(String text, int width, int height){
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix matrix = null;
        try {
            matrix = writer.encode(text, BarcodeFormat.QR_CODE, 275, 275);
        } catch (WriterException ex) {
            ex.printStackTrace();
        }
        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        for (int x = 0; x < width; x++){
            for (int y = 0; y < height; y++){
                bmp.setPixel(x, y, matrix.get(x,y) ? Color.GREEN : Color.BLUE);
            }
        }
        return bmp;
    }

    public class GetLabInfoFromServer extends AsyncTask<Void, Void, Boolean> {

        private JSONParser2 jsonParser2 = new JSONParser2();
        ArrayList<String> jArrList = new ArrayList<>();
        JSONObject json;
        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            List<NameValuePair> args = new ArrayList<NameValuePair>();
            args.add(new BasicNameValuePair("username", mEmail));
            args.add(new BasicNameValuePair("date", mDate));


            String url_login_user = "< TODO add_url_here >";


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
                    JSONObject jsonObj1 = new JSONObject(json.get("lab").toString());

                    try {
                        informationOnQRCodeString="\n\nDate : "+jsonObj1.getString("dateTimeStamp")+"\n";
                        informationOnQRCodeString = informationOnQRCodeString +"Patient ID : "+jsonObj1.getString("patientID")+"\n";
                        informationOnQRCodeString = informationOnQRCodeString+"Patient Name : "+jsonObj1.getString("patientName")+"\n";
                        informationOnQRCodeString = informationOnQRCodeString + "Physician Name : "+jsonObj1.getString("physicianName")+"\n";
                        informationOnQRCodeString = informationOnQRCodeString+"Clinic Name : "+jsonObj1.getString("physicianClinicName")+"\n\n";

                        JSONArray jsonarr = jsonObj1.getJSONArray("Tests");
                        for(int i = 0; i < jsonarr.length(); i++){
                            JSONObject jsonobj = jsonarr.getJSONObject(i);
                            String name =jsonobj.getString("Name");
                            String specialInstruction=jsonobj.getString("Special_Instructions");
                            informationOnQRCodeString = informationOnQRCodeString+"Test Name : "+name+"\n";
                            informationOnQRCodeString = informationOnQRCodeString+"Test Special Instructions : "+specialInstruction+"\n\n";
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

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


                ImageView tv1;
                tv1= (ImageView) findViewById(R.id.qrCodeImageView);
                Bitmap bitmap1 = encodeToQrCode(informationOnQRCodeString,275, 275);
                tv1.setImageBitmap(bitmap1);
                TextView informationOnQRCodeTextView = (TextView)findViewById(R.id.qrInformation);
                informationOnQRCodeTextView.setText(informationOnQRCodeString);



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