package cz.cellar.meteoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private static final String TAG = "MainActivity";
    private SensorManager sensorManager;
    private Sensor temperature, pressure;

    TextView temp, press, tempInfo, pressInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        temp=findViewById(R.id.temp);
        press=findViewById(R.id.press);

    //    Log.d(TAG, "onCreate: Initializing Sensor Services");
        sensorManager=(SensorManager) getSystemService(Context.SENSOR_SERVICE);

        temperature= sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        if(temperature!=null){
            sensorManager.registerListener(MainActivity.this, temperature, SensorManager.SENSOR_DELAY_NORMAL);
         //   Log.d(TAG, "onCreate: Registered temperature listener");
        }else{
            temp.setText("Temperature Sensor Not Supported");
        }

        pressure= sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        if(pressure!=null){
            sensorManager.registerListener(MainActivity.this, pressure, SensorManager.SENSOR_DELAY_NORMAL);
          //  Log.d(TAG, "onCreate: Registered pressure listener");
        }else{
            press.setText("Pressure Sensor Not Supported");
        }


    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        if(sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE){
            temp.setText("Teplota: "+event.values[0]);
       //     tempInfo.setText("todo");

        }
        else if(sensor.getType() == Sensor.TYPE_PRESSURE){
            press.setText("Tlak: "+event.values[0]);
      //      pressInfo.setText("todo");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


}
