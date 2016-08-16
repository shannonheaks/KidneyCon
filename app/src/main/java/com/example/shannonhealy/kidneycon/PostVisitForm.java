package com.example.shannonhealy.kidneycon;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.github.dkharrat.nexusdialog.FormWithAppCompatActivity;
import com.github.dkharrat.nexusdialog.controllers.EditTextController;
import com.github.dkharrat.nexusdialog.controllers.FormSectionController;
import com.github.dkharrat.nexusdialog.controllers.SelectionController;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PostVisitForm extends FormWithAppCompatActivity {

    String[] vars = {"q1", "q2", "q3", "q4", "q5", "q6", "q7", "q8", "q9", "q10"};
    String mEmail;
    JSONObject jo;
    JSONParser2 jsonParser2 = new JSONParser2();
    VisitFormTask vTask;
    List<String> lStr;
    @Override
    protected void initForm() {
        setTitle("Post Visit Form");
        mEmail = getIntent().getStringExtra("username");

        FormSectionController section1 = new FormSectionController(this, "1. How likely is it that you would recommend your Doctor to a friend or family member?");
        section1.addElement(new SelectionController(this, "q1", "Please select an option", true, "Select",
                Arrays.asList("0 - Not at all likely", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10 - Extremely Likely"), true));
        getFormController().addSection(section1);

        FormSectionController section2 = new FormSectionController(this, "2. Overall, how satisfied or dissatisfied were you with your last visit to our office?");
        section2.addElement(new SelectionController(this, "q2", "Please select an option", true, "Select",
                Arrays.asList("Very satisfied", "Somewhat satisfied", "Neither satisfied nor dissatisfied", "Somewhat dissatisfied", "Very dissatisfied"), true));
        getFormController().addSection(section2);

        FormSectionController section3 = new FormSectionController(this, "3. How easy or difficult was it to schedule your appointment at a time that was convenient for you?");
        section3.addElement(new SelectionController(this, "q3", "Please select an option", true, "Select",
                Arrays.asList("Very easy", "Somewhat easy", "Neither easy nor difficult", "Somewhat difficult", "Very difficult"), true));
        getFormController().addSection(section3);

        FormSectionController section4 = new FormSectionController(this, "4. How convenient was the appointment time you were able to get?");
        section4.addElement(new SelectionController(this, "q4", "Please select an option", true, "Select",
                Arrays.asList("Extremely convenient", "Very convenient", "Somewhat convenient", "Not so convenient", "Not at all convenient"), true));
        getFormController().addSection(section4);

        FormSectionController section5 = new FormSectionController(this, "5. In your opinion, how convenient is the location of our office?");
        section5.addElement(new SelectionController(this, "q5", "Please select an option", true, "Select",
                Arrays.asList("Extremely convenient", "Very convenient", "Somewhat convenient", "Not so convenient", "Not at all convenient"), true));
        getFormController().addSection(section5);

        FormSectionController section6 = new FormSectionController(this, "6. Overall, how would your rate the service you received from the staff at our office?");
        section6.addElement(new SelectionController(this, "q6", "Please select an option", true, "Select",
                Arrays.asList("Excellent", "Very good", "Good", "Fair", "Poor"), true));
        getFormController().addSection(section6);

        FormSectionController section7 = new FormSectionController(this, "7. Overall, how would you rate the care you received from your Doctor?");
        section7.addElement(new SelectionController(this, "q7", "Please select an option", true, "Select",
                Arrays.asList("Excellent", "Very good", "Good", "Fair", "Poor"), true));
        getFormController().addSection(section7);

        FormSectionController section8 = new FormSectionController(this, "8. How well did your Doctor explain your treatment option?");
        section8.addElement(new SelectionController(this, "q8", "Please select an option", true, "Select",
                Arrays.asList("Extremely well", "Very well", "Somewhat well", "Not so well", "Not at all well"), true));
        getFormController().addSection(section8);


        FormSectionController section9 = new FormSectionController(this, "9. Is there anything we could have done to improve your last visit?");
        section9.addElement(new EditTextController(this, "q9", "Please Enter Here"));
        getFormController().addSection(section9);

        FormSectionController section10 = new FormSectionController(this, "10. How well did your Doctor explain your follow-up care?");
        section10.addElement(new SelectionController(this, "q10", "Please select an option", true, "Select",
                Arrays.asList("Extremely well", "Very well", "Somewhat well", "Not so well", "Not at all well"), true));
        getFormController().addSection(section10);

       getModel().addPropertyChangeListener("q10", new PropertyChangeListener() {
           @Override
           public void propertyChange(PropertyChangeEvent event) {
               Log.i("tag", "Value was: " + event.getOldValue() + ", now: " + event.getNewValue());
               getVars();
               vTask = new VisitFormTask(jo, lStr);
               vTask.execute((Void) null);
           }
       });

    }

    public void getVars() {
        jo = new JSONObject();
        lStr = new ArrayList<>();
        for (int i = 0; i < 18; ++i) {

            try {

                if (getModel().getValue(vars[i]) != null) {
                    Log.d(vars[i], getModel().getValue(vars[i]).toString());
                    jo.put(vars[i], getModel().getValue(vars[i]).toString());
                    lStr.add(vars[i]);

                }
                else{
                    jo.put(vars[i], "No Response");
                    lStr.add(vars[i]);

                }

            } catch (Exception e) {
                Log.d("JSON", "error");
            }
        }
    }


    public class VisitFormTask extends AsyncTask<Void, Void, Boolean> {

        JSONObject jObj;
        List<String> str;

        VisitFormTask(JSONObject j, List<String> s) {
            jObj = j;
            str = s;

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            List<NameValuePair> args = new ArrayList<NameValuePair>();

            args.add(new BasicNameValuePair("username", mEmail));


            for (int i = 0; i < 10; ++i) {
                try {
                    args.add(new BasicNameValuePair(lStr.get(i), jObj.getString(lStr.get(i))));

                } catch (Exception e) {
                    Log.d("in-background", "build name val");
                }

            }
            Log.d("thee array", lStr.toString());
            args.add(new BasicNameValuePair("objects", lStr.toString()));
            String url_login_user = "< TODO add_url_here >";
            //String url_login_user = "192.168.1.15:8080/CliniConnectAdmin/VisitForm";

            JSONObject json;
            // getting JSON Object
            // Note that create product url accepts POST method
            try {
                json = jsonParser2.makeHttpRequest(url_login_user,
                        "POST", args);

                // check log cat from response
                Log.d("Create Response", json.toString());
            } catch (Exception e) {
                // there is an error with server connection
                return false;
            }


            try {
                String success = json.get("success").toString();
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
                finish();

                Context context = getApplicationContext();
                CharSequence text = "Form Submitted, Thank you for your Feedback";
                int duration = Toast.LENGTH_LONG;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

//                Intent redirect = new Intent(PostVisitForm.this, MainActivity.class);
//                redirect.putExtra("username", mEmail);
//                PostVisitForm.this.startActivity(redirect);
            } else {
                Context context = getApplicationContext();
                CharSequence text = "Error Submitting Form, Try Again Later.";
                int duration = Toast.LENGTH_LONG;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        }
    }
}
