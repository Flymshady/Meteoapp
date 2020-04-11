package cz.cellar.meteoapp;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MapActivity  extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static final String LOCATE_VALUES = "LocateValuesFile";
    public static final String TEMP_VALUES = "TempValuesFile";
    public static final String PRESS_VALUES = "PressValuesFile";
    public static final String DATESHARE_VALUES = "DateShareValuesFile";
    FirebaseAuth mFirebaseAuth;
    Button btnShare, btnLocate, btnBack;
    LocationManager locationManager;
    LocationListener locationListener;
    private double myLat, myLong;
    private boolean latlngexist, called;
    private String addressline;
    private float myTemp, myPress;
    private String username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);


        btnBack=findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnShare=findViewById(R.id.btnShare);
        btnLocate=findViewById(R.id.btnLocate);
        mFirebaseAuth = FirebaseAuth.getInstance();
        username = mFirebaseAuth.getCurrentUser().getEmail();

        SharedPreferences tempshare = getSharedPreferences(TEMP_VALUES, 0);
        SharedPreferences pressshare = getSharedPreferences(PRESS_VALUES, 0);
        myTemp = tempshare.getFloat("tempshare", myTemp);
        myPress = pressshare.getFloat("pressshare", myPress);

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(latlngexist && myTemp!=0f && myPress !=0f && username!=null) {
                    LatLng latLng = new LatLng(myLat, myLong);
                    Date date = new Date();
                    SharedPreferences dateshare = getSharedPreferences(DATESHARE_VALUES, 0);
                    SharedPreferences.Editor editor = dateshare.edit();
                    editor.putString("dateshare", date.toString());
                    editor.commit();
                    String id = latLng + username + date;
                    mMap.addMarker(new MarkerOptions().position(latLng).title(username).snippet(String.valueOf(date)));


                    Map<String, Object> marker = new HashMap<>();
                    marker.put("lat", latLng.latitude);
                    marker.put("lng", latLng.longitude);
                    marker.put("user", username);
                    marker.put("temp", String.valueOf(myTemp));
                    marker.put("press", String.valueOf(myPress));
                    marker.put("date", String.valueOf(date));
                    marker.put("id", id);
                    db.collection("markers").add(marker);
                    Toast.makeText(MapActivity.this, "Sdíleno", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MapActivity.this, latlngexist+"lat"+myLat+"pres"+myPress+"us"+username+"tmp"+myTemp+"lng"+myLong, Toast.LENGTH_LONG).show();
                }

            }
        });

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {
                myLat=location.getLatitude();
                myLong=location.getLongitude();
                latlngexist=true;
                if(called){
                    called=false;
                    LatLng me = new LatLng(myLat, myLong);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(me, 10.2f));
                    Geocoder geocoder = new Geocoder(MapActivity.this, Locale.getDefault());
                    try {
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(),1);
                        addressline = "Adresa: "+addresses.get(0).getAddressLine(0);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    SharedPreferences locateshare = getSharedPreferences(LOCATE_VALUES, 0);
                    SharedPreferences.Editor editor = locateshare.edit();
                    editor.putString("locateshare", addressline);
                    editor.commit();
                }

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);

            }
        };
        btnLocate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((ActivityCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED)&&
                        ActivityCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET
                    }, 10);
                    return;
                }
                locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);
                called=true;


            }
        });
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 10:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                   btnLocate.callOnClick();
                return;
        }
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapLongClickListener(this);
        mMap.setOnMarkerClickListener(this);

        db.collection("markers")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                LatLng latLng = new LatLng(document.getDouble("lat"), document.getDouble("lng"));
                                mMap.addMarker(new MarkerOptions().position(latLng).title(document.getString("user")).snippet(String.valueOf(document.get("date"))));

                        }
                    }
                });
        if(latlngexist) {
            LatLng me = new LatLng(myLat, myLong);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(me, 10.2f));
        }

    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        // Add a marker on click
        String username = "test-user";
        Date date = new Date();
        String id = latLng+username+date;
        float tempv = 123.1f;
        float pressv = 456.1f;
        mMap.addMarker(new MarkerOptions().position(latLng).title(username).snippet(String.valueOf(date)));


        Map<String, Object> marker = new HashMap<>();
        marker.put("lat", latLng.latitude);
        marker.put("lng", latLng.longitude);
        marker.put("user", username);
        marker.put("temp", String.valueOf(tempv));
        marker.put("press", String.valueOf(pressv));
        marker.put("date", String.valueOf(date));
        marker.put("id",id );

        db.collection("markers").add(marker);

    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        String mId=marker.getPosition()+marker.getTitle()+marker.getSnippet();

        db.collection("markers")
                .whereEqualTo("id", mId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String user = document.getString("user");
                                String temp = document.getString("temp");
                                String press = document.getString("press");
                                String date = (String) document.get("date");
                                Double lat = document.getDouble("lat");
                                Double lng = document.getDouble("lng");

                                Intent intent = new Intent(MapActivity.this, MarkerActivity.class);
                                intent.putExtra("user", user);
                                intent.putExtra("press", press);
                                intent.putExtra("temp", temp);
                                intent.putExtra("date", date);
                                intent.putExtra("lat", lat);
                                intent.putExtra("lng", lng);
                                startActivity(intent);
                            }
                        } else {
                            Toast.makeText(MapActivity.this, "Došlo k chybě", Toast.LENGTH_LONG).show();
                        }
                    }
                });

        return false;
    }
}
