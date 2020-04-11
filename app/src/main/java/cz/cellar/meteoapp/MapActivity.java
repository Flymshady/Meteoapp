package cz.cellar.meteoapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
    Button btnShare, btnLocate;
    FusedLocationProviderClient fusedLocationProviderClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        //callback pro zavolani onready
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        btnShare=findViewById(R.id.btnLocate);
        btnLocate=findViewById(R.id.btnLocate);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        btnLocate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ActivityCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
                    getLocation();
                }else {
                    ActivityCompat.requestPermissions(MapActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
                }
            }
        });
    }

    private void getLocation() {
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location=task.getResult();
                if(location!=null){
                    Geocoder geocoder = new Geocoder(MapActivity.this, Locale.getDefault());
                    try {
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(),1);
                        Toast.makeText(MapActivity.this, "Lokalizováno: "+location.getLatitude()+";"+location.getLongitude(), Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(MapActivity.this, "Polohu se nepodařilo zaměřit", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney, Australia, and move the camera.
     //   LatLng sydney = new LatLng(-34, 151);
      //  mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
      //  mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
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

// Add a new document with a generated ID
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
