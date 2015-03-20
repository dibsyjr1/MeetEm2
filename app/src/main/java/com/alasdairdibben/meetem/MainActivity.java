package com.alasdairdibben.meetem;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;


public class MainActivity extends ActionBarActivity {


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);
    }


    public void signinbtnclicked(View v){
        Intent i = new Intent(MainActivity.this, MenuActivity.class);
        startActivity(i);
    }
}
