package com.alasdairdibben.meetem;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class GroupsActivity extends ListActivity {

    // Progress Dialog
    private ProgressDialog pDialog;

    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();

    ArrayList<HashMap<String, String>> groupsList;

    // url to get all groups list
    private static String url_all_groups = "http://meetem.x10host.com/get_groups_list_basic.php";

    String User_ID;

    private static final String TAG_UID = "User_ID";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_GROUPS = "groups";
    private static final String TAG_GID = "Group_ID";
    private static final String TAG_GROUP_NAME = "Group_Name";
    private static final String TAG_DESCRIPTION = "Description";

    // products JSONArray
    JSONArray groups = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);

        // getting group details from intent
        Intent i = getIntent();

        // getting group id (gid) from intent
        User_ID = i.getStringExtra(TAG_UID);

        // Hashmap for ListView
        groupsList = new ArrayList<HashMap<String, String>>();

        // Loading products in Background Thread
        new LoadAllGroups().execute();

        // Get listview
        ListView lv = getListView();

        // on selecting single group
        // launching Edit Group Screen
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // getting values from selected GroupItem
                String Group_ID = ((TextView) view.findViewById(R.id.Group_ID)).getText()
                        .toString();

                // Starting new intent
                Intent in = new Intent(getApplicationContext(),
                        EditGroupsActivity.class);
                // sending gid to next activity
                in.putExtra(TAG_GID, Group_ID);

                // starting new activity and expecting some response back
                startActivityForResult(in, 100);
            }
        });

    }

    // Response from Edit Group Activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // if result code 100
        if (resultCode == 100) {
            // if result code 100 is received
            // means user edited/deleted group
            // reload this screen again
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }

    }

    /**
     * Background Async Task to Load all groups by making HTTP Request
     * */
    class LoadAllGroups extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(GroupsActivity.this);
            pDialog.setMessage("Loading groups. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting All groups from url
         * */
        protected String doInBackground(String... args) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(url_all_groups, "GET", params);

            // Check your log cat for JSON response
            Log.d("Groups: ", json.toString());

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // products found
                    // Getting Array of Groups
                    groups = json.getJSONArray(TAG_GROUPS);

                    // looping through All Groups
                    for (int i = 0; i < groups.length(); i++) {
                        JSONObject groupobjects = groups.getJSONObject(i);

                        // Storing each json item in variable
                        String Group_ID = groupobjects.getString(TAG_GID);
                        String Group_Name = groupobjects.getString(TAG_GROUP_NAME);
                        String Description = groupobjects.getString(TAG_DESCRIPTION);

                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        map.put(TAG_GID, Group_ID);
                        map.put(TAG_GROUP_NAME, Group_Name);
                        map.put(TAG_DESCRIPTION, Description);

                        // adding HashList to ArrayList
                        groupsList.add(map);
                    }
                } else {
                    // no groups found
                    // Launch Add New group Activity
                    Intent i = new Intent(getApplicationContext(),
                            AddGroupsActivity.class);
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
            // dismiss the dialog after getting all groups
            pDialog.dismiss();
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */
                    ListAdapter adapter = new SimpleAdapter(
                            GroupsActivity.this, groupsList,
                            R.layout.list_group, new String[] { TAG_GID,
                            TAG_GROUP_NAME, TAG_DESCRIPTION},
                            new int[] { R.id.Group_ID, R.id.Group_Name, R.id.Description });
                    // updating listview
                    setListAdapter(adapter);
                }
            });

        }

    }

    public void create_group_btn_clicked(View v){
        Intent i = new Intent(GroupsActivity.this, AddGroupsActivity.class);
        startActivity(i);
    }
}
