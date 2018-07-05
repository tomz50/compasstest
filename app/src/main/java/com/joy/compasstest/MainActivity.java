package com.joy.compasstest;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private ImageView pic;
    private TextView tvHeading;
    private float currentDegree = 0f;
    private SensorManager mSensorManager;
    private Sensor aSensor;
    private Sensor mSensor;
    float[] accelerometerValues = new float[3];
    float[] magneticFieldValues = new float[3];

    @Override
    protected void onResume() {
        super.onResume();

        // 註冊Sensor
        mSensorManager.registerListener(this, aSensor,SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mSensor,SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // 停止Sensor
        mSensorManager.unregisterListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pic = findViewById(R.id.imageView);
        tvHeading = findViewById(R.id.tvHeading);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        aSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        float degree = Math.round(event.values[0]);

        tvHeading.setText("Heading: " + Float.toString(degree) + " 角度");

        // 建立旋轉動畫
        RotateAnimation ra = new RotateAnimation(

                currentDegree, //開始角度
                -degree,  //結束角度

                Animation.RELATIVE_TO_SELF, 0.5f,  //X軸的伸縮模式,伸縮值0.5
                Animation.RELATIVE_TO_SELF, 0.5f); //Y軸的伸縮模式,伸縮值0.5

        // 動畫停留時間
        ra.setDuration(2000);

        // 動畫執行完後,是否停留在執行完的狀態
        ra.setFillAfter(true);

        // 開始動畫
        pic.startAnimation(ra);
        currentDegree = -degree;

        //計算方向數值
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
            magneticFieldValues = event.values;
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            accelerometerValues = event.values;

        Log.i("mSensor =",String.valueOf(magneticFieldValues[2]));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}