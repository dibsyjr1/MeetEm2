package com.alasdairdibben.meetem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class FriendsActivity extends ListActivity{

    // Progress Dialog
    private ProgressDialog pDialog;

    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();

    ArrayList<HashMap<String, String>> friendsList;

    // url to get all friends list
    private static String url_all_friends = "http://meetem.x10host.com/get_friends_list.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_FRIENDS = "friends";
    private static final String TAG_UID = "uid";
    private static final String TAG_FIRST_NAME = "first_name";
    /*private static final String TAG_SURNAME = "surname";*/

    // friends JSONArray
    JSONArray friends = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        // Hashmap for ListView
        friendsList = new ArrayList<HashMap<String, String>>();

        // Loading friends in Background Thread
        new LoadAllFriends().execute();

        // Get listview
        ListView lv = getListView();

        // on seleting single friend
        // launching Edit Friend Screen
        lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // getting values from selected Friend
                String uid = ((TextView) view.findViewById(R.id.uid)).getText()
                        .toString();

                // Starting new intent
                Intent in = new Intent(getApplicationContext(),
                        EditFriendsActivity.class);
                // sending uid to next activity
                in.putExtra(TAG_UID, uid);

                // starting new activity and expecting some response back
                startActivityForResult(in, 100);
            }
        });

    }

    // Response from Edit Friend Activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // if result code 100
        if (resultCode == 100) {
            // if result code 100 is received
            // means user edited/deleted friend
            // reload this screen again
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }

    }

    /**
     * Background Async Task to Load all friends by making HTTP Request
     * */
    class LoadAllFriends extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(FriendsActivity.this);
            pDialog.setMessage("Loading friends. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting All friends from url
         * */
        protected String doInBackground(String... args) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(url_all_friends, "GET", params);

            // Check your log cat for JSON response
            Log.d("Friends: ", json.toString());

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // friends found
                    // Getting Array of Friends
                    friends = json.getJSONArray(TAG_FRIENDS);

                    // looping through All Friends
                    for (int i = 0; i < friends.length(); i++) {
                        JSONObject c = friends.getJSONObject(i);

                        // Storing each json item in variable
                        String id = c.getString(TAG_UID);
                        String first_name = c.getString(TAG_FIRST_NAME);
                        /*String surname = c.getString(TAG_SURNAME);*/

                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        map.put(TAG_UID, id);
                        map.put(TAG_FIRST_NAME, first_name);
                        /*map.put(TAG_SURNAME, surname);*/

                        // adding HashList to ArrayList
                        friendsList.add(map);
                    }
                } else {
                    // no friends found
                    // Launch Add New product Activity
                    Intent i = new Intent(getApplicationContext(),
                            AddFriendsActivity.class);
                    // Closing all previous activities
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
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
            // dismiss the dialog after getting all friends
            pDialog.dismiss();
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */
                    ListAdapter adapter = new SimpleAdapter(
                            FriendsActivity.this, friendsList,
                            R.layout.list_friend, new String[] { TAG_UID,
                            TAG_FIRST_NAME, /*TAG_SURNAME*/},
                            new int[] { R.id.uid, R.id.first_name, /*R.id.surname*/ });
                    // updating listview
                    setListAdapter(adapter);
                }
            });

        }

    }
}

