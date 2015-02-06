package com.alasdairdibben.meetem;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.view.View.OnClickListener;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

 /* Created by User on 19/01/2015.
 */
public class MenuActivity extends ActionBarActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

     protected void onResume() {
         super.onResume();
     }

     Button button = (Button) findViewById(R.id.map);
     button.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             Intent i = new Intent(MenuActivity.this, MapsActivity.class);
             startActivity(i);
         }
     });


}
