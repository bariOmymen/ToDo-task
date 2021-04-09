package com.example.todotasks.ui;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import androidx.lifecycle.ViewModelProviders;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.todotasks.R;
import com.example.todotasks.model.Task;
import com.example.todotasks.viewmodel.TaskViewModel;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.button.MaterialButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    public static final String latString = "latitude";
    public static final String lonString = "longitude";
    public static String booleanString = "boolean";
    public static String afterUpdate = "after_update";




    private GoogleMap mMap;
   private LocationManager locationManager;
    private LocationListener locationListener;
    private double lat;
    private double lon;
    private LatLng latLng = null;
    private Context mContext;
    private TaskViewModel taskViewModel;

    Marker marker = null;
    private Intent gotIntent;
    private Task mainTask;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mContext= getApplicationContext();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        taskViewModel= TaskViewModel.getViewModelInstance(this);
        mainTask = taskViewModel.getMainTask();


        MaterialButton saveButton = findViewById(R.id.saveButton);
        gotIntent = getIntent();
        Log.i("intent results",String.valueOf(gotIntent.getIntExtra(AddTasksActivity.updateLocationIntent,12)));
        if(gotIntent.getIntExtra(AddTasksActivity.updateLocationIntent,9)==12){
            saveButton.setText("Update");
        }
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                latLng=new LatLng(lat,lon);
Log.i("intent",String.valueOf(gotIntent.getIntExtra(AddTasksActivity.updateLocationIntent,12)));
                if(gotIntent.getIntExtra(AddTasksActivity.updateLocationIntent,9)==12) {
                    Toast.makeText(getBaseContext(),"update button was pressed",Toast.LENGTH_SHORT).show();
                    new GeoTask(getApplicationContext()).execute(latLng);




                }
                else if(latLng!=null){
                    Intent intent = new Intent();
                    Toast.makeText(getBaseContext(),"latlng button was pressed",Toast.LENGTH_SHORT).show();
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    intent.putExtra(latString,lat);
                    intent.putExtra(lonString,lon);

                    intent.putExtra(booleanString,true);
                    // startActivity(intent);
                    setResult(RESULT_OK,intent);

                    finish();
                }else {Toast.makeText(getBaseContext(),"Long press to set a Location",Toast.LENGTH_SHORT).show();}
                //Task task =new Task(title,details,lat,lon);
                //taskViewModel.insertTask(task);
            }
        });

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
    @Override
    public void onMapReady(GoogleMap googleMap)  {

        mMap = googleMap;


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);

        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {

                return false;
            }
        });


        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {


                if(marker==null) {
                    marker = mMap.addMarker(new MarkerOptions().position(latLng));
                }else {marker.setPosition(latLng);}

                Toast.makeText(getBaseContext(),"your Location",Toast.LENGTH_SHORT).show();
                lat=latLng.latitude;
                lon=latLng.longitude;


            }
        });


        // Add a marker in Sydney and move the camera
       // LatLng sydney = new LatLng(-34, 151);
        //mMap.addMarker(new MarkerOptions().position(latLng).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
                locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER,0,0,locationListener);
            }
        }
    }




    public class GeoTask extends AsyncTask<LatLng, Void, String>  {
        Context mContext;
        LatLng latLng;



        GeoTask(Context context) {
            this.mContext = context;
        }

        @Override
        protected String doInBackground(LatLng... latLngs) {
             latLng = latLngs[0];
            Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
            List<Address> addresses = null;
            String result = "";
            try {
                addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                Log.i("latlng", String.valueOf(latLng.latitude + "\n" + latLng.longitude));


            } catch (IOException ioException) {
                result = mContext.getString(R.string.service_not);
                Log.i("address not ", ioException.toString());
            }
            if (addresses == null) {
                result = mContext.getString(R.string.address_not_found);
            } else {
                Log.i("address", addresses.get(0).toString());
                Address address = addresses.get(0);
                ArrayList<String> addressParts = new ArrayList<>();
                for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                    addressParts.add(address.getAddressLine(i));
                }
                result = TextUtils.join("\n", addressParts);
            }

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            mainTask.setTaskAddress(s);
            mainTask.setLat(latLng.latitude);
            mainTask.setLon(latLng.longitude);



            taskViewModel.updateTask(mainTask);
            taskViewModel.setMainTask(mainTask);


            if(gotIntent.getIntExtra(AddTasksActivity.updateLocationIntent,9)==12) {
             intent = new Intent(getApplicationContext(),AddTasksActivity.class);
            intent.putExtra(afterUpdate,6);

                startActivity(intent);
                finish();

        }
        }


    }

}