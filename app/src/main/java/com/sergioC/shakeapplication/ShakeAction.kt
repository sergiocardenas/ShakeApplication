package com.sergioC.shakeapplication

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log


class ShakeAction : SensorEventListener {

    private var mLastShakeTimestamp: Long = 0
    private var mShakeCount = 0
    private var mListener: ShakeListener? = null

    override fun onSensorChanged(sensorEvent: SensorEvent) {
        val ejeX = sensorEvent.values[0]
        val ejeY = sensorEvent.values[1]
        val ejeZ = sensorEvent.values[2]

        val gX = ejeX / SensorManager.GRAVITY_EARTH
        val gY = ejeY / SensorManager.GRAVITY_EARTH
        val gZ = ejeZ / SensorManager.GRAVITY_EARTH

        val gForce = Math.sqrt((gX * gX + gY * gY + gZ * gZ).toDouble())
        val now = System.currentTimeMillis()
        if (gForce > SHAKE_THRESHOLD_MEDIUM) {
            if (mLastShakeTimestamp + SHAKE_COOLDOWN_MS > now) {
                return
            }

            var currentCount = 1

            if (gForce > SHAKE_THRESHOLD_HIGH) {
                currentCount = 2
            }

            Log.i("ShakeActionJV", "gforce value: $gForce, count $currentCount")
            if (currentCount != mShakeCount) {
                mLastShakeTimestamp = now

                mShakeCount = currentCount
                if (mListener != null) {
                    mListener!!.onShakeAction(mShakeCount)
                }
            }

        } else {
            if (mLastShakeTimestamp + SHAKE_COOLDOWN_RESET_MS < now) {
                mLastShakeTimestamp = now
                if (mShakeCount != 0) {
                    mShakeCount = 0
                    Log.i("ShakeActionJV", "cooldown validation")
                    if (mListener != null) {
                        mListener!!.onShakeAction(mShakeCount)
                    }
                }
            }
        }

    }

    fun setListener(listener: ShakeListener) {
        mListener = listener
    }

    override fun onAccuracyChanged(sensor: Sensor, i: Int) {

    }

    companion object {
        private val SHAKE_COOLDOWN_MS = 500
        private val SHAKE_COOLDOWN_RESET_MS = 3000
        private val SHAKE_THRESHOLD_HIGH = 3.2
        private val SHAKE_THRESHOLD_MEDIUM = 1.5
    }
}
