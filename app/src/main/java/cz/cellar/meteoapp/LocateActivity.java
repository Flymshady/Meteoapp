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
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocateActivity extends AppCompatActivity {


    Button btnLocate, btnBack;
    TextView latitude, longitude, country, locality, addressline;
    FusedLocationProviderClient fusedLocationProviderClient;
    public static final String LOCATE_VALUES = "LocateValuesFile";
    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locate);
        btnLocate = findViewById(R.id.btnLocate);
        btnBack = findViewById(R.id.btnBack);
        latitude = findViewById(R.id.latitude);
        longitude = findViewById(R.id.longitude);
        country = findViewById(R.id.country);
        locality = findViewById(R.id.locality);
        addressline = findViewById(R.id.addressline);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences locateshare = getSharedPreferences(LOCATE_VALUES, 0);
                SharedPreferences.Editor editor = locateshare.edit();
                editor.putString("locateshare", addressline.getText().toString());
                editor.commit();
                finish();
            }
        });

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {

                Geocoder geocoder = new Geocoder(LocateActivity.this, Locale.getDefault());
                try {
                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(),1);

                    latitude.setText("Zeměpisná šířka: "+addresses.get(0).getLatitude());
                    longitude.setText("Zeměpisná délka: "+addresses.get(0).getLongitude());
                    country.setText("Stát: "+addresses.get(0).getCountryName());
                    locality.setText("Lokalita: "+addresses.get(0).getLocality());
                    addressline.setText("Adresa: "+addresses.get(0).getAddressLine(0));

                } catch (IOException e) {
                    e.printStackTrace();
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
            configureButton();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 10:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    configureButton();
                return;
        }
    }

    private void configureButton() {
        btnLocate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((ActivityCompat.checkSelfPermission(LocateActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED)&&
                        ActivityCompat.checkSelfPermission(LocateActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET
                    }, 10);
                    return;
                }
                locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);
            }
        });

    }

}
