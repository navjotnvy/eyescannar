package com.navjot.faceproject.alarmforhealth;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.text.DecimalFormat;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    DBAdapter myDb;
    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;
    LatLng latLng;
    String mapdistance;
    String fname, lname, weight, height1, walkprofile, pushupsprofile;
    int id;
    Cursor cursor;
    long wakeup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        ConnectivityManager cn=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nf=cn.getActiveNetworkInfo();
        if(nf != null && nf.isConnected() )
        {
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                checkLocationPermission();
            }
        }
        else
        {
            Toast.makeText(this, "Network Not Available", Toast.LENGTH_LONG).show();
            finish();
        }


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        openDB();

        cursor = myDb.getAllRows();
        if(cursor.getCount() == 0){

        }else {
            id = cursor.getInt(DBAdapter.COL_ROWID);
            fname = cursor.getString(DBAdapter.COL_FNAME);
            lname = cursor.getString(DBAdapter.COL_LNAME);
            wakeup = cursor.getInt(DBAdapter.COL_WAKEUP);
            weight = cursor.getString(DBAdapter.COL_WEIGHT);
            height1 = cursor.getString(DBAdapter.COL_HEIGHT);
            walkprofile = cursor.getString(DBAdapter.COL_WALK);
            pushupsprofile = cursor.getString(DBAdapter.COL_PUSHUPS);
            String message = "id=" + id
                    + ", fname=" + fname
                    + ", lname=" + lname
                    + ", #=" + wakeup
                    + ", weight=" + weight
                    + ", height" + height1
                    + ", walk" + walkprofile
                    + ", pushupsprofile" + pushupsprofile
                    + "\n";
            //Toast.makeText(this, message+"", Toast.LENGTH_SHORT).show();
        }

        DecimalFormat format = new DecimalFormat();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    private void openDB() {
        myDb = new DBAdapter(this);
        myDb.open();
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            //Toast.makeText(this, mLastLocation.getLongitude()+" OnConnected"+mLastLocation.getLatitude(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
        //Place current location marker
        latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = mMap.addMarker(markerOptions.position(latLng));
        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(19));
        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }



    }

/*
    public double getDistance()
    {
        double latitude1 = latLng.latitude;
        double lngitude1 = latLng.longitude;

        double latitudeA = Math.toRadians(latitude1);
        double lontitudeA = Math.toRadians(lngitude1);

        double lngtidue2 = mLastLocation.getLongitude();
        double latitude2 = mLastLocation.getLatitude();

        double latitudeB = Math.toRadians(latitude2);
        double longitudeB = Math.toRadians(lngtidue2);
        (Math.PI/180);
        double cosAngular = (Math.cos(latitudeA) * Math.cos(latitudeB) * Math.cos(longitudeB-lontitudeA)) + (Math.sin(latitudeA) * Math.sin(latitudeB));
        double angular = Math.acos(cosAngular);
        double distance = angular * 6371;
        return distance;
    }
*/
public double getDistance()
     {
         double latitude1 = latLng.latitude;
         double lngitude1 = latLng.longitude;

         double lngtidue2 = mLastLocation.getLongitude();
         double latitude2 = mLastLocation.getLatitude();

         double totalLat = degtorad(latitude2-latitude1);  // deg2rad below
         double totalLon = degtorad(lngtidue2-lngitude1);
         double a = Math.sin(totalLat/2) * Math.sin(totalLat/2) + Math.cos(degtorad(latitude1)) * Math.cos(degtorad(latitude2)) * Math.sin(totalLon/2) * Math.sin(totalLon/2);
         double count = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
         double fin = 6371 * count; // Distance in km
         return fin;
    }

    public double degtorad(double degr) {
        return degr * (Math.PI/180);
    }


    @Override
    public void onPause() {
        super.onPause();

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            // ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            // public void onRequestPermissionsResult(int requestCode, String[] permissions,
            // int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
           // Toast.makeText(this, mLastLocation.getLongitude()+" OnPause"+mLastLocation.getLatitude(), Toast.LENGTH_SHORT).show();
        }
        //stop location updates when Activity is no longer active
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }

try {
    mapdistance = String.valueOf(getDistance());
}
catch(RuntimeException ex){
    ex.printStackTrace();
}
        //Toast.makeText(this, mapdistance, Toast.LENGTH_SHORT).show();
        //Toast.makeText(this, getDistance()+"", Toast.LENGTH_SHORT).show();
try {
    Intent walkintent = new Intent(MapsActivity.this, Profile.class);
    walkintent.putExtra("walking", getDistance());
    startActivity(walkintent);
}
catch(RuntimeException ex){
    ex.printStackTrace();
}
        try {
                if(cursor.getCount() == 0){
                    long newId = myDb.insertRow("", "", 0, "", "",mapdistance,"");
                    cursor = myDb.getRow(newId);
                }
                else
                {
                    long id = cursor.getLong(DBAdapter.COL_ROWID);
                    try {
                        myDb.updateRow(id, fname, lname, wakeup, weight, height1, mapdistance, "");
                    }
                    catch(RuntimeException ex){
                        ex.printStackTrace();
                    }
                }

            cursor.close();
        }

        catch (NullPointerException ex)
        {
            Log.d("TAG_walk_NULL_POINTER",""+ex);
        }
        finish();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            android.Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {
                    // Permission denied, Disable the functionality that depends on this permission.
                    //Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                    ActivityCompat.requestPermissions(this,
                            new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                            MY_PERMISSIONS_REQUEST_LOCATION);
                }
                return;
            }
            // other 'case' lines to check for other permissions this app might request.
            // You can add here other case statements according to your requirement.
        }
    }
}
