package me.jeffreywagner.a24hours

import android.os.Bundle
import android.os.SystemClock
import android.app.Activity
import android.view.View
import android.widget.Button
import android.widget.Chronometer

class MainActivity : Activity() {

    private lateinit var chronometer: Chronometer
    var baseTime = 0L
    var isPlaying = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        chronometer = findViewById(R.id.chronometer)
        val startButton = findViewById<Button>(R.id.startButton)

        startButton.setOnClickListener {v ->
            if (!isPlaying) {
                chronometer.base = SystemClock.elapsedRealtime() - baseTime
                chronometer.start()
                isPlaying = true
            } else {
                baseTime = SystemClock.elapsedRealtime() - chronometer.base
                chronometer.stop()
                isPlaying = false
            }
            startButton.setText(if (!isPlaying) R.string.start else R.string.stop)
        }
    }
}

