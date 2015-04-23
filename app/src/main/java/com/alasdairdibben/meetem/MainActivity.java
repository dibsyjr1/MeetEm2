package com.alasdairdibben.meetem;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends ActionBarActivity {


    private ProgressDialog pDialog;

    JSONParser jsonParser = new JSONParser();
    EditText inputUsername;
    EditText inputPassword;

    String User_ID;
    String Username;
    String Password;

    ImageButton btnSignIn;

    ArrayList<HashMap<String, String>> userList;

    // url to get all groups list
    private static String url_login = "http://meetem.x10host.com/get_login.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_USER = "user";
    private static final String TAG_UID = "User_ID";
    private static final String TAG_USERNAME = "Username";
    private static final String TAG_PASSWORD = "Password";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_main);

        btnSignIn = (ImageButton) findViewById(R.id.signin);

        // Hashmap for ListView
        userList = new ArrayList<HashMap<String, String>>();

        inputUsername = (EditText) findViewById(R.id.Input_Username);
        inputPassword = (EditText) findViewById(R.id.Input_Password);

        // save button click event
        btnSignIn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // starting background task to update group
                String username = inputUsername.getText().toString();
                String password = inputPassword.getText().toString();

                LoadUserDetails();
                if (username != null && !username.isEmpty()){
                    if (password != null && !password.isEmpty()) {
                        if (Username.equals(username)) {
                            if (Password.equals(password)) {
                                Intent i = new Intent(MainActivity.this,
                                        MenuActivity.class);
                                // sending gid to next activity
                                i.putExtra(TAG_UID, User_ID);
                                startActivity(i);
                            }
                            else {
                                inputPassword.setError("Password is Incorrect");
                            }
                        }
                        else {
                            inputUsername.setError("User Does Not Exist");
                        }
                    }
                    else {
                        inputPassword.setError("Password is Empty");
                    }
                }
                else{
                    inputUsername.setError("Username is Empty");
                }


            }
        });

    }

    /**
     * Background Async Task to Load all groups by making HTTP Request
     * */
        protected HashMap<String, String> LoadUserDetails(String... params) {

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
                                url_login, "GET", params);

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
                            Username = user.getString(TAG_USERNAME);
                            Password = user.getString(TAG_PASSWORD);

                            // creating new HashMap
                            HashMap<String, String> map = new HashMap<String, String>();

                            // adding each child node to HashMap key => value
                            map.put(TAG_UID, User_ID);
                            map.put(TAG_USERNAME, Username);
                            map.put(TAG_PASSWORD, Password);

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

        public void create_new_clicked(View v){
            Intent i = new Intent(MainActivity.this, AddUserActivity.class);
            startActivity(i);
        }


}
