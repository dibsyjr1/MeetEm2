package com.alasdairdibben.meetem;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class OptionsActivity extends Activity {

    EditText inputUsername;
    CheckBox inputBar;
    CheckBox inputCafe;
    CheckBox inputAmusement;
    CheckBox inputBooks;
    CheckBox inputArt;
    CheckBox inputRestaurant;
    CheckBox inputCinema;
    CheckBox inputBowling;
    CheckBox inputClothing;
    CheckBox inputClub;
    CheckBox inputDepartment;
    CheckBox inputElectronics;
    CheckBox inputJewellery;
    CheckBox inputMall;
    CheckBox inputMuseum;
    CheckBox inputShoes;
    CheckBox inputPark;
    Spinner inputPrice;
    Spinner inputRating;

    ImageButton btnSave;

    String User_ID;



    // Progress Dialog
    private ProgressDialog pDialog;

    // JSON parser class
    JSONParser jsonParser = new JSONParser();

    // single group url
    private static final String url_user_details = "http://meetem.x10host.com/get_user_details.php";

    // url to update group
    private static final String url_update_user = "http://meetem.x10host.com/update_user.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_USER = "user";
    private static final String TAG_UID = "User_ID";
    private static final String TAG_USERNAME = "Username";
    private static final String TAG_BAR = "Bar";
    private static final String TAG_CAFE = "Cafe";
    private static final String TAG_AMUSEMENT = "Amusement_Park";
    private static final String TAG_BOOKS = "Book_Store";
    private static final String TAG_ART = "Art_Gallery";
    private static final String TAG_RESTAURANT = "Restaurant";
    private static final String TAG_CINEMA = "Movie_Theater";
    private static final String TAG_BOWLING = "Bowling_Alley";
    private static final String TAG_CLOTHING = "Clothing_Store";
    private static final String TAG_SHOES = "Shoe_Store";
    private static final String TAG_CLUB = "Night_Club";
    private static final String TAG_DEPARTMENT = "Department_Store";
    private static final String TAG_ELECTRONICS = "Electronics_Store";
    private static final String TAG_JEWELLERY = "Jewellery_Store";
    private static final String TAG_MALL = "Shopping_Mall";
    private static final String TAG_MUSEUM = "Museum";
    private static final String TAG_PARK = "Park";
    private static final String TAG_PRICE = "Price";
    private static final String TAG_RATING = "Rating";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        ArrayAdapter rating_adapter = ArrayAdapter.createFromResource(this, R.array.rating_array, android.R.layout.simple_spinner_item);

        ArrayAdapter price_adapter = ArrayAdapter.createFromResource(this, R.array.price_array, android.R.layout.simple_spinner_item);

        inputRating = (Spinner) findViewById(R.id.Input_Rating);


// Set the layout to use for each dropdown item
        rating_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        inputRating.setAdapter(rating_adapter);

        inputPrice = (Spinner) findViewById(R.id.Input_Price);

// Set the layout to use for each dropdown item
        price_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        inputPrice.setAdapter(price_adapter);

        // getting group details from intent
        Intent i = getIntent();

        // getting group id (gid) from intent
        User_ID = i.getStringExtra(TAG_UID);

        // save button
        btnSave = (ImageButton) findViewById(R.id.btnSave);

        // Getting complete group details in background thread
        new GetUserDetails().execute();

        // save button click event
        btnSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (inputUsername != null) {
                    // starting background task to update group
                    new SaveUserDetails().execute();
                }
            }
        });



    }

    /**
     * Background Async Task to Get complete group details
     * */
    class GetUserDetails extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(OptionsActivity.this);
            pDialog.setMessage("Loading User Details. Please Wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Getting group details in background thread
         * */
        protected String doInBackground(String... params) {

            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    // Check for success tag
                    int success;
                    try {
                        // Building Parameters
                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("User_ID", User_ID));

                        Log.v(User_ID, "User_ID");

                        // getting group details by making HTTP request
                        // Note that group details url will use GET request
                        JSONObject json = jsonParser.makeHttpRequest(
                                url_user_details, "GET", params);

                        // check your log for json response
                        Log.d("Single User Details", json.toString());

                        // json success tag
                        success = json.getInt(TAG_SUCCESS);
                        if (success == 1) {
                            // successfully received group details
                            JSONArray userObj = json
                                    .getJSONArray(TAG_USER); // JSON Array

                            // get first group object from JSON Array
                            JSONObject user = userObj.getJSONObject(0);

                            // product with this gid found
                            inputUsername = (EditText) findViewById(R.id.Input_Username);
                            inputBar = (CheckBox) findViewById(R.id.Input_Bar);
                            inputCafe = (CheckBox) findViewById(R.id.Input_Cafe);
                            inputAmusement = (CheckBox) findViewById(R.id.Input_Amusement);
                            inputBooks = (CheckBox) findViewById(R.id.Input_Books);
                            inputArt = (CheckBox) findViewById(R.id.Input_Art);
                            inputShoes = (CheckBox) findViewById(R.id.Input_Shoes);
                            inputRestaurant = (CheckBox) findViewById(R.id.Input_Restaurant);
                            inputCinema = (CheckBox) findViewById(R.id.Input_Cinema);
                            inputBowling = (CheckBox) findViewById(R.id.Input_Bowling);
                            inputClothing = (CheckBox) findViewById(R.id.Input_Clothing);
                            inputClub = (CheckBox) findViewById(R.id.Input_Club);
                            inputDepartment = (CheckBox) findViewById(R.id.Input_Department);
                            inputElectronics = (CheckBox) findViewById(R.id.Input_Electronics);
                            inputJewellery = (CheckBox) findViewById(R.id.Input_Jewellery);
                            inputMall = (CheckBox) findViewById(R.id.Input_Mall);
                            inputMuseum = (CheckBox) findViewById(R.id.Input_Museum);
                            inputPark = (CheckBox) findViewById(R.id.Input_Park);
                            inputPrice = (Spinner) findViewById(R.id.Input_Price);
                            inputRating = (Spinner) findViewById(R.id.Input_Rating);

                            inputBar.setChecked(false);
                            inputShoes.setChecked(false);
                            inputCafe.setChecked(false);
                            inputAmusement.setChecked(false);
                            inputBooks.setChecked(false);
                            inputArt.setChecked(false);
                            inputRestaurant.setChecked(false);
                            inputCinema.setChecked(false);
                            inputBowling.setChecked(false);
                            inputClothing.setChecked(false);
                            inputClub.setChecked(false);
                            inputDepartment.setChecked(false);
                            inputElectronics.setChecked(false);
                            inputJewellery.setChecked(false);
                            inputMall.setChecked(false);
                            inputMuseum.setChecked(false);
                            inputPark.setChecked(false);

                            // display group data in EditText
                            inputUsername.setText(user.getString(TAG_USERNAME));
                            if (user.getString(TAG_BAR).equals("True")){
                                inputBar.setChecked(true);
                            }
                            if (user.getString(TAG_CAFE).equals("True")){
                                inputCafe.setChecked(true);
                            }
                            if (user.getString(TAG_AMUSEMENT).equals("True")){
                                inputAmusement.setChecked(true);
                            }
                            if (user.getString(TAG_BOOKS).equals("True")){
                                inputBooks.setChecked(true);
                            }
                            if (user.getString(TAG_ART).equals("True")){
                                inputArt.setChecked(true);
                            }
                            if (user.getString(TAG_RESTAURANT).equals("True")){
                                inputRestaurant.setChecked(true);
                            }
                            if (user.getString(TAG_CINEMA).equals("True")){
                                inputCinema.setChecked(true);
                            }
                            if (user.getString(TAG_BOWLING).equals("True")){
                                inputBowling.setChecked(true);
                            }
                            if (user.getString(TAG_CLOTHING).equals("True")){
                                inputClothing.setChecked(true);
                            }
                            if (user.getString(TAG_CLUB).equals("True")){
                                inputClub.setChecked(true);
                            }
                            if (user.getString(TAG_DEPARTMENT).equals("True")){
                                inputDepartment.setChecked(true);
                            }
                            if (user.getString(TAG_ELECTRONICS).equals("True")){
                                inputElectronics.setChecked(true);
                            }
                            if (user.getString(TAG_JEWELLERY).equals("True")){
                                inputJewellery.setChecked(true);
                            }
                            if (user.getString(TAG_MALL).equals("True")){
                                inputMall.setChecked(true);
                            }
                            if (user.getString(TAG_MUSEUM).equals("True")){
                                inputMuseum.setChecked(true);
                            }
                            if (user.getString(TAG_PARK).equals("True")){
                                inputPark.setChecked(true);
                            }
                            if (user.getString(TAG_SHOES).equals("True")){
                                inputShoes.setChecked(true);
                            }


                            if (user.getString(TAG_PRICE) != null) {
                                for (int i=0;i<inputPrice.getCount();i++){
                                    if (inputPrice.getItemAtPosition(i).equals(user.getString(TAG_PRICE))) {
                                        inputPrice.setSelection(i);
                                    }

                                }
                            }

                            if (user.getString(TAG_RATING) != null) {
                                for (int i=0;i<inputRating.getCount();i++){
                                    if (inputRating.getItemAtPosition(i).equals(user.getString(TAG_RATING))) {
                                        inputRating.setSelection(i);
                                    }

                                }
                            }

                        }else{
                            // group with gid not found
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once got all details
            pDialog.dismiss();
        }
    }

    /**
     * Background Async Task to  Save group Details
     * */
    class SaveUserDetails extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(OptionsActivity.this);
            pDialog.setMessage("Saving User ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Saving group
         * */
        protected String doInBackground(String... args) {

            String bar = "False";
            String cafe = "False";
            String shoes = "False";
            String restaurant = "False";
            String cinema = "False";
            String amusement = "False";
            String art = "False";
            String books = "False";
            String bowling = "False";
            String clothing = "False";
            String club = "False";
            String department = "False";
            String electronics = "False";
            String jewellery = "False";
            String mall = "False";
            String museum = "False";
            String park = "False";

            if(inputBar.isChecked()){
                bar = "True";
            }

            if(inputCafe.isChecked()) {
                cafe = "True";
            }
            if(inputShoes.isChecked()){
                shoes = "True";
            }
            if(inputRestaurant.isChecked()){
                restaurant = "True";
            }
            if(inputCinema.isChecked()){
                cinema = "True";
            }

            if(inputAmusement.isChecked()){
                amusement = "True";
            }
            if(inputArt.isChecked()){
                art = "True";
            }
            if(inputBooks.isChecked()){
                books = "True";
            }
            if(inputBowling.isChecked()){
                bowling = "True";
            }
            if(inputClothing.isChecked()){
                clothing = "True";
            }
            if(inputClub.isChecked()){
                club = "True";
            }
            if(inputDepartment.isChecked()){
                department = "True";
            }
            if(inputElectronics.isChecked()){
                electronics = "True";
            }
            if(inputJewellery.isChecked()){
                jewellery = "True";
            }
            if(inputMall.isChecked()){
                mall = "True";
            }
            if(inputMuseum.isChecked()){
                museum = "True";
            }
            if(inputPark.isChecked()){
                park = "True";
            }



            String username = inputUsername.getText().toString();
            String price = inputPrice.getSelectedItem().toString();
            String rating = inputRating.getSelectedItem().toString();

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("User_ID", User_ID));
            params.add(new BasicNameValuePair("Username", username));
            params.add(new BasicNameValuePair("Bar", bar));
            params.add(new BasicNameValuePair("Cafe", cafe));
            params.add(new BasicNameValuePair("Shoe_Store", shoes));
            params.add(new BasicNameValuePair("Restaurant", restaurant));
            params.add(new BasicNameValuePair("Movie_Theater", cinema));
            params.add(new BasicNameValuePair("Amusement_Park", amusement));
            params.add(new BasicNameValuePair("Art_Gallery", art));
            params.add(new BasicNameValuePair("Book_Store", books));
            params.add(new BasicNameValuePair("Bowling_Alley", bowling));
            params.add(new BasicNameValuePair("Clothing_Store", clothing));
            params.add(new BasicNameValuePair("Night_Club", club));
            params.add(new BasicNameValuePair("Department_Store", department));
            params.add(new BasicNameValuePair("Electronics_Store", electronics));
            params.add(new BasicNameValuePair("Jewellery_Store", jewellery));
            params.add(new BasicNameValuePair("Shopping_Mall", mall));
            params.add(new BasicNameValuePair("Museum", museum));
            params.add(new BasicNameValuePair("Park", park));
            params.add(new BasicNameValuePair("Price", price));
            params.add(new BasicNameValuePair("Rating", rating));

            // getting JSON Object
            // Note that create user url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_update_user,
                    "POST", params);

            // check log cat for response
            Log.d("Create Response", json.toString());

            // check for success tag
            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // successfully created user

                    // closing this screen

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
            // dismiss the dialog once group updated
            pDialog.dismiss();

            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void signoutbtnclicked(View v){
        Intent i = new Intent(OptionsActivity.this, MainActivity.class);
        startActivity(i);
    }
}
