package cz.cellar.meteoapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import cz.cellar.meteoapp.model.Pressure;
import cz.cellar.meteoapp.model.Temperature;

public class PressureActivity extends AppCompatActivity implements SensorEventListener {

    private static final String TAG = "TemperatureActivity";
    private SensorManager sensorManager;
    private Sensor pressure;
    TextView press;
    ImageView good, danger, warning;
    private Pressure pressureModel;
    private Button pressBack;
    private float pv;
    public static final String PRESS_VALUES = "PressValuesFile";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        pressureModel=new Pressure();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pressure);


        press=findViewById(R.id.press);
        good=findViewById(R.id.good);
        warning=findViewById(R.id.warning);
        danger=findViewById(R.id.danger);
        good.setVisibility(View.INVISIBLE);
        warning.setVisibility(View.INVISIBLE);
        danger.setVisibility(View.INVISIBLE);
        pressBack=findViewById(R.id.pressBack);





        Log.d(TAG, "onCreate: Initializing Sensor Services");
        sensorManager=(SensorManager) getSystemService(Context.SENSOR_SERVICE);

        pressure= sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        if(pressure!=null){
            sensorManager.registerListener(PressureActivity.this, pressure, SensorManager.SENSOR_DELAY_NORMAL);
            Log.d(TAG, "onCreate: Registered pressure listener");
        }else{
            press.setText("Pressure Sensor Not Supported");
        }
        pressBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences pressshare = getSharedPreferences(PRESS_VALUES,0);
                SharedPreferences.Editor editor2 = pressshare.edit();
                editor2.putFloat("pressshare", pv);
                editor2.commit();
                finish();

            }
        });

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        if(sensor.getType() == Sensor.TYPE_PRESSURE){
            press.setText("Atmosferický tlak: "+event.values[0]+" hPa");
            pv=event.values[0];
            pressureModel.setValue(event.values[0]);
            pressureModel.countStats(event.values[0]);

            if(pressureModel.getStatus()==1){
                good.setVisibility(View.VISIBLE);
                warning.setVisibility(View.INVISIBLE);
                danger.setVisibility(View.INVISIBLE);
            }else if(pressureModel.getStatus()==2){
                warning.setVisibility(View.VISIBLE);
                good.setVisibility(View.INVISIBLE);
                danger.setVisibility(View.INVISIBLE);
            }else if(pressureModel.getStatus()==3){
                danger.setVisibility(View.VISIBLE);
                good.setVisibility(View.INVISIBLE);
                warning.setVisibility(View.INVISIBLE);
            }

            getWindow().getDecorView().setBackgroundColor(pressureModel.getColor());

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
