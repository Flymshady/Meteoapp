package cz.cellar.meteoapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import cz.cellar.meteoapp.model.Temperature;

public class TemperatureActivity  extends AppCompatActivity implements SensorEventListener {

    private static final String TAG = "TemperatureActivity";
    private SensorManager sensorManager;
    private Sensor temperature;
    TextView temp, tempInfo;
    private Temperature temperatureModel;
    private Button tempBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        temperatureModel=new Temperature();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temperature);



        temp=findViewById(R.id.temp);
        tempInfo=findViewById(R.id.tempInfo);
        tempBack=findViewById(R.id.tempBack);

        tempBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Log.d(TAG, "onCreate: Initializing Sensor Services");
        sensorManager=(SensorManager) getSystemService(Context.SENSOR_SERVICE);

        temperature= sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        if(temperature!=null){
            sensorManager.registerListener(TemperatureActivity.this, temperature, SensorManager.SENSOR_DELAY_NORMAL);
            Log.d(TAG, "onCreate: Registered temperature listener");
        }else{
            temp.setText("Temperature Sensor Not Supported");
        }

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        if(sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE){
            temp.setText("Teplota: "+event.values[0]+" °C");
            temperatureModel.setValue(event.values[0]);
            temperatureModel.countStats(event.values[0]);
            tempInfo.setText(temperatureModel.getInfo());
            getWindow().getDecorView().setBackgroundColor(temperatureModel.getColor());

         //   temperatureModel=new Temperature(event.values[0]);
         //   temp.setText("Teplota: "+temperatureModel.getValue()+" °C");
         //   tempInfo.setText(temperatureModel.getInfo());
         //   currentView.setBackgroundColor(Integer.parseInt(temperatureModel.getColor()));

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}