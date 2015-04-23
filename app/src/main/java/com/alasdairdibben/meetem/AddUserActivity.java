package com.alasdairdibben.meetem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

public class AddUserActivity extends Activity {

    // Progress Dialog
    private ProgressDialog pDialog;

    ArrayList<HashMap<String, String>> userList;

    String User_ID;

    // JSON Node names
    private static final String TAG_UID = "User_ID";
    private static final String TAG_USER = "user";

    JSONParser jsonParser = new JSONParser();
    EditText inputUsername;
    EditText inputPassword;
    EditText inputFirstName;
    EditText inputSurname;
    EditText inputHomeTown;
    CheckBox inputShoes;
    CheckBox inputCinema;
    CheckBox inputAmusement;
    CheckBox inputArt;
    CheckBox inputBooks;
    CheckBox inputBowling;
    CheckBox inputClothing;
    CheckBox inputDepartment;
    CheckBox inputElectronics;
    CheckBox inputJewellery;
    CheckBox inputMall;
    CheckBox inputMuseum;
    CheckBox inputClub;
    CheckBox inputPark;
    CheckBox inputBar;
    CheckBox inputCafe;
    CheckBox inputRestaurant;
    Spinner inputPrice;
    Spinner inputRating;

    // url to create new user
    private static String url_create_user = "http://meetem.x10host.com/create_user.php";

    private static String url_get_user_id = "http://meetem.x10host.com/get_user_id.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";

    private LocationManager locMan;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        userList = new ArrayList<HashMap<String, String>>();


        inputRating = (Spinner) findViewById(R.id.Input_Rating);

        ArrayAdapter rating_adapter = ArrayAdapter.createFromResource(this, R.array.rating_array, android.R.layout.simple_spinner_item);
// Set the layout to use for each dropdown item
        rating_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        inputRating.setAdapter(rating_adapter);

        inputPrice = (Spinner) findViewById(R.id.Input_Price);

        ArrayAdapter price_adapter = ArrayAdapter.createFromResource(this, R.array.price_array, android.R.layout.simple_spinner_item);
// Set the layout to use for each dropdown item
        price_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        inputPrice.setAdapter(price_adapter);

        inputUsername = (EditText) findViewById(R.id.Input_Username);
        inputPassword = (EditText) findViewById(R.id.Input_Password);
        inputFirstName = (EditText) findViewById(R.id.Input_First_Name);
        inputSurname = (EditText) findViewById(R.id.Input_Surname);
        inputHomeTown = (EditText) findViewById(R.id.Input_Home_Town);
        inputBar = (CheckBox) findViewById(R.id.barcheck);
        inputCafe = (CheckBox) findViewById(R.id.cafecheck);
        inputShoes = (CheckBox) findViewById(R.id.shoescheck);
        inputRestaurant = (CheckBox) findViewById(R.id.restaurantcheck);
        inputCinema = (CheckBox) findViewById(R.id.cinemacheck);
        inputAmusement = (CheckBox) findViewById(R.id.amusementcheck);
        inputArt = (CheckBox) findViewById(R.id.artcheck);
        inputBooks = (CheckBox) findViewById(R.id.bookscheck);
        inputBowling = (CheckBox) findViewById(R.id.bowlingcheck);
        inputClothing = (CheckBox) findViewById(R.id.clothingcheck);
        inputDepartment = (CheckBox) findViewById(R.id.departmentcheck);
        inputElectronics = (CheckBox) findViewById(R.id.electronicscheck);
        inputJewellery = (CheckBox) findViewById(R.id.jewellerycheck);
        inputMall = (CheckBox) findViewById(R.id.mallcheck);
        inputMuseum = (CheckBox) findViewById(R.id.museumcheck);
        inputClub = (CheckBox) findViewById(R.id.clubcheck);
        inputPark = (CheckBox) findViewById(R.id.parkcheck);

        ImageButton sign_up = (ImageButton) findViewById(R.id.signupbutton);

        sign_up.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // starting background task to update group
                String username = inputUsername.getText().toString();
                String password = inputPassword.getText().toString();
                String first_name = inputFirstName.getText().toString();
                String surname = inputSurname.getText().toString();
                String home_town = inputHomeTown.getText().toString();

                GetUserID();
                if (first_name != null && !first_name.isEmpty()){
                    if (surname != null && !surname.isEmpty()) {
                        if (home_town != null && !home_town.isEmpty()) {
                            if (username != null && !username.isEmpty()) {
                                if (password != null && !password.isEmpty()) {
                                    new CreateNewUser().execute();
                                    GetUserID();
                                    finish();

                                    Intent i = new Intent(AddUserActivity.this,
                                            MenuActivity.class);
                                    // sending gid to next activity
                                    i.putExtra(TAG_UID, User_ID);
                                    startActivity(i);
                                }
                                else {
                                    inputPassword.setError("Password is Empty");
                                }
                            }
                            else {
                                inputUsername.setError("Username is Empty");
                            }
                        }
                        else {
                            inputHomeTown.setError("Home Town is Empty");
                        }
                    }
                    else {
                        inputSurname.setError("Surname is Empty");
                    }
                }
                else{
                    inputFirstName.setError("First Name is Empty");
                }


            }
        });



    }

    /**
     * Background Async Task to Create new user
     * */
    class CreateNewUser extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
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
        protected String doInBackground(String... args) {
            locMan = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

            Location lastLoc = locMan.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            double inputCurrLat = lastLoc.getLatitude();
            double inputCurrLong = lastLoc.getLongitude();
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
            String password = inputPassword.getText().toString();
            String first_name = inputFirstName.getText().toString();
            String surname = inputSurname.getText().toString();
            String home_town = inputHomeTown.getText().toString();
            String price = inputPrice.getSelectedItem().toString();
            String rating = inputRating.getSelectedItem().toString();
            String currlat = String.valueOf(inputCurrLat);
            String currlong = String.valueOf(inputCurrLong);

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("Username", username));
            params.add(new BasicNameValuePair("Password", password));
            params.add(new BasicNameValuePair("First_Name", first_name));
            params.add(new BasicNameValuePair("Surname", surname));
            params.add(new BasicNameValuePair("Home_Town", home_town));
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
            params.add(new BasicNameValuePair("Curr_Lat", currlat));
            params.add(new BasicNameValuePair("Curr_Long", currlong));

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
            // dismiss the dialog once done
            pDialog.dismiss();
        }

    }

    protected String GetUserID(String... params) {

            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    // Check for success tag
                    int success;
                    try {

                        String username = inputUsername.getText().toString();
                        // Building Parameters
                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("Username", username));

                        // getting group details by making HTTP request
                        // Note that group details url will use GET request
                        JSONObject json = jsonParser.makeHttpRequest(
                                url_get_user_id, "GET", params);

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

                            User_ID = user.getString(TAG_UID);

                            // creating new HashMap
                            HashMap<String, String> map = new HashMap<String, String>();

                            // adding each child node to HashMap key => value
                            map.put(TAG_UID, User_ID);

                            // adding HashList to ArrayList
                            userList.add(map);

                        } else {
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            return null;
        }
}
