package cz.cellar.meteoapp;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity{

    private Button btnTemp;
    private Button btnPress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
