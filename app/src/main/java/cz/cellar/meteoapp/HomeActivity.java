package cz.cellar.meteoapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Date;

import cz.cellar.meteoapp.PressureActivity;
import cz.cellar.meteoapp.R;
import cz.cellar.meteoapp.TemperatureActivity;

public class HomeActivity extends AppCompatActivity{

    Button btnTemp;
    Button btnPress;
    Button btnLogout;
    Button btnMyMap;
    Button btnShare;
    TextView user, temp, press, date;
    FirebaseAuth mFirebaseAuth;
    private float tv, pv;

    public static final String TEMP_VALUES = "TempValuesFile";
    public static final String PRESS_VALUES = "PressValuesFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mFirebaseAuth = FirebaseAuth.getInstance();

        user=findViewById(R.id.user);
        temp=findViewById(R.id.temp);
        press=findViewById(R.id.press);
        date=findViewById(R.id.date);

        btnShare=findViewById(R.id.btnShare);
        btnLogout=findViewById(R.id.btnLogout);
        btnTemp=findViewById(R.id.btnTemp);
        btnTemp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchActivityTemp();
            }
        });
        btnPress=findViewById(R.id.btnPress);
        btnPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchActivityPress();
            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                finish();
                /*
                Intent intLogout = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intLogout);

                 */
            }
        });
        btnMyMap=findViewById(R.id.btnMyMap);
        btnMyMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchActivityMyMap();
            }
        });

        SharedPreferences tempshare = getSharedPreferences(TEMP_VALUES,0);
        SharedPreferences pressshare = getSharedPreferences(PRESS_VALUES,0);


        temp.setText("Teplota není naměřena");
        press.setText("Atmosferický tlak není naměřen");

       /* Bundle extras = getIntent().getExtras();
        if(extras != null){
            if(extras.get("t")!=null){
                tv= (float) extras.get("t");
                SharedPreferences.Editor editor = tempshare.edit();
                editor.putFloat("tempshare", tv);
                editor.commit();

            }
            if(extras.get("p")!=null){
                pv= (float) extras.get("p");
                SharedPreferences.Editor editor2 = pressshare.edit();
                editor2.putFloat("pressshare", pv);
                editor2.commit();

            }
        }
        else{

        }
*/

        tv = tempshare.getFloat("tempshare", tv);
        temp.setText("Teplota: "+tv+" °C");
        pv = pressshare.getFloat("pressshare", pv);
        press.setText("Tlak: "+pv+" hPa");
        user.setText(mFirebaseAuth.getCurrentUser().getEmail());
        Date d = new Date();
        date.setText(d.toString());
    }

    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_home);
        setContentView(R.layout.activity_home);

        mFirebaseAuth = FirebaseAuth.getInstance();

        user=findViewById(R.id.user);
        temp=findViewById(R.id.temp);
        press=findViewById(R.id.press);
        date=findViewById(R.id.date);

        btnShare=findViewById(R.id.btnShare);
        btnLogout=findViewById(R.id.btnLogout);
        btnTemp=findViewById(R.id.btnTemp);
        btnTemp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchActivityTemp();
            }
        });
        btnPress=findViewById(R.id.btnPress);
        btnPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchActivityPress();
            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intLogout = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intLogout);
            }
        });
        btnMyMap=findViewById(R.id.btnMyMap);
        btnMyMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchActivityMyMap();
            }
        });

        SharedPreferences tempshare = getSharedPreferences(TEMP_VALUES,0);
        SharedPreferences pressshare = getSharedPreferences(PRESS_VALUES,0);


        tv = tempshare.getFloat("tempshare", tv);
        temp.setText("Teplota: "+tv+" °C");
        pv = pressshare.getFloat("pressshare", pv);
        press.setText("Tlak: "+pv+" hPa");
        user.setText(mFirebaseAuth.getCurrentUser().getEmail());
        Date d = new Date();
        date.setText(d.toString());
    }

    private void launchActivityTemp() {
        Intent intent = new Intent(this, TemperatureActivity.class);
        startActivity(intent);
    }
    private void launchActivityPress() {
        Intent intent = new Intent(this, PressureActivity.class);
        startActivity(intent);
    }
    private void launchActivityMyMap() {
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
    }

}
