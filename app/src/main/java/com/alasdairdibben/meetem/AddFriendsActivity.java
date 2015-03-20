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

public class AddFriendsActivity extends Activity {

    // Progress Dialog
    private ProgressDialog pDialog;

    JSONParser jsonParser = new JSONParser();
    EditText inputUsername;

    // url to create new group
    private static String url_add_friend = "http://http://meetem.x10host.com/add_friend.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_friend);

        // Edit Text
        inputUsername = (EditText) findViewById(R.id.Input_Username);

        // Create button
        Button btnCreateGroup = (Button) findViewById(R.id.btnAddFriend);

        // button click event
        btnCreateGroup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // creating new group in background thread
                new AddNewFriend().execute();
            }
        });
    }

    /**
     * Background Async Task to Create new group
     * */
    class AddNewFriend extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AddFriendsActivity.this);
            pDialog.setMessage("Adding Friend..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Creating group
         * */
        protected String doInBackground(String... args) {
            String username = inputUsername.getText().toString();

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("Username", username));

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
                    // successfully created group
                    Intent i = new Intent(getApplicationContext(), GroupsActivity.class);
                    startActivity(i);

                    // closing this screen
                    finish();
                } else {
                    // failed to create group
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
}