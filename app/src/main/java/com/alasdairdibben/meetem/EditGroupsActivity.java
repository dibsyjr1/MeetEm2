package com.alasdairdibben.meetem;

import java.util.ArrayList;
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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditGroupsActivity extends Activity {

    EditText txtGroupName;
    EditText txtDesc;
    Button btnSave;
    Button btnDelete;

    String gid;

    // Progress Dialog
    private ProgressDialog pDialog;

    // JSON parser class
    JSONParser jsonParser = new JSONParser();

    // single group url
    private static final String url_group_details = "http://meetem.x10host.com/get_group_details.php";

    // url to update group
    private static final String url_update_group = "http://meetem.x10host.com/edit_group.php";

    // url to delete group
    private static final String url_delete_group = "http://meetem.x10host.com/delete_group.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_GROUP = "group";
    private static final String TAG_GID = "gid";
    private static final String TAG_GROUP_NAME = "group_name";
    private static final String TAG_DESCRIPTION = "description";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editgroup);

        // save button
        btnSave = (Button) findViewById(R.id.btnSave);
        btnDelete = (Button) findViewById(R.id.btnDelete);

        // getting group details from intent
        Intent i = getIntent();

        // getting group id (gid) from intent
        gid = i.getStringExtra(TAG_GID);

        // Getting complete group details in background thread
        new GetGroupDetails().execute();

        // save button click event
        btnSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // starting background task to update group
                new SaveGroupDetails().execute();
            }
        });

        // Delete button click event
        btnDelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // deleting group in background thread
                new DeleteGroup().execute();
            }
        });

    }

    /**
     * Background Async Task to Get complete group details
     * */
    class GetGroupDetails extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EditGroupsActivity.this);
            pDialog.setMessage("Loading group details. Please wait...");
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
                        params.add(new BasicNameValuePair("gid", gid));

                        // getting group details by making HTTP request
                        // Note that group details url will use GET request
                        JSONObject json = jsonParser.makeHttpRequest(
                                url_group_details, "GET", params);

                        // check your log for json response
                        Log.d("Single Group Details", json.toString());

                        // json success tag
                        success = json.getInt(TAG_SUCCESS);
                        if (success == 1) {
                            // successfully received group details
                            JSONArray groupObj = json
                                    .getJSONArray(TAG_GROUP); // JSON Array

                            // get first group object from JSON Array
                            JSONObject group = groupObj.getJSONObject(0);

                            // product with this gid found
                            txtGroupName = (EditText) findViewById(R.id.Input_Group_Name);
                            txtDesc = (EditText) findViewById(R.id.Input_Description);

                            // display group data in EditText
                            txtGroupName.setText(group.getString(TAG_GROUP_NAME));
                            txtDesc.setText(group.getString(TAG_DESCRIPTION));

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
    class SaveGroupDetails extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EditGroupsActivity.this);
            pDialog.setMessage("Saving group ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Saving group
         * */
        protected String doInBackground(String... args) {

            // getting updated data from EditTexts
            String group_name = txtGroupName.getText().toString();
            String description = txtDesc.getText().toString();

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(TAG_GID, gid));
            params.add(new BasicNameValuePair(TAG_GROUP_NAME, group_name));
            params.add(new BasicNameValuePair(TAG_DESCRIPTION, description));

            // sending modified data through http request
            // Notice that update group url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_update_group,
                    "POST", params);

            // check json success tag
            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // successfully updated
                    Intent i = getIntent();
                    // send result code 100 to notify about group update
                    setResult(100, i);
                    finish();
                } else {
                    // failed to update group
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
        }
    }

    /*****************************************************************
     * Background Async Task to Delete Group
     * */
    class DeleteGroup extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EditGroupsActivity.this);
            pDialog.setMessage("Deleting Group...");
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
                params.add(new BasicNameValuePair("gid", gid));

                // getting group details by making HTTP request
                JSONObject json = jsonParser.makeHttpRequest(
                        url_delete_group, "POST", params);

                // check your log for json response
                Log.d("Delete Product", json.toString());

                // json success tag
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    // group successfully deleted
                    // notify previous activity by sending code 100
                    Intent i = getIntent();
                    // send result code 100 to notify about group deletion
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
            // dismiss the dialog once group deleted
            pDialog.dismiss();

        }

    }
}
