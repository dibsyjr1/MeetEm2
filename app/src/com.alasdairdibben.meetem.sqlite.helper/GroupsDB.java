package com.alasdairdibben.meetem;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class GroupsDB extends ActionBarActivity {

    int groupid;
    String description;

    public GroupsDB(){
    }

    public GroupsDB(String description){
        this.description = description;
    }

    public GroupsDB(int groupid, String description){
        this.groupid = groupid;
        this.description = description;
    }

    public void setGroupId(int groupid){
        this.groupid = groupid;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public void getGroupId(){
        return this.groupid;
    }

    public void getDescription(){
        return this.description;
    }

}
