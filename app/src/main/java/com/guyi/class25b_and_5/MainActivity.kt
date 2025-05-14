package com.guyi.class25b_and_5

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.guyi.class25b_and_5.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var sensorManager: SensorManager
    private var proximitySensor: Sensor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    }

    override fun onResume() {
        super.onResume()
        // 3. Register listener: SENSOR_DELAY_NORMAL is usually fine for proximity
        proximitySensor?.also { sensor ->
            sensorManager.registerListener(
                proximitySensorListener,
                sensor,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }
    }

    override fun onPause() {
        super.onPause()
        // 4. Unregister to save battery
        sensorManager.unregisterListener(proximitySensorListener)
    }

    private var proximitySensorListener: SensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent?) {
            val v0 = event?.values?.getOrNull(0)?.twoDecimals()
            val v1 = event?.values?.getOrNull(1)?.twoDecimals()
            val v2 = event?.values?.getOrNull(2)?.twoDecimals()

            binding.lblInfo.text = "${v0}\n" +
                    "${v1}\n" +
                    "${v2}"

            newSensorValue(event?.values?.getOrNull(0) ?: 0f)
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
            // Handle accuracy changes
        }

    }

    var lastTimeStamp: Long = 0
    private fun newSensorValue(v0: Float) {
        if (System.currentTimeMillis() - lastTimeStamp > 5000) {
            if (v0 > 8.0f) {
                SoundEffectsUtility.play(R.raw.snd_coin)
                lastTimeStamp = System.currentTimeMillis()
            } else if (v0 < -8.0f) {
                SoundEffectsUtility.play(R.raw.snd_game_over)
                lastTimeStamp = System.currentTimeMillis()
            }
        }

        if (v0 > 3.0  &&  MyBackgroundMusic.instance?.isMusicPlaying == false) {
            MyBackgroundMusic.instance?.playMusic()
        } else if (v0 < 3.0  &&  MyBackgroundMusic.instance?.isMusicPlaying == true) {
            MyBackgroundMusic.instance?.pauseMusic()
        }
    }
}

fun Float.twoDecimals(): String = "%.2f".format(this)
