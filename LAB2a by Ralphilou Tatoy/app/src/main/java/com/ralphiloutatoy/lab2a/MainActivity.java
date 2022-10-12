package com.ralphiloutatoy.lab2a;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.EditText;

public class MainActivity extends Activity implements SensorEventListener{
    private SensorManager sensorManager;
    private Sensor light;

    @Override
    public final void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Get an instance of the sensor service, and use that to get an instance of
        //a particular sensor
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        light = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy){
        //do something here if sensor accuracy changes
    }

    @Override
    public final void onSensorChanged(SensorEvent event){
        EditText field = findViewById(R.id.lightValue);
        field.setText(event.values[0]+"lux");
    }

    @Override
    public void onResume(){
        //Register a listener for the sensor
        super.onResume();
        sensorManager.registerListener(this, light, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause(){
        //Be sure to unregister the sensor when the activity pauses
        super.onPause();
        sensorManager.unregisterListener(this);
    }
}