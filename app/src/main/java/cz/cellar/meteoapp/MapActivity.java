package cz.cellar.meteoapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MapActivity  extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        //callback pro zavolani onready
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
        Float tempv = 123.1f;
        Float pressv = 456.1f;


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
