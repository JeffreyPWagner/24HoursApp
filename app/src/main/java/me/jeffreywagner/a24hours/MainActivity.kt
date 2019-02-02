package me.jeffreywagner.a24hours

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.SystemClock
import android.app.Activity
import android.app.AlertDialog
import android.app.TimePickerDialog
import android.content.pm.ActivityInfo
import android.view.WindowManager
import android.widget.*

class MainActivity : Activity() {

    private lateinit var chronoMain: Chronometer
    private lateinit var chrono1: Chronometer
    private lateinit var chrono2: Chronometer
    private lateinit var chrono3: Chronometer
    private lateinit var chrono4: Chronometer
    private lateinit var chronoCur: Chronometer

    private lateinit var startBut: Button
    private lateinit var adjTimeBut: Button
    private lateinit var goal1But: Button
    private lateinit var goal2But: Button
    private lateinit var goal3But: Button
    private lateinit var goal4But: Button

    private lateinit var progress1: ProgressBar
    private lateinit var progress2: ProgressBar
    private lateinit var progress3: ProgressBar
    private lateinit var progress4: ProgressBar
    private lateinit var progressCur: ProgressBar

    private lateinit var goal1Text: TextView
    private lateinit var goal2Text: TextView
    private lateinit var goal3Text: TextView
    private lateinit var goal4Text: TextView

    private lateinit var adjustTime: TimePickerDialog
    private lateinit var setGoalTime: TimePickerDialog

    private val defaultGoal = "/ 01:00:00"
    private val defaultTime = 3600

    private var ticks = 0
    private var isPlaying = false
    private var baseTime = 0L

    private var goal1Time = defaultTime
    private var goal2Time = defaultTime
    private var goal3Time = defaultTime
    private var goal4Time = defaultTime


    private fun chronoSetUp(c: Chronometer) {
        c.format = "00:%s"
        c.base = SystemClock.elapsedRealtime() - 0
    }

    private fun stopTimer() {
        baseTime = SystemClock.elapsedRealtime() - chronoMain.base
        chronoMain.stop()
        chronoCur.stop()
        isPlaying = false
        startBut.setText(R.string.start)
    }

    @SuppressLint("InflateParams")
    private fun showGoalDialog(b: Button) {
        val li = this.layoutInflater
        val promptView = li.inflate(R.layout.edit_text_dialog, null)
        val dialogBuilder = AlertDialog.Builder(this)


        dialogBuilder.setView(promptView)

        val userInput = promptView.findViewById(R.id.etUserInput) as EditText

        dialogBuilder.setPositiveButton("OK") { _, _ ->
            b.text = userInput.text.toString()


        }
        dialogBuilder.setNegativeButton("Cancel") { _, _ ->

        }
        val dialog = dialogBuilder.create()

        dialog.show()
        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        startBut = findViewById(R.id.startButton)
        adjTimeBut = findViewById(R.id.adjustTimeButton)

        goal1But = findViewById(R.id.goal1)
        goal2But = findViewById(R.id.goal2)
        goal3But = findViewById(R.id.goal3)
        goal4But = findViewById(R.id.goal4)

        goal1But.text = getString(R.string.goal1)
        goal2But.text = getString(R.string.goal2)
        goal3But.text = getString(R.string.goal3)
        goal4But.text = getString(R.string.goal4)


        goal1Text = findViewById(R.id.textView1)
        goal2Text = findViewById(R.id.textView2)
        goal3Text = findViewById(R.id.textView3)
        goal4Text = findViewById(R.id.textView4)

        goal1Text.text = defaultGoal
        goal2Text.text = defaultGoal
        goal3Text.text = defaultGoal
        goal4Text.text = defaultGoal

        chronoMain = findViewById(R.id.chronometer_main)
        chronoSetUp(chronoMain)
        chrono1 = findViewById(R.id.chronometer1)
        chronoSetUp(chrono1)
        chrono2 = findViewById(R.id.chronometer2)
        chronoSetUp(chrono2)
        chrono3 = findViewById(R.id.chronometer3)
        chronoSetUp(chrono3)
        chrono4 = findViewById(R.id.chronometer4)
        chronoSetUp(chrono4)

        chronoCur = chrono2
        //TODO build current chrono selection into code

        progress1 = findViewById(R.id.progressBar1)
        progress1.max = goal1Time
        progress1.progress = 0

        progress2 = findViewById(R.id.progressBar1)
        progress2.max = goal2Time
        progress2.progress = 0

        progress3 = findViewById(R.id.progressBar1)
        progress3.max = goal3Time
        progress3.progress = 0

        progress4 = findViewById(R.id.progressBar1)
        progress4.max = goal4Time
        progress4.progress = 0

        chronoMain.setOnChronometerTickListener {
            if (ticks > 1) {progress1.progress += 1}
            ticks++
            val elapsedMil = SystemClock.elapsedRealtime() - chronoMain.base
            if (elapsedMil > 3600000L) {
                chronoMain.format = "0%s"
                chronoCur.format = "0%s"
            }
            else {
                chronoMain.format = "00:%s"
                chronoCur.format = "00:%s"
            }
        }

        adjustTime = TimePickerDialog(this, R.style.HoloDialog,TimePickerDialog.OnTimeSetListener(function = { _, h, m ->
            baseTime = h.toLong()*3600000 + m.toLong()*60000
            chronoMain.base = SystemClock.elapsedRealtime() - baseTime
            chronoCur.base = SystemClock.elapsedRealtime() - baseTime
            //TODO adjust chrono format based on entered
            //TODO update progress bar
        }), 0, 0, true)

        setGoalTime = TimePickerDialog(this, R.style.HoloDialog,TimePickerDialog.OnTimeSetListener { _, h, m ->
            goal1Time = h *3600 + m * 60
            progress1.max = goal1Time
            val hs = h.toString()
            val ms = m.toString()
            goal1Text.text = "/ ${if (h>=10){hs} else{
                "0$h"
            }}:${if (m>=10){ms} else{
                "0$ms"
            }}:00"
        }, 0, 0, true)

        startBut.setOnClickListener {
            if (!isPlaying) {
                ticks = 0
                chronoMain.base = SystemClock.elapsedRealtime() - baseTime
                chronoCur.base = SystemClock.elapsedRealtime() - baseTime
                chronoMain.start()
                chronoCur.start()
                isPlaying = true
            }
            else {
                stopTimer()
            }
            startBut.setText(if (!isPlaying) R.string.start else R.string.stop)
        }

        adjTimeBut.setOnClickListener {
            if (isPlaying) {
                stopTimer()
            }
            adjustTime.show()
        }

        goal1But.setOnClickListener {
            chronoCur = chrono1
            //TODO reset chronos to current goal
        }
        goal2But.setOnClickListener {
            chronoCur = chrono2
        }
        goal3But.setOnClickListener {
            chronoCur = chrono3
        }
        goal4But.setOnClickListener {
            chronoCur = chrono4
        }

        goal1But.setOnLongClickListener {
            showGoalDialog(goal1But)
            return@setOnLongClickListener true
        }
        goal2But.setOnLongClickListener {
            showGoalDialog(goal2But)
            return@setOnLongClickListener true
        }
        goal3But.setOnLongClickListener {
            showGoalDialog(goal3But)
            return@setOnLongClickListener true
        }
        goal4But.setOnLongClickListener {
            showGoalDialog(goal4But)
            return@setOnLongClickListener true
        }
    }
}

