package cz.cellar.meteoapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import cz.cellar.meteoapp.PressureActivity;
import cz.cellar.meteoapp.R;
import cz.cellar.meteoapp.TemperatureActivity;

public class HomeActivity extends AppCompatActivity{

    Button btnTemp;
    Button btnPress;
    Button btnLogout;
    FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

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
    }

    private void launchActivityTemp() {
        Intent intent = new Intent(this, TemperatureActivity.class);
        startActivity(intent);
    }
    private void launchActivityPress() {
        Intent intent = new Intent(this, PressureActivity.class);
        startActivity(intent);
    }

}
