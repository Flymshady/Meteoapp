package cz.cellar.meteoapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.FirebaseFirestore;

public class MarkerActivity extends AppCompatActivity {

    TextView user, date, tempValue, pressValue, latlng;
    Button btnBackToMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker);

        user=findViewById(R.id.user);
        date=findViewById(R.id.date);
        tempValue=findViewById(R.id.tempValue);
        pressValue=findViewById(R.id.pressValue);
        latlng=findViewById(R.id.latlang);
        btnBackToMap=findViewById(R.id.btnBackToMap);

        Bundle extras = getIntent().getExtras();
        if(extras != null){


            user.setText("Uživatel: "+extras.getString("user"));
            tempValue.setText("Teplota: "+extras.getString("temp") +" °C");
            pressValue.setText("Tlak: "+extras.getString("press")+" hPa");
            date.setText("Naměřeno: "+extras.getString("date"));
            latlng.setText("Souřadnice: "+(extras.get("lat")+";"+extras.get("lng")));



        }else{
            user.setText("Došlo k chybě");
            tempValue.setText("Došlo k chybě");
            pressValue.setText("Došlo k chybě");
            date.setText("Došlo k chybě");
            latlng.setText("Došlo k chybě");

        }
        btnBackToMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

}
