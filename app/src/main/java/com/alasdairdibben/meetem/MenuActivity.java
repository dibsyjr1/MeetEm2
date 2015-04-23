package com.alasdairdibben.meetem;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.view.View;
import android.view.View.OnClickListener;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

 /* Created by User on 19/01/2015.
 */
public class MenuActivity extends ActionBarActivity {

    String User_ID;

    private static final String TAG_UID = "User_ID";


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_menu);



        // getting group details from intent
        Intent i = getIntent();

        // getting group id (gid) from intent
        User_ID = i.getStringExtra(TAG_UID);
        Log.v(User_ID, "Menu");
    }

     protected void onResume() {
         super.onResume();
     }

     public void map_btn_clicked(View v){
         Intent i = new Intent(MenuActivity.this,
                 MapsActivity.class);
         // sending gid to next activity
         i.putExtra(TAG_UID, User_ID);
         startActivity(i);
     }

     public void friend_btn_clicked(View v){
         Intent i = new Intent(MenuActivity.this,
                 FriendsActivity.class);
         // sending gid to next activity
         i.putExtra(TAG_UID, User_ID);
         startActivity(i);
     }

     public void group_btn_clicked(View v){
         Intent i = new Intent(MenuActivity.this,
                 GroupsActivity.class);
         // sending gid to next activity
         i.putExtra(TAG_UID, User_ID);
         startActivity(i);
     }

     public void options_btn_clicked(View v){
         Intent i = new Intent(MenuActivity.this,
                 OptionsActivity.class);
         // sending gid to next activity
         i.putExtra(TAG_UID, User_ID);
         startActivity(i);
     }


}
