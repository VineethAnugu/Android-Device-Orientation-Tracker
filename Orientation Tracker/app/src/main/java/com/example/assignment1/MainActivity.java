package com.example.assignment1;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SensorEventListener {

    private TextView Acc_X, Acc_Y, Acc_Z, Gyr_X, Gyr_Y, Gyr_Z, Mag_X, Mag_Y, Mag_Z,AccPitch, AccRoll, GyrPitch, GyrRoll, FinalPitch, FinalRoll;
    private Button button;
    private SensorManager sensorManager_;
    private Sensor accelerometer, gyroscope, magnetometer;
    private float[] AccValues, GyrValues, MagValues;
    private double Apitch, Aroll, pitchAngle, rollAngle;
    private double Gpitch,Groll = 0;

    protected float[] lowPass(float[] in ,float[] out ) {
        if (out == null) return in;
        for (int i = 0; i < in.length; i++) {
            out[i] = out[i] + 0.01f * (in[i] - out[i]);
        }
        return out;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Acc_X = (TextView) findViewById(R.id.Acc_X);
        Acc_Y = (TextView) findViewById(R.id.Acc_Y);
        Acc_Z = (TextView) findViewById(R.id.Acc_Z);
        Gyr_X = (TextView) findViewById(R.id.Gyr_X);
        Gyr_Y = (TextView) findViewById(R.id.Gyr_Y);
        Gyr_Z = (TextView) findViewById(R.id.Gyr_Z);
        Mag_X = (TextView)findViewById(R.id.Mag_X);
        Mag_Y = (TextView)findViewById(R.id.Mag_Y);
        Mag_Z = (TextView)findViewById(R.id.Mag_Z);
        FinalPitch = (TextView)findViewById(R.id.FinalPitch);
        FinalRoll = (TextView)findViewById(R.id.FinalRoll);
        AccPitch = (TextView)findViewById(R.id.AccPitch);
        AccRoll = (TextView)findViewById(R.id.AccRoll);
        GyrPitch = (TextView)findViewById(R.id.GyrPitch);
        GyrRoll = (TextView)findViewById(R.id.GyrRoll);
        button = (Button)findViewById(R.id.button);
        button.setOnClickListener(this);

        sensorManager_ = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager_.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (accelerometer == null) {
            Toast.makeText(getApplicationContext(), "Accelerometer not available on this device", Toast.LENGTH_LONG);

        }

        gyroscope = sensorManager_.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        if (gyroscope == null) {
            Toast.makeText(getApplicationContext(), "Gyroscope not available on this device", Toast.LENGTH_LONG);
        }

        magnetometer = sensorManager_.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        if (magnetometer == null) {
            Toast.makeText(getApplicationContext(), "Magnetometer not available on this device", Toast.LENGTH_LONG);
        }



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                sensorManager_.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
                sensorManager_.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL);
                sensorManager_.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_NORMAL);
                break;
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            Acc_X.setText("X-axis: "+Float.toString(event.values[0]));
            Acc_Y.setText("Y-axis: "+Float.toString(event.values[1]));
            Acc_Z.setText("Z-axis: "+Float.toString(event.values[2]));
            AccValues = lowPass(event.values.clone(), AccValues);
            Apitch = Math.toDegrees(Math.atan(AccValues[1]/AccValues[2]));
            Aroll = Math.toDegrees(Math.atan((-AccValues[0])/(Math.sqrt((AccValues[1]*AccValues[1]) + (AccValues[2]*AccValues[2])))));
            AccPitch.setText("Pitch: "+Double.toString(Apitch));
            AccRoll.setText("Roll:"+Double.toString(Aroll));
        }

        if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            Gyr_X.setText("X-axis: "+Float.toString(event.values[0]));
            Gyr_Y.setText("Y-axis: "+Float.toString(event.values[1]));
            Gyr_Z.setText("Z-axis: "+Float.toString(event.values[2]));
            GyrValues = event.values.clone();
            Gpitch = Gpitch +  Math.toDegrees((event.values[0] * 0.2));
            Groll = Groll + Math.toDegrees((event.values[1] * 0.2));

            if(Gpitch > 360 || Gpitch < -360){
                Gpitch = Gpitch/360;
            }
            if(Groll > 360 || Groll < -360 ){
                Groll = Groll/360;
            }

            GyrPitch.setText("Pitch:  "+Double.toString(Gpitch));
            GyrRoll.setText("Roll:  "+Double.toString(Groll));
        }

        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            Mag_X.setText("X-axis: "+Float.toString(event.values[0]));
            Mag_Y.setText("Y-axis: "+Float.toString(event.values[1]));
            Mag_Z.setText("Z-axis: "+Float.toString(event.values[2]));
            MagValues = event.values.clone();

        }

        pitchAngle = 0.85 * ( Gpitch ) + 0.15*(Apitch);
        rollAngle = 0.75 * ( Groll ) + 0.25*(Aroll);
        FinalPitch.setText("Pitch:  "+Double.toString(pitchAngle));
        FinalRoll.setText("Roll:  "+Double.toString(rollAngle));

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
