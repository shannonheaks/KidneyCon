package com.example.shannonhealy.kidneycon;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private boolean isReceiverRegistered;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    private String mEmail;
    private String patDate;
    private String patName;
    private String clinicAddress;
    private String clinicPhone;
    private String clinicEmail;
    private String dr;
    TextView patNameText;
    TextView patDateText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mEmail = getIntent().getStringExtra("username");
        clinicAddress = getIntent().getStringExtra("address");
        clinicEmail = getIntent().getStringExtra("email");
        clinicPhone = getIntent().getStringExtra("phone");
        dr = getIntent().getStringExtra("dr");

        patName = getIntent().getStringExtra("name");
        patNameText = (TextView)findViewById(R.id.patName);
        patNameText.setText(patName);

        patDate = getIntent().getStringExtra("date");
        patDateText =(TextView)findViewById(R.id.patDate);
        patDateText.setText(patDate);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                String[] info = {"Appt. For: " + patName,
                        "Appt. Date: " + patDate,
                        "Physician: " + dr,
                        "Address: " + clinicAddress,
                        "Phone: " + clinicPhone
                };
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setTitle("Appointment Info")
                        .setItems(info, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // The 'which' argument contains the index position
                                // of the selected item
                            }
                        });

                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //Your action here
                    }
                });

                alert.show();

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        drawer.openDrawer(Gravity.LEFT);

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //mRegistrationProgressBar.setVisibility(ProgressBar.GONE);
                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(context);
                boolean sentToken = sharedPreferences
                        .getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
                if (sentToken) {
                    //mInformationTextView.setText(getString(R.string.gcm_send_message));
                } else {
                    //mInformationTextView.setText(getString(R.string.token_error_message));
                }
            }
        };
        // mInformationTextView = (TextView) findViewById(R.id.informationTextView);

        // Registering BroadcastReceiver
        registerReceiver();

        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            intent.putExtra("username", mEmail.toString());
            startService(intent);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_paperwork) {
            Intent paperwork_intent = new Intent(this, VisitForm.class);
            paperwork_intent.putExtra("username", mEmail);
            startActivity(paperwork_intent);

        } else if (id == R.id.nav_directions) {

            Uri gmmIntentUri = Uri.parse("google.navigation:q=" + clinicAddress);
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);

        } else if (id == R.id.nav_log) {
            Intent log_intent = new Intent(this, DailyVitalLog.class);
            log_intent.putExtra("username", mEmail);
            startActivity(log_intent);

        } else if (id == R.id.nav_clinical_test) {

            Intent clinical_test = new Intent(this, ClinicTest.class);
            clinical_test.putExtra("username",mEmail);
            startActivity(clinical_test);

        } else if (id == R.id.nav_postvisit) {

            Intent clinical_test = new Intent(this, PostVisitForm.class);
            clinical_test.putExtra("username", mEmail);
            startActivity(clinical_test);

        }else if (id == R.id.nav_info) {

            Intent info_intent = new Intent(this, ClinicInformation.class);
            startActivity(info_intent);

        } else if (id == R.id.nav_phone) {
            String number = clinicPhone;
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" +number));
            try {
                startActivity(intent);
            }catch(Exception e){
                Log.d("PHONE PERM", "phone not available");
            }

        } else if (id == R.id.nav_email) {

            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto", clinicEmail, null));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "CLINIC QUESTION");
            startActivity(Intent.createChooser(emailIntent, "Send email..."));

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver();
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        isReceiverRegistered = false;
        super.onPause();
    }

    private void registerReceiver(){
        if(!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
            isReceiverRegistered = true;
        }
    }
    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i("NO PLAY SERVICE", "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    private void showInfoAlert(){

        String[] info = {"Date", "Time", "Dr", "Visit"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Appointment Details")
                .setItems(info, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                    }
                });
        builder.create();
    }
}
