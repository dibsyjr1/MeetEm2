package com.alasdairdibben.meetem;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.LocationListener;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.os.AsyncTask;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.location.Location;
import android.location.LocationManager;
import android.content.Context;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;


public class MapsActivity extends FragmentActivity implements LocationListener {

    // Progress Dialog
    private ProgressDialog pDialog;

    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();

    ArrayList<HashMap<String, String>> preferencesList;

    // url to get all friends list
    private static String url_all_preferences = "http://meetem.x10host.com/get_preferences_list.php";

    String User_ID;
    String Group_ID;

    private int userIcon, friendIcon, barIcon, bookIcon, bowlingIcon, cafeIcon, cinemaIcon,
     clothesIcon, departmentIcon, electronicsIcon, jewelleryIcon, museumIcon, clubIcon, parkIcon, restaurantIcon,
     shoeIcon, mallIcon, amusementIcon, artIcon, otherIcon;
    private GoogleMap googlemap;
    private LocationManager locMan;
    private Marker userMarker;
    private Marker[] placeMarkers;
    private final int MAX_PLACES = 20;
    private MarkerOptions[] places;

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_FRIENDS = "data";
    private static final String TAG_UID = "User_ID";
    private static final String TAG_USERID = "Friend_User_ID";
    private static final String TAG_AMUSEMENT = "Amusement_Park";
    private static final String TAG_ART = "Art_Gallery";
    private static final String TAG_BAR = "Bar";
    private static final String TAG_BOOKS = "Books_Store";
    private static final String TAG_BOWLING = "Bowling_Alley";
    private static final String TAG_CAFE = "Cafe";
    private static final String TAG_CINEMA = "Movie_Theater";
    private static final String TAG_CLOTHING = "Clothing_Store";
    private static final String TAG_CLUB = "Night_Club";
    private static final String TAG_DEPARTMENT = "Department_Store";
    private static final String TAG_ELECTRONICS = "Electronics_Store";
    private static final String TAG_JEWELLERY = "Jewellery_Store";
    private static final String TAG_MALL = "Shopping_Mall";
    private static final String TAG_MUSEUM = "Museum";
    private static final String TAG_PARK = "Park";
    private static final String TAG_RESTAURANT = "Restaurant";
    private static final String TAG_SHOES = "Shoe_Store";

    JSONArray preferences = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // getting group details from intent
        Intent i = getIntent();

        // getting group id (gid) from intent
        User_ID = i.getStringExtra(TAG_UID);

       // Group_ID = i.getStringExtra(TAG_GID);

        Group_ID = "1";
        userIcon = R.drawable.usericon;
        friendIcon = R.drawable.friendicon;
        amusementIcon = R.drawable.amusementicon;
        otherIcon = R.drawable.red_point;
        artIcon = R.drawable.articon;
        barIcon = R.drawable.baricon;
        bookIcon = R.drawable.bookicon;
        bowlingIcon = R.drawable.bowlingicon;
        cafeIcon = R.drawable.cafeicon;
        cinemaIcon = R.drawable.cinemaicon;
        clothesIcon = R.drawable.clothingicon;
        departmentIcon = R.drawable.departmenticon;
        electronicsIcon = R.drawable.electronicsicon;
        jewelleryIcon = R.drawable.jewelleryicon;
        museumIcon = R.drawable.museumicon;
        clubIcon = R.drawable.clubicon;
        parkIcon = R.drawable.parkicon;
        restaurantIcon = R.drawable.restauranticon;
        shoeIcon = R.drawable.shoeicon;
        mallIcon = R.drawable.mallicon;

