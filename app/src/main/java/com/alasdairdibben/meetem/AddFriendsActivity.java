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
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class AddFriendsActivity extends Activity {

    // Progress Dialog
    private ProgressDialog pDialog;

    JSONParser jsonParser = new JSONParser();
    EditText inputUsername;

    private static String url_get_id = "http://meetem.x10host.com/get_id.php";

    // url to create new group
    private static String url_add_friend = "http://meetem.x10host.com/add_friend.php";

    String User_ID;
    String Friend_ID;

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_UID = "User_ID";
    private static final String TAG_FRIEND_ID = "Friend_ID";
    private static final String TAG_FRIEND = "friend";


    ArrayList<HashMap<String, String>> userList;

    JSONArray friend = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.add_friend);

        userList = new ArrayList<HashMap<String, String>>();

        // getting group details from intent
        Intent i = getIntent();

        // getting group id (gid) from intent
        User_ID = i.getStringExtra(TAG_UID);

        // Edit Text
        inputUsername = (EditText) findViewById(R.id.Input_Username);

        // Create button
        ImageButton btnAddFriend = (ImageButton) findViewById(R.id.btnAddFriend);

        // button click event
        btnAddFriend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // creating new group in background thread
                GetUserID();
                new AddNewFriend().execute();
            }
        });
    }



    protected HashMap<String, String> GetUserID(String... params) {

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
                            url_get_id, "GET", params);

                    // check your log for json response
                    Log.d("Single User Details", json.toString());

                    // json success tag
                    success = json.getInt(TAG_SUCCESS);
                    if (success == 1) {
                        // successfully received group details
                        JSONArray userObj = json
                                .getJSONArray(TAG_FRIEND); // JSON Array

                        // get first group object from JSON Array
                        JSONObject friend = userObj.getJSONObject(0);

                        Friend_ID = friend.getString(TAG_FRIEND_ID);

                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();

                        Log.v(Friend_ID, "FriendID retrieval");

                        // adding each child node to HashMap key => value
                        map.put(TAG_FRIEND_ID, Friend_ID);

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

    class AddNewFriend extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AddFriendsActivity.this);
            pDialog.setMessage("Creating Product..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Creating group
         */
        protected String doInBackground(String... args) {

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("User_ID", User_ID));
            params.add(new BasicNameValuePair("Friend_ID", Friend_ID));

            Log.v(User_ID, "User_ID");
            Log.v(Friend_ID, "Friend_ID");

            // getting JSON Object
            // Note that create group url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_add_friend,
                    "POST", params);

            // check log cat for response
            Log.d("Create Response", json.toString());

            // check for success tag
            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // successfully added Friend

                    /*Intent i = new Intent(getApplicationContext(), FriendsActivity.class);
                    startActivity(i);*/

                    // closing this screen

                } else {
                    // failed to add Friend
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();
            finish();
        }
    }
}