package com.alasdairdibben.meetem;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alasdairdibben.meetem.JSONParser;
import com.alasdairdibben.meetem.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;



public class EditFriendsActivity extends Activity {

    TextView txtFirst_Name;
    TextView txtSurname;
    TextView txtHome_Town;
    Button btnDelete;

    String uid;

    // Progress Dialog
    private ProgressDialog pDialog;

    // JSON parser class
    JSONParser jsonParser = new JSONParser();

    // single friend url
    private static final String url_friend_details = "http://meetem.x10host.com/get_friend_details.php";


    // url to delete friend
    private static final String url_delete_friend = "http://meetem.x10host.com/delete_friend.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_FRIEND = "friend";
    private static final String TAG_UID = "uid";
    private static final String TAG_FIRST_NAME = "first_name";
    private static final String TAG_SURNAME = "surname";
    private static final String TAG_HOME_TOWN = "home_town";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editfriend);

        // delete button
        btnDelete = (Button) findViewById(R.id.btnDelete);

        // getting friend details from intent
        Intent i = getIntent();

        // getting friend id (uid) from intent
        uid = i.getStringExtra(TAG_UID);

        // Getting complete friend details in background thread
        new GetFriendDetails().execute();

        // Delete button click event
        btnDelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // deleting group in background thread
                new DeleteFriend().execute();
            }
        });

    }

    /**
     * Background Async Task to Get complete friend details
     * */
    class GetFriendDetails extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EditFriendsActivity.this);
            pDialog.setMessage("Loading friend details. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Getting friend details in background thread
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
                        params.add(new BasicNameValuePair("uid", uid));

                        // getting friend details by making HTTP request
                        // Note that friend details url will use GET request
                        JSONObject json = jsonParser.makeHttpRequest(
                                url_friend_details, "GET", params);

                        // check your log for json response
                        Log.d("Single Friend Details", json.toString());

                        // json success tag
                        success = json.getInt(TAG_SUCCESS);
                        if (success == 1) {
                            // successfully received friend details
                            JSONArray groupObj = json
                                    .getJSONArray(TAG_FRIEND); // JSON Array

                            // get first friend object from JSON Array
                            JSONObject group = groupObj.getJSONObject(0);

                            // product with this uid found
                            txtFirst_Name = (TextView) findViewById(R.id.Input_First_Name);
                            txtSurname = (TextView) findViewById(R.id.Input_Surname);
                            txtHome_Town = (TextView) findViewById(R.id.Input_Home_Town);

                            // display friend data in EditText
                            txtFirst_Name.setText(group.getString(TAG_FIRST_NAME));
                            txtSurname.setText(group.getString(TAG_SURNAME));
                            txtHome_Town.setText(group.getString(TAG_HOME_TOWN));

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


    /*****************************************************************
     * Background Async Task to Delete Friend
     * */
    class DeleteFriend extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EditFriendsActivity.this);
            pDialog.setMessage("Deleting Friend...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Deleting group
         * */
        protected String doInBackground(String... args) {

            // Check for success tag
            int success;
            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("uid", uid));

                // getting friend details by making HTTP request
                JSONObject json = jsonParser.makeHttpRequest(
                        url_delete_friend, "POST", params);

                // check your log for json response
                Log.d("Delete Friend", json.toString());

                // json success tag
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    // friend successfully deleted
                    // notify previous activity by sending code 100
                    Intent i = getIntent();
                    // send result code 100 to notify about friend deletion
                    setResult(100, i);
                    finish();
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
            // dismiss the dialog once friend deleted
            pDialog.dismiss();

        }

    }
}