        if (googlemap==null) {
            googlemap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();


            if (googlemap != null) {
                googlemap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                placeMarkers = new Marker[MAX_PLACES];
                updatePlaces();
            }
        }
    }

    public void onLocationChanged(Location location){
        Log.v("MapsActivity", "location changed");
        updatePlaces();
    }

    public void onProviderDisabled(String provider){
        Log.v("MapsActivity", "provider disabled");
    }

    public void onProviderEnabled(String provider){
        Log.v("MapsActivity", "provider enabled");
    }

    public void onStatusChanged(String provider, int status, Bundle extras){
        Log.v("MapsActivity", "status changed");
    }

    private void MeetingPointCalculation(){
        updatePlacesArray();
        new LoadUserData().execute();
        Integer RatingValue;
        Create ArrayList PlaceRating;
        for (int i = 0; i <= preferencesList.length; i++){
            for (int j = 0; j <= placeMarkers.length; j++){
                for (int k = 0; group_member.length(); k++){
                    if ()
                }
            }

        }
    }

    public ArrayList<HashMap<String, String>> LoadUserData() {

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("Group_ID", Group_ID));
            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(url_all_preferences, "GET", params);

            // Check your log cat for JSON response
            Log.d("Preferences", json.toString());

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // friends found
                    // Getting Array of Friends
                    preferences = json.getJSONArray(TAG_FRIENDS);

                    // looping through All Friends
                    for (int i = 0; i < preferences.length(); i++) {
                        JSONObject preferenceobjects= preferences.getJSONObject(i);

                        // Storing each json item in variable
                        String uid = preferenceobjects.getString(TAG_USERID);
                        String amusement_park = preferenceobjects.getString(TAG_AMUSEMENT);
                        String art_gallery = preferenceobjects.getString(TAG_ART);
                        String bar = preferenceobjects.getString(TAG_BAR);
                        String book_store = preferenceobjects.getString(TAG_BOOKS);
                        String bowling_alley = preferenceobjects.getString(TAG_BOWLING);
                        String cafe = preferenceobjects.getString(TAG_CAFE);
                        String cinema = preferenceobjects.getString(TAG_CINEMA);
                        String clothing_store = preferenceobjects.getString(TAG_CLOTHING);
                        String night_club = preferenceobjects.getString(TAG_CLUB);
                        String department_store = preferenceobjects.getString(TAG_DEPARTMENT);
                        String electronics_store = preferenceobjects.getString(TAG_ELECTRONICS);
                        String jewellery_store = preferenceobjects.getString(TAG_JEWELLERY);
                        String shopping_mall = preferenceobjects.getString(TAG_MALL);
                        String museum = preferenceobjects.getString(TAG_MUSEUM);
                        String park = preferenceobjects.getString(TAG_PARK);
                        String restaurant = preferenceobjects.getString(TAG_RESTAURANT);
                        String shoe_store = preferenceobjects.getString(TAG_SHOES);

                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();

                        if (!uid.equals(User_ID)) {
                            // adding each child node to HashMap key => value
                            map.put(TAG_USERID, uid);
                            map.put(TAG_AMUSEMENT, amusement_park);
                            map.put(TAG_ART, art_gallery);
                            map.put(TAG_BAR, bar);
                            map.put(TAG_BOOKS, book_store);
                            map.put(TAG_BOWLING, bowling_alley);
                            map.put(TAG_CAFE, cafe);
                            map.put(TAG_CINEMA, cinema);
                            map.put(TAG_CLOTHING, clothing_store);
                            map.put(TAG_CLUB, night_club);
                            map.put(TAG_DEPARTMENT, department_store);
                            map.put(TAG_ELECTRONICS, electronics_store);
                            map.put(TAG_JEWELLERY, jewellery_store);
                            map.put(TAG_MALL, shopping_mall);
                            map.put(TAG_MUSEUM, museum);
                            map.put(TAG_PARK, park);
                            map.put(TAG_RESTAURANT, restaurant);
                            map.put(TAG_SHOES, shoe_store);

                            // adding HashList to ArrayList
                            preferencesList.add(map);
                        }
                    }
                } else {

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return preferencesList;

    }

    private void updatePlacesArray(){
        locMan = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        Location lastLoc = locMan.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        double lat = lastLoc.getLatitude();
        double lng = lastLoc.getLongitude();

        LatLng mylocation = new LatLng(lat, lng);




        String types = "bar|restaurant|clothing_store|amusement_park|art_gallery|book_store|bowling_alley" +
                "|cafe|department_store|electronics_store|jewellery_store|museum|movie_theater|night_club|park|" +
                "shoe_store|shopping_mall";
        try {
            types = URLEncoder.encode(types, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        String placesSearchStr = "https://maps.googleapis.com/maps/api/place/nearbysearch/" +
                "json?location="+lat+","+lng+
                "&radius=1000&sensor=true"+
                "&types="+types+
                "&key=AIzaSyAvS55FhEZzva-RbVHPy17NZtqwAO0iB-A";

        GetPlacesArray();

        if(userMarker!=null) userMarker.remove();

        userMarker = googlemap.addMarker(new MarkerOptions()
                .position(mylocation)
                .title("You are here")
                .icon(BitmapDescriptorFactory.fromResource(userIcon))
                .snippet("Your last recorded location"));

        googlemap.animateCamera(CameraUpdateFactory.newLatLngZoom(mylocation,14));
    }

    private ArrayList<String> GetPlacesArray (String... placesURL){
            StringBuilder placesBuilder = new StringBuilder();
            //process search parameter string(s)
            for (String placeSearchURL : placesURL) {
                HttpClient placesClient = new DefaultHttpClient();
                try {
                    //try to fetch the data

                    //HTTP Get receives URL string
                    HttpGet placesGet = new HttpGet(placeSearchURL);
                    //execute GET with Client - return response
                    HttpResponse placesResponse = placesClient.execute(placesGet);

                    //check response status
                    StatusLine placeSearchStatus = placesResponse.getStatusLine();
                    //only carry on if response is OK
                    if (placeSearchStatus.getStatusCode() == 200) {
                        //get response entity
                        HttpEntity placesEntity = placesResponse.getEntity();
                        //get input stream setup
                        InputStream placesContent = placesEntity.getContent();
                        //create reader
                        InputStreamReader placesInput = new InputStreamReader(placesContent);
                        //use buffered reader to process
                        BufferedReader placesReader = new BufferedReader(placesInput);
                        //read a line at a time, append to string builder
                        String lineIn;
                        while ((lineIn = placesReader.readLine()) != null) {
                            placesBuilder.append(lineIn);
                        }
                    }
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }

            //parse place data returned from Google Places
            //remove existing markers
            if(placeMarkers!=null){
                for(int pm=0; pm<placeMarkers.length; pm++){
                    if(placeMarkers[pm]!=null)
                        placeMarkers[pm].remove();
                }
            }
            try {
                //parse JSON

                //create JSONObject, pass string returned from doInBackground
                JSONObject resultObject = new JSONObject(placesBuilder.toString());
                //get "results" array
                JSONArray placesArray = resultObject.getJSONArray("results");
                //marker options for each place returned
                places = new MarkerOptions[placesArray.length()];
                //loop through places
                for (int p=0; p<placesArray.length(); p++) {
                    //parse each place
                    //if any values are missing we won't show the marker
                    boolean missingValue=false;
                    LatLng placeLL=null;
                    String placeName="";
                    String vicinity="";
                    int currIcon = otherIcon;
                    try{
                        //attempt to retrieve place data values
                        missingValue=false;
                        int t=0;
                        //get place at this index
                        JSONObject placeObject = placesArray.getJSONObject(p);
                        //get location section
                        JSONObject loc = placeObject.getJSONObject("geometry")
                                .getJSONObject("location");
                        //read lat lng
                        placeLL = new LatLng(Double.valueOf(loc.getString("lat")),
                                Double.valueOf(loc.getString("lng")));
                        //get types
                        JSONArray types = placeObject.getJSONArray("types");
                        //loop through types
                        for(t=0; t<types.length(); t++){
                            //what type is it
                            String thisType=types.get(t).toString();
                            ArrayList<String> placesType = new ArrayList<String>();
                            placesType.add(thisType);
                        }
                        //vicinity
                        vicinity = placeObject.getString("vicinity");
                        //name
                        placeName = placeObject.getString("name");
                    }
                    catch(JSONException jse){
                        Log.v("PLACES", "missing value");
                        missingValue=true;
                        jse.printStackTrace();
                    }
                    //if values missing we don't display
                    if(missingValue)	places[p]=null;
                    else
                        places[p]=new MarkerOptions()
                                .position(placeLL)
                                .title(placeName)
                                .icon(BitmapDescriptorFactory.fromResource(currIcon))
                                .snippet(vicinity);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            if(places!=null && placeMarkers!=null){
                for(int p=0; p<places.length && p<placeMarkers.length; p++){
                    //will be null if a value was missing
                    if(places[p]!=null)
                        placeMarkers[p]=googlemap.addMarker(places[p]);
                }
            }


    }

    private void updatePlaces(){
        locMan = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        Location lastLoc = locMan.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        double lat = lastLoc.getLatitude();
        double lng = lastLoc.getLongitude();

        LatLng mylocation = new LatLng(lat, lng);




        String types = "bar|restaurant|clothing_store|amusement_park|art_gallery|book_store|bowling_alley" +
                "|cafe|department_store|electronics_store|jewellery_store|museum|movie_theater|night_club|park|" +
                "shoe_store|shopping_mall";
        try {
            types = URLEncoder.encode(types, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        String placesSearchStr = "https://maps.googleapis.com/maps/api/place/nearbysearch/" +
                "json?location="+lat+","+lng+
                "&radius=1000&sensor=true"+
                "&types="+types+
                "&key=AIzaSyAvS55FhEZzva-RbVHPy17NZtqwAO0iB-A";

        new GetPlaces().execute(placesSearchStr);

        if(userMarker!=null) userMarker.remove();

        userMarker = googlemap.addMarker(new MarkerOptions()
                .position(mylocation)
                .title("You are here")
                .icon(BitmapDescriptorFactory.fromResource(userIcon))
                .snippet("Your last recorded location"));

        googlemap.animateCamera(CameraUpdateFactory.newLatLngZoom(mylocation,14));
    }

        private class GetPlaces extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... placesURL) {
                //fetch places

                //build result as string
                StringBuilder placesBuilder = new StringBuilder();
                //process search parameter string(s)
                for (String placeSearchURL : placesURL) {
                    HttpClient placesClient = new DefaultHttpClient();
                    try {
                        //try to fetch the data

                        //HTTP Get receives URL string
                        HttpGet placesGet = new HttpGet(placeSearchURL);
                        //execute GET with Client - return response
                        HttpResponse placesResponse = placesClient.execute(placesGet);

                        //check response status
                        StatusLine placeSearchStatus = placesResponse.getStatusLine();
                        //only carry on if response is OK
                        if (placeSearchStatus.getStatusCode() == 200) {
                            //get response entity
                            HttpEntity placesEntity = placesResponse.getEntity();
                            //get input stream setup
                            InputStream placesContent = placesEntity.getContent();
                            //create reader
                            InputStreamReader placesInput = new InputStreamReader(placesContent);
                            //use buffered reader to process
                            BufferedReader placesReader = new BufferedReader(placesInput);
                            //read a line at a time, append to string builder
                            String lineIn;
                            while ((lineIn = placesReader.readLine()) != null) {
                                placesBuilder.append(lineIn);
                            }
                        }
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                }
                return placesBuilder.toString();
        }

            protected void onPostExecute(String result) {
                //parse place data returned from Google Places
                //remove existing markers
                if(placeMarkers!=null){
                    for(int pm=0; pm<placeMarkers.length; pm++){
                        if(placeMarkers[pm]!=null)
                            placeMarkers[pm].remove();
                    }
                }
                try {
                    //parse JSON

                    //create JSONObject, pass string returned from doInBackground
                    JSONObject resultObject = new JSONObject(result);
                    //get "results" array
                    JSONArray placesArray = resultObject.getJSONArray("results");
                    //marker options for each place returned
                    places = new MarkerOptions[placesArray.length()];
                    //loop through places
                    for (int p=0; p<placesArray.length(); p++) {
                        //parse each place
                        //if any values are missing we won't show the marker
                        boolean missingValue=false;
                        LatLng placeLL=null;
                        String placeName="";
                        String vicinity="";
                        int currIcon = otherIcon;
                        try{
                            //attempt to retrieve place data values
                            missingValue=false;
                            int t=0;
                            //get place at this index
                            JSONObject placeObject = placesArray.getJSONObject(p);
                            //get location section
                            JSONObject loc = placeObject.getJSONObject("geometry")
                                    .getJSONObject("location");
                            //read lat lng
                            placeLL = new LatLng(Double.valueOf(loc.getString("lat")),
                                    Double.valueOf(loc.getString("lng")));
                            //get types
                            JSONArray types = placeObject.getJSONArray("types");
                            //loop through types
                            for(t=0; t<types.length(); t++){
                                //what type is it
                                String thisType=types.get(t).toString();
                                //check for particular types - set icons
                                if(thisType.contains("clothing_store")){
                                    currIcon = clothesIcon;
                                    break;
                                }
                                else if(thisType.contains("bar")){
                                    currIcon = barIcon;
                                    break;
                                }
                                else if(thisType.contains("shoe_store")){
                                    currIcon = shoeIcon;
                                    break;
                                }
                                else if(thisType.contains("restaurant")){
                                    currIcon = restaurantIcon;
                                    break;
                                }
                                else if(thisType.contains("cafe")){
                                    currIcon = cafeIcon;
                                    break;
                                }
                                else if(thisType.contains("electronics_store")){
                                    currIcon = electronicsIcon;
                                    break;
                                }
                                else if(thisType.contains("book_store")){
                                    currIcon = bookIcon;
                                    break;
                                }

                                else if(thisType.contains("amusement_park")){
                                    currIcon = amusementIcon;
                                    break;
                                }
                                else if(thisType.contains("art_gallery")){
                                    currIcon = artIcon;
                                    break;
                                }
                                else if(thisType.contains("bowling_alley")){
                                    currIcon = bowlingIcon;
                                    break;
                                }
                                else if(thisType.contains("movie_theater")){
                                    currIcon = cinemaIcon;
                                    break;
                                }
                                else if(thisType.contains("department_store")){
                                    currIcon = departmentIcon;
                                    break;
                                }
                                else if(thisType.contains("electronics_store")){
                                    currIcon = electronicsIcon;
                                    break;
                                }
                                else if(thisType.contains("jewellery_store")){
                                    currIcon = jewelleryIcon;
                                    break;
                                }
                                else if(thisType.contains("museum")){
                                    currIcon = museumIcon;
                                    break;
                                }
                                else if(thisType.contains("night_club")){
                                    currIcon = clubIcon;
                                    break;
                                }
                                else if(thisType.contains("park")){
                                    currIcon = parkIcon;
                                    break;
                                }
                                else if(thisType.contains("shopping_mall")){
                                    currIcon = mallIcon;
                                    break;
                                }

                            }
                            //vicinity
                            vicinity = placeObject.getString("vicinity");
                            //name
                            placeName = placeObject.getString("name");
                        }
                        catch(JSONException jse){
                            Log.v("PLACES", "missing value");
                            missingValue=true;
                            jse.printStackTrace();
                        }
                        //if values missing we don't display
                        if(missingValue)	places[p]=null;
                        else
                            places[p]=new MarkerOptions()
                                    .position(placeLL)
                                    .title(placeName)
                                    .icon(BitmapDescriptorFactory.fromResource(currIcon))
                                    .snippet(vicinity);
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                if(places!=null && placeMarkers!=null){
                    for(int p=0; p<places.length && p<placeMarkers.length; p++){
                        //will be null if a value was missing
                        if(places[p]!=null)
                            placeMarkers[p]=googlemap.addMarker(places[p]);
                    }
                }

            }
        }

    public void gmpClicked(){

    }
}

