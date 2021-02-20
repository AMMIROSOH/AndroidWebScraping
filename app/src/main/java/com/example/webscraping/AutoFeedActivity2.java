package com.example.webscraping;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Calendar;

public class AutoFeedActivity2 extends AppCompatActivity implements SensorEventListener {

    private static final String TAG = "AutoFeedActivity2";
    private SensorManager sensorManager;
    private boolean acc_exist = true, mag_exist = true, gyro_exist = true;
    Sensor accelerometer, magnetometer, gyroscope;
    private static boolean STATUS = false;
    TextView Error_Text, ACCX, ACCY, ACCZ, MAGX, MAGY, MAGZ, GYROX, GYROY, GYROZ;
    JSON_PARSE JP;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_feed);

        Error_Text = (TextView)findViewById(R.id.Error_Text);
        ACCX = (TextView)findViewById(R.id.AccX);
        ACCY = (TextView)findViewById(R.id.AccY);
        ACCZ = (TextView)findViewById(R.id.AccZ);
        MAGX = (TextView)findViewById(R.id.MagX);
        MAGY = (TextView)findViewById(R.id.MagY);
        MAGZ = (TextView)findViewById(R.id.MagZ);
        GYROX = (TextView)findViewById(R.id.GyroX);
        GYROY = (TextView)findViewById(R.id.GyroY);
        GYROZ = (TextView)findViewById(R.id.GyroZ);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);


        JP = new JSON_PARSE(getApplicationContext());
    }
    @Override
    public void onBackPressed() {
        STATUS = false;
        Set_Sensor_Listener();
        JP.empty_auto(getApplicationContext());
        Intent intent = new Intent(AutoFeedActivity2.this, MainActivity.class);
        startActivity(intent);
    }
    @Override
    protected void onResume() {
        super.onResume();

        final Button put_data_Button = (Button)findViewById(R.id.put_data);
        final Button Start_gathering_Button = (Button)findViewById(R.id.Start_gathering_auto);
        put_data_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (STATUS){
                    STATUS = false;
                    Start_gathering_Button.setText("Start");
                    JP.put_auto_acc(getApplicationContext());
                    //////////////////////////samsung-j6
                    //JP.put_auto_mag(getApplicationContext());
                    //JP.put_auto_gyro(getApplicationContext());
                    ////////////////////////////////////
                    Set_Sensor_Listener();
                }
                else {
                    Toast.makeText(getApplicationContext(), "First Start the Recording!",Toast.LENGTH_SHORT).show();
                }
            }
        });
        Start_gathering_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (STATUS){
                    STATUS = false;
                    Start_gathering_Button.setText("Start");
                    Set_Sensor_Listener();
                    JP.empty_auto(getApplicationContext());
                }
                else {
                    STATUS = true;
                    Start_gathering_Button.setText("Stop");
                    Set_Sensor_Listener();
                }
            }
        });
    }

    public void Set_Sensor_Listener(){
        if(STATUS){
            try {
                sensorManager.registerListener(AutoFeedActivity2.this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
                sensorManager.registerListener(AutoFeedActivity2.this, magnetometer, SensorManager.SENSOR_DELAY_NORMAL);
                sensorManager.registerListener(AutoFeedActivity2.this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL);
                if(accelerometer == null){
                    Error_Text.setText("No Accelerometer Found!!!");
                    acc_exist = false;
                }
                else if(magnetometer == null){
                    Error_Text.setText("No Magnetometer Found!!!");
                    mag_exist = false;
                }
                else if(gyroscope == null){
                    Error_Text.setText("No GyroScope Found!!!");
                    gyro_exist = false;
                }
                else{
                    Log.d(TAG, "Registered!!");
                }
                Thread.sleep(0);
            } catch(InterruptedException e) {
                Log.e(TAG, "InterruptedException" , e);
            }
        }
        else {
            sensorManager.unregisterListener(AutoFeedActivity2.this);

            ACCX.setText("");
            ACCY.setText("");
            ACCZ.setText("");
            MAGX.setText("");
            MAGY.setText("");
            MAGZ.setText("");
            GYROX.setText("");
            GYROY.setText("");
            GYROZ.setText("");
            Error_Text.setText("");
        }
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        double currentTime = Calendar.getInstance().getTimeInMillis();
        Sensor sensor = event.sensor;

        if (acc_exist && sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            JSONArray acc = new JSONArray();
            try {
                acc.put(event.values[0]);
                acc.put(event.values[1]);
                acc.put(event.values[2]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JP.add_auto_input(acc, "accelerometer", currentTime, getApplicationContext());
            ACCX.setText("ACC X: " + event.values[0]);
            ACCY.setText("ACC Y: " + event.values[1]);
            ACCZ.setText("ACC Z: " + event.values[2]);
        }
        if(mag_exist && sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD){
            JSONArray mag = new JSONArray();
            try {
                mag.put(event.values[0]);
                mag.put(event.values[1]);
                mag.put(event.values[2]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JP.add_auto_input(mag, "magnetometer", currentTime, getApplicationContext());
            MAGX.setText("MAG X: " + event.values[0]);
            MAGY.setText("MAG Y: " + event.values[1]);
            MAGZ.setText("MAG Z: " + event.values[2]);
        }
        if(gyro_exist && sensor.getType() == Sensor.TYPE_GYROSCOPE){
            //Angular speed around the x-axis
            JSONArray gyro = new JSONArray();
            try {
                gyro.put(event.values[0]);
                gyro.put(event.values[1]);
                gyro.put(event.values[2]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JP.add_auto_input(gyro, "gyroscope", currentTime, getApplicationContext());
            GYROX.setText("GYRO X: " + event.values[0]);
            GYROY.setText("GYRO Y: " + event.values[1]);
            GYROZ.setText("GYRO Z: " + event.values[2]);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}