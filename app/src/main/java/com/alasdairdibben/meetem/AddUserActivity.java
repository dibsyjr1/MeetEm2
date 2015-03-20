package com.alasdairdibben.meetem;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.CheckBox;
import android.widget.Spinner;

public class AddUserActivity extends Activity {

    // Progress Dialog
    private ProgressDialog pDialog;

    JSONParser jsonParser = new JSONParser();
    EditText inputUsername;
    EditText inputPassword;
    EditText inputFirstName;
    EditText inputSurname;
    EditText inputHomeTown;
    CheckBox inputBar;
    CheckBox inputCafe;
    CheckBox inputShopping;
    CheckBox inputRestaurant;
    CheckBox inputEntertainment;
    Spinner inputPrice;
    Spinner inputRating;

    // url to create new user
    private static String url_create_user = "http://http://meetem.x10host.com/create_user.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_user);

        // Edit Text
        /*inputUsername = (EditText) findViewById(R.id.Input_Username);
        inputPassword = (EditText) findViewById(R.id.Input_Password);
        inputFirstName = (EditText) findViewById(R.id.Input_First_Name);
        inputSurname = (EditText) findViewById(R.id.Input_Surname);
        inputHomeTown = (EditText) findViewById(R.id.Input_Home_Town);
        inputBar = (CheckBox) findViewById(R.id.Input_Bar);
        inputCafe = (CheckBox) findViewById(R.id.Input_Cafe);
        inputShopping = (CheckBox) findViewById(R.id.Input_Shopping);
        inputRestaurant = (CheckBox) findViewById(R.id.Input_Restaurant);
        inputEntertainment = (CheckBox) findViewById(R.id.Input_Entertainment);
        inputPrice = (Spinner) findViewById(R.id.Input_Price);
        inputRating = (Spinner) findViewById(R.id.Input_Rating);

        // Create button
        Button btnCreateUser = (Button) findViewById(R.id.btnCreateUser);

       /* // button click event
        btnCreateUser.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // creating new user in background thread
                new CreateNewUser().execute();
            }
        });*/
    }

    /**
     * Background Async Task to Create new user
     * */
    /*class CreateNewUser extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        /*@Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AddUserActivity.this);
            pDialog.setMessage("Creating User..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Creating user
         * */
        /*protected String doInBackground(String... args) {
            String username = inputUsername.getText().toString();
            String password = inputPassword.getText().toString();
            String first_name = inputFirstName.getText().toString();
            String surname = inputSurname.getText().toString();
            String home_town = inputHomeTown.getText().toString();
            Boolean bar = findViewById(R.id.Input_Bar).isChecked();
            Boolean cafe = findViewById(R.id.Input_Cafe).isChecked();
            Boolean shopping = findViewById(R.id.Input_Shopping).isChecked();
            Boolean restaurant = findViewById(R.id.Input_Restaurant).isChecked();
            Boolean entertainment = findViewById(R.id.Input_Entertainment).isChecked();
            Integer price = (Integer) Input_Price.getValue();
            Integer rating = (Integer) Input_Rating.getValue();

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("Username", username));
            params.add(new BasicNameValuePair("Password", password));
            params.add(new BasicNameValuePair("First Name", first_name));
            params.add(new BasicNameValuePair("Surname", surname));
            params.add(new BasicNameValuePair("Home Town", home_town));
            params.add(new BasicNameValuePair("Bar", bar));
            params.add(new BasicNameValuePair("Cafe", cafe));
            params.add(new BasicNameValuePair("Shopping", shopping));
            params.add(new BasicNameValuePair("Restaurant", restaurant));
            params.add(new BasicNameValuePair("Entertainment", entertainment));
            params.add(new BasicNameValuePair("Price", price));
            params.add(new BasicNameValuePair("Rating", rating));

            // getting JSON Object
            // Note that create user url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_create_user,
                    "POST", params);

            // check log cat for response
            Log.d("Create Response", json.toString());

            // check for success tag
            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // successfully created user
                    Intent i = new Intent(getApplicationContext(), MenuActivity.class);
                    startActivity(i);

                    // closing this screen
                    finish();
                } else {
                    // failed to create user
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();
        }

    /*}*/
}
