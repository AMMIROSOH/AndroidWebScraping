package com.example.webscraping;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Calendar;

public class UserFeedActivity extends AppCompatActivity implements SensorEventListener{

    private static final String TAG = "AutoFeedActivity2";
    private SensorManager sensorManager;
    private boolean acc_exist = true, mag_exist = true, gyro_exist = true;
    Sensor accelerometer, magnetometer, gyroscope;
    private static boolean STATUS = false;
    private int rectime = 0;
    TextView Error_Text, Counter, ACCX, ACCY, ACCZ, MAGX, MAGY, MAGZ, GYROX, GYROY, GYROZ;
    CountDownTimer cdt;
    JSON_PARSE JP;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_feed);
        Error_Text = (TextView)findViewById(R.id.Error_Text);
        Counter = (TextView) findViewById(R.id.counter);
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        final Button Start_gathering_Button = (Button) findViewById(R.id.Start_gathering_user);
        final Button put_data_Button = (Button) findViewById(R.id.put_data);
        final Spinner pick_action = (Spinner) findViewById(R.id.Pick_action);
        final Spinner pick_time = (Spinner) findViewById(R.id.pick_time);
        Counter = (TextView) findViewById(R.id.counter);

        Start_gathering_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (STATUS ){
                    STATUS = false;
                    Start_gathering_Button.setText("Start");
                    Toast.makeText(getApplicationContext(), "Data Was Not Saved!",Toast.LENGTH_LONG).show();
                    Counter.setText("");
                    cdt.cancel();
                    Set_Sensor_Listener();
                }
                else if(pick_action.getSelectedItemPosition() == 0 || pick_time.getSelectedItemPosition() == 0){
                    Toast.makeText(getApplicationContext(), "Pick Time and Action Time!",Toast.LENGTH_SHORT).show();
                }
                else {
                    rectime = pick_time.getSelectedItemPosition() * 5;
                    STATUS = true;
                    Start_gathering_Button.setText("Stop");
                    Set_Sensor_Listener();
                    cdt = new CountDownTimer(rectime*1000, 100) {
                        public void onTick(long millisUntilFinished) {
                            Counter.setText("" + millisUntilFinished /100  * 1.0 / 10);
                        }
                        public void onFinish() {
                            Counter.setText("Done!");
                            STATUS = false;
                            Start_gathering_Button.setText("Start");
                            Set_Sensor_Listener();
                        }

                    }.start();

                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        STATUS = false;
        Set_Sensor_Listener();
        Intent intent = new Intent(UserFeedActivity.this, MainActivity.class);
        startActivity(intent);
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
            //JP.add_auto_input(acc, "accelerometer", currentTime, getApplicationContext());
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
            //JP.add_auto_input(mag, "magnetometer", currentTime, getApplicationContext());
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
            //JP.add_auto_input(gyro, "gyroscope", currentTime, getApplicationContext());
            GYROX.setText("GYRO X: " + event.values[0]);
            GYROY.setText("GYRO Y: " + event.values[1]);
            GYROZ.setText("GYRO Z: " + event.values[2]);
        }
    }

    public void Set_Sensor_Listener(){
        if(STATUS){
            try {
                sensorManager.registerListener(UserFeedActivity.this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
                sensorManager.registerListener(UserFeedActivity.this, magnetometer, SensorManager.SENSOR_DELAY_NORMAL);
                sensorManager.registerListener(UserFeedActivity.this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL);
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
                    Log.d(TAG, "onCreate: Registered!!");
                }
                Thread.sleep(0);
            } catch(InterruptedException e) {
                Log.e(TAG, "InterruptedException" , e);
            }
        }
        else {
            sensorManager.unregisterListener(UserFeedActivity.this);
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
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}