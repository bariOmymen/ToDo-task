
package com.example.todotasks.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.todotasks.GeofenceBroadcast;
import com.example.todotasks.adpter.ListAdapter;
import com.example.todotasks.R;
import com.example.todotasks.model.Task;
import com.example.todotasks.module.TaskList;
import com.example.todotasks.onClickListener;
import com.example.todotasks.viewmodel.TaskViewModel;
import com.example.todotasks.application.TodoTasksContext;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AddTasksActivity extends AppCompatActivity {

    private TextInputEditText detailsText;

    public static String titleString = "Title";
    public static String detailString = "Detail";
    public static String idString = "Task_id";
    private Task mainTask;
    private MaterialTextView addressText;


    private boolean mapsCheck = false;
    Bundle bundle;
    private GeofencingClient geofencingClient;
    private List<Geofence> geofenceList;
    private PendingIntent geofencePendingIntent;
    private Activity activity;
    private LocationManager locationManager;
    private Context tContext = TodoTasksContext.getAppContext();
    private Task task;
    private String updateLocation = "update";
    public static String updateLocationIntent = "Update_Location";
    private EditText titleText;
    private TextView setLocation;
    private Location mLocation;
    TaskViewModel model;





    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tasks);
        geofenceList= new ArrayList<>();


        model = TaskViewModel.getViewModelInstance(this);


        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_actionbar);
        View view = getSupportActionBar().getCustomView();


        titleText = view.findViewById(R.id.customEditText);
        setLocation=view.findViewById(R.id.setLocationText);

        ImageView backButton= view.findViewById(R.id.backButton);
        geofencingClient = LocationServices.getGeofencingClient(this);

        activity=(Activity) this;

        locationManager= (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        detailsText = findViewById(R.id.detailEdit);

        addressText = findViewById(R.id.address);

        if (savedInstanceState != null) {

            titleText.setText(savedInstanceState.getString(titleString, ""));
            detailsText.setText(savedInstanceState.getString(detailString, ""));

        }
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });












        Intent ListIntent = getIntent();


        if (ListIntent.getIntExtra(ListAdapter.number, 0) == 1) {
            Log.i("YES", "called from the adapter");
            showTask();

        } else if(ListIntent.getIntExtra(MapsActivity.afterUpdate, 0) == 6){
            showUpdatedTask();

        }

        setLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mapsCheck) {

                 addGeofence();

                    finish();
                } else if(setLocation.getText().toString().equals(updateLocation)){

                    Toast.makeText(getApplicationContext(), "from update location", Toast.LENGTH_SHORT).show();

                    startUpdateIntent();
                    finish();

                }else if(setLocation.getText().toString().equals("save")){
                    finish();
                }

                else {
                    requestLocationPermissions();



                    Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                    startActivityForResult(intent, 5);

                }
            }
        });
    }

    private void showTask(){


        mainTask =  model.getMainTask();
mainTask.getDetails();
        titleText.setText(mainTask.getTitle());
        detailsText.setText(mainTask.getDetails());
        addressText.setVisibility(View.VISIBLE);
        addressText.setText(mainTask.getTaskAddress());
        setLocation.setText(updateLocation);

    }
    private void showUpdatedTask(){

        mainTask = model.getMainTask();
        titleText.setText(mainTask.getTitle());
        detailsText.setText(mainTask.getDetails());
        addressText.setVisibility(View.VISIBLE);
        addressText.setText(mainTask.getTaskAddress());
        setLocation.setText("save");
    }




    public class GeoTask extends AsyncTask<LatLng, Void, String> {
        Context mContext;

        GeoTask(Context context) {
            this.mContext = context;
        }

        @Override
        protected String doInBackground(LatLng... latLngs) {
            LatLng latLng = latLngs[0];
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

            addressText.setText(s);
            task= new Task(titleText.getText().toString(), detailsText.getText().toString()
                    , bundle.getDouble(MapsActivity.latString), bundle.getDouble(MapsActivity.lonString),s);

            model.insertTask(task);

        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(titleString, titleText.getText().toString());
        outState.putString(detailString, detailsText.getText().toString());
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 5 && resultCode == RESULT_OK) {
            Log.i("from maps on result", String.valueOf(data.getBooleanExtra(MapsActivity.booleanString, false)));
            Log.i("from maps", String.valueOf(data.getDoubleExtra(MapsActivity.latString, 0.0)));
            if (data.getBooleanExtra(MapsActivity.booleanString, false)) {
                //add code here
                mapsCheck = data.getBooleanExtra(MapsActivity.booleanString, false);
                setLocation.setText(getResources().getString(R.string.saveTask));

                model = ViewModelProviders.of(this).get(TaskViewModel.class);
                bundle = data.getExtras();

                 geofenceList.add(new Geofence.Builder()

                        .setRequestId(titleText.getText().toString())
                        //.setCircularRegion(mLocation.getLatitude(),mLocation.getLongitude(),10)
                        //.setExpirationDuration(20000)

                        .setCircularRegion(mainTask.getLat(),
                                mainTask.getLon(),100)
                        .setExpirationDuration(Geofence.NEVER_EXPIRE)
                        .setLoiteringDelay(20)
                         .setNotificationResponsiveness(1000)
                        .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                                Geofence.GEOFENCE_TRANSITION_EXIT | Geofence.GEOFENCE_TRANSITION_DWELL).build());


            }
        }
    }

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(geofenceList);
        return builder.build();
    }

    private PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
        if (geofencePendingIntent != null) {

            return geofencePendingIntent;
        }
        Intent intent = new Intent(tContext, GeofenceBroadcast.class);

        intent.putExtra(titleString,titleText.getText().toString());
        intent.putExtra(detailString,detailsText.getText().toString());

        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when
        // calling addGeofences() and removeGeofences().
        geofencePendingIntent = PendingIntent.getBroadcast(tContext, 0, intent, PendingIntent.
                FLAG_UPDATE_CURRENT);
        return geofencePendingIntent;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && requestCode == 2) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                 locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
                     @Override
                     public void onLocationChanged(@NonNull Location location) {

                     }
                 });

            }

        }
    }
    private void setTaskFields() {
        mainTask.setTitle(titleText.getText().toString());
        mainTask.setDetails(detailsText.getText().toString());
        model.setMainTask(mainTask);
    }
    private void startUpdateIntent(){
        Intent updateIntent = new Intent(getApplicationContext(), MapsActivity.class);

        updateIntent.putExtra(updateLocationIntent,12);
        setTaskFields();
        startActivity(updateIntent);
    }
    private void requestLocationPermissions(){
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions

            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, 2);;
        }
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED){
            locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {


                }
            });
        }
    }
    private void addGeofence(){
        LatLng latLng = new LatLng(bundle.getDouble(MapsActivity.latString), bundle.getDouble(MapsActivity.lonString));

        new GeoTask(getApplicationContext()).execute(latLng);
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions

            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, 2);;
        }

        geofencingClient.addGeofences(getGeofencingRequest(), getGeofencePendingIntent()).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "did not work", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(), "did work", Toast.LENGTH_SHORT).show();
                Log.i("did work", "from success");
            }
        });

    }


}
