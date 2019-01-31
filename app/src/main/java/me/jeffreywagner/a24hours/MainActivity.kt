package me.jeffreywagner.a24hours

import android.os.Bundle
import android.os.SystemClock
import android.app.Activity
import android.app.TimePickerDialog
import android.content.pm.ActivityInfo
import android.view.View
import android.widget.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : Activity() {

    private lateinit var chronometerMain: Chronometer
    private lateinit var chronometer1: Chronometer
    private lateinit var chronometerCur: Chronometer
    private lateinit var setTimePicker: TimePickerDialog
    private lateinit var setGoal1Time: TimePickerDialog
    private lateinit var progress1: ProgressBar
    var baseTime = 0L
    var goal1Time = 0
    var ticks = 0
    var isPlaying = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        val startButton = findViewById<Button>(R.id.startButton)
        val setTimeButton = findViewById<Button>(R.id.setTimeButton)
        val goal1Button = findViewById<Button>(R.id.goal1)
        var goal1Text = findViewById<TextView>(R.id.textView1)

        chronometerMain = findViewById(R.id.chronometer_main)
        chronometer1 = findViewById(R.id.chronometer1)
        chronometerCur = chronometer1

        progress1 = findViewById(R.id.progressBar1)
        progress1.max = 0
        progress1.progress = 0

        chronometerMain.setOnChronometerTickListener {
            if (ticks > 1) {progress1.progress += 1}
            ticks++
        }

        setTimePicker = TimePickerDialog(this, R.style.HoloDialog,TimePickerDialog.OnTimeSetListener(function = { view, h, m ->
            baseTime = h.toLong()*3600000 + m.toLong()*60000
            chronometerMain.base = SystemClock.elapsedRealtime() - baseTime
            chronometerCur.base = SystemClock.elapsedRealtime() - baseTime
        }), 0, 0, true)

        setGoal1Time = TimePickerDialog(this, R.style.HoloDialog,TimePickerDialog.OnTimeSetListener(function = { view, h, m ->
            goal1Time = h *3600 + m * 60
            progress1.max = goal1Time
            goal1Text.text = "/ ${if (h>=10){h} else{"0"+h}}:${if (m>=10){m} else{"0"+m}}:00"
        }), 0, 0, true)

        fun stopTimer() {
            baseTime = SystemClock.elapsedRealtime() - chronometerMain.base
            chronometerMain.stop()
            chronometerCur.stop()
            isPlaying = false
            startButton.setText(R.string.start)
        }

        startButton.setOnClickListener {
            if (!isPlaying) {
                ticks = 0
                chronometerMain.base = SystemClock.elapsedRealtime() - baseTime
                chronometerCur.base = SystemClock.elapsedRealtime() - baseTime
                chronometerMain.start()
                chronometerCur.start()
                isPlaying = true


            }
            else {
                stopTimer()
            }
            startButton.setText(if (!isPlaying) R.string.start else R.string.stop)
        }

        setTimeButton.setOnClickListener {
            if (isPlaying) {
                stopTimer()
            }
            setTimePicker.show()
        }

        goal1Button.setOnClickListener {
            setGoal1Time.show()
        }
    }
}

