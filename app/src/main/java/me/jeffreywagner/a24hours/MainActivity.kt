package me.jeffreywagner.a24hours

import android.os.Bundle
import android.os.SystemClock
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.Chronometer

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val chronometer = findViewById<Chronometer>(R.id.chronometer)
        var baseTime: Long = 0


        val startButton = findViewById<Button>(R.id.startButton)
        startButton?.setOnClickListener(object : View.OnClickListener {

            internal var isPlaying = false

            override fun onClick(v: View) {
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
        })
    }
}

