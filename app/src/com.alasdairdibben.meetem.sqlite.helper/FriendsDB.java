package com.alasdairdibben.meetem;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class FriendsDB extends ActionBarActivity {

    int friendid;
    String firstname;
    String surname;
    String hometown;
    double latitude;
    double longitude;

    public FriendsDB(){
    }

    public FriendsDB(String firstname, String surname, String hometown, double latitude, double longitude){
        this.firstname = firstname;
        this.surname = surname;
        this.hometown = hometown;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public FriendsDB(int friendid, String firstname, String surname, String hometown, double latitude, double longitude){
        this.friendid = friendid;
        this.firstname = firstname;
        this.surname = surname;
        this.hometown = hometown;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void setFriendId(int friendid){
        this.friendid = friendid;
    }

    public void setFirstName(String firstname){
        this.firstname = firstname;
    }

    public void setSurname(String surname){
        this.surname = surname;
    }

    public void setHometown(String hometown){
        this.hometown = hometown;
    }

    public void setLatitude(double latitude){
        this.latitude = latitude;
    }

    public void setLongitude(double longitude){
        this.longitude = longitude;
    }

    public void getFriendId(){
        return this.friendid;
    }

    public void getFirstName(){
        return this.firstname;
    }

    public void getSurname(){
        return this.surname;
    }

    public void getHometown(){
        return this.hometown;
    }

    public void getLatitude(){
        return this.latitude;
    }

    public void getLongitude(){
        return this.longitude;
    }

}
