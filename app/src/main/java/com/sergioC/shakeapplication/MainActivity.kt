package com.sergioC.shakeapplication

import android.content.Context
import android.content.pm.ActivityInfo
import android.hardware.Sensor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.hardware.SensorManager
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.sergioC.shakeapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), ShakeListener {

    private var mSensorManager: SensorManager? = null
    private var mAccelerometer: Sensor? = null
    private var mShakeAction: ShakeAction? = null
    private var binding: ActivityMainBinding? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mAccelerometer = mSensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        mShakeAction = ShakeAction()
        mShakeAction!!.setListener(this)
    }

    override fun onShakeAction(value: Int) {
        if (value == 0){
            binding!!.label.setText(getString(R.string.shake_none))
            binding!!.background.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite))
        }else if(value == 1){
            binding!!.label.setText(getString(R.string.shake_medium))
            binding!!.background.setBackgroundColor(ContextCompat.getColor(this, R.color.colorYellow))
        }else if(value == 2){
            binding!!.label.setText(getString(R.string.shake_high))
            binding!!.background.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGreen))
        }
    }

    override fun onResume() {
        super.onResume()
        mSensorManager!!.registerListener(mShakeAction, mAccelerometer, SensorManager.SENSOR_DELAY_UI)
    }

    override fun onPause() {
        mSensorManager!!.unregisterListener(mShakeAction)
        super.onPause()
    }
}
