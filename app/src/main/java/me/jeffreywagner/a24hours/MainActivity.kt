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

    private lateinit var curGoal: Goal
    var goal1 = Goal()
    var goal2 = Goal()
    var goal3 = Goal()
    var goal4 = Goal()

    private lateinit var startBut: Button
    private lateinit var adjTimeBut: Button

    private lateinit var adjustTime: TimePickerDialog
    private lateinit var setGoalTime: TimePickerDialog

    private val defaultGoal = "/ 01:00:00"

    private var ticks = 0
    private var isPlaying = false

    private fun chronoSetUp(c: Chronometer) {
        c.format = "00:%s"
        c.base = SystemClock.elapsedRealtime()
        c.stop()
    }

    private fun stopTimer() {
        curGoal.baseTime = SystemClock.elapsedRealtime() - chronoMain.base
        chronoMain.stop()
        curGoal.chrono.stop()
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

        goal1.but = findViewById(R.id.goal1)
        goal2.but = findViewById(R.id.goal2)
        goal3.but = findViewById(R.id.goal3)
        goal4.but = findViewById(R.id.goal4)

        goal1.but.text = getString(R.string.goal1)
        goal2.but.text = getString(R.string.goal2)
        goal3.but.text = getString(R.string.goal3)
        goal4.but.text = getString(R.string.goal4)

        goal1.goalText = findViewById(R.id.textView1)
        goal2.goalText = findViewById(R.id.textView2)
        goal3.goalText = findViewById(R.id.textView3)
        goal4.goalText = findViewById(R.id.textView4)

        goal1.goalText.text = defaultGoal
        goal2.goalText.text = defaultGoal
        goal3.goalText.text = defaultGoal
        goal4.goalText.text = defaultGoal

        chronoMain = findViewById(R.id.chronometer_main)
        chronoSetUp(chronoMain)
        goal1.chrono = findViewById(R.id.chronometer1)
        chronoSetUp(goal1.chrono)
        goal2.chrono = findViewById(R.id.chronometer2)
        chronoSetUp(goal2.chrono)
        goal3.chrono = findViewById(R.id.chronometer3)
        chronoSetUp(goal3.chrono)
        goal4.chrono = findViewById(R.id.chronometer4)
        chronoSetUp(goal4.chrono)

        goal1.progbar = findViewById(R.id.progressBar1)
        goal1.progbar.max = goal1.goalTime
        goal1.progbar.progress = 0

        goal2.progbar = findViewById(R.id.progressBar2)
        goal2.progbar.max = goal2.goalTime
        goal2.progbar.progress = 0

        goal3.progbar = findViewById(R.id.progressBar3)
        goal3.progbar.max = goal3.goalTime
        goal3.progbar.progress = 0

        goal4.progbar = findViewById(R.id.progressBar4)
        goal4.progbar.max = goal4.goalTime
        goal4.progbar.progress = 0

        curGoal = goal1

        chronoMain.setOnChronometerTickListener {
            if (ticks > 1) {curGoal.progbar.progress += 1}
            ticks++
            val elapsedMil = SystemClock.elapsedRealtime() - chronoMain.base
            if (elapsedMil >= 36000000L) {
                chronoMain.format = "%s"
                curGoal.chrono.format = "%s"
            }
            else if (elapsedMil >= 3600000L) {
                chronoMain.format = "0%s"
                curGoal.chrono.format = "0%s"
            }
            else {
                chronoMain.format = "00:%s"
                curGoal.chrono.format = "00:%s"
            }
        }

        adjustTime = TimePickerDialog(this, R.style.HoloDialog,TimePickerDialog.OnTimeSetListener(function = { _, h, m ->
            curGoal.baseTime = h.toLong()*3600000 + m.toLong()*60000
            if (curGoal.baseTime >= 36000000L) {
                chronoMain.format = "%s"
                curGoal.chrono.format = "%s"
            }
            else if (curGoal.baseTime >= 3600000L) {
                chronoMain.format = "0%s"
                curGoal.chrono.format = "0%s"
            }
            else {
                chronoMain.format = "00:%s"
                curGoal.chrono.format = "00:%s"
            }
            chronoMain.base = SystemClock.elapsedRealtime() - curGoal.baseTime
            curGoal.chrono.base = SystemClock.elapsedRealtime() - curGoal.baseTime
            curGoal.progbar.progress = (curGoal.baseTime / 1000).toInt()
        }), 0, 0, true)

        fun updateGoalTime (g: Goal) {
            setGoalTime = TimePickerDialog(this, R.style.HoloDialog, TimePickerDialog.OnTimeSetListener { _, h, m ->
                g.goalTime = h * 3600 + m * 60
                g.progbar.max = g.goalTime
                val hs = h.toString()
                val ms = m.toString()
                g.goalText.text = "/ ${if (h >= 10) {
                    hs
                } else {
                    "0$h"
                }}:${if (m >= 10) {
                    ms
                } else {
                    "0$ms"
                }}:00"
            }, 0, 0, true)
            setGoalTime.show()
        }
        startBut.setOnClickListener {
            if (!isPlaying) {
                ticks = 0
                chronoMain.base = SystemClock.elapsedRealtime() - curGoal.baseTime
                curGoal.chrono.base = SystemClock.elapsedRealtime() - curGoal.baseTime
                chronoMain.start()
                curGoal.chrono.start()
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

        goal1.but.setOnClickListener {
            if (isPlaying){
                stopTimer()
            }
            curGoal = goal1
            chronoMain.base = SystemClock.elapsedRealtime() - curGoal.baseTime
        }
        goal2.but.setOnClickListener {
            if (isPlaying){
                stopTimer()
            }
            curGoal = goal2
            chronoMain.base = SystemClock.elapsedRealtime() - curGoal.baseTime
        }
        goal3.but.setOnClickListener {
            if (isPlaying){
                stopTimer()
            }
            curGoal = goal3
            chronoMain.base = SystemClock.elapsedRealtime() - curGoal.baseTime
        }
        goal4.but.setOnClickListener {
            if (isPlaying){
                stopTimer()
            }
            curGoal = goal4
            chronoMain.base = SystemClock.elapsedRealtime() - curGoal.baseTime
        }

        goal1.but.setOnLongClickListener {
            showGoalDialog(goal1.but)
            return@setOnLongClickListener true
        }
        goal2.but.setOnLongClickListener {
            showGoalDialog(goal2.but)
            return@setOnLongClickListener true
        }
        goal3.but.setOnLongClickListener {
            showGoalDialog(goal3.but)
            return@setOnLongClickListener true
        }
        goal4.but.setOnLongClickListener {
            showGoalDialog(goal4.but)
            return@setOnLongClickListener true
        }

        goal1.progbar.setOnLongClickListener {
            updateGoalTime(goal1)
            return@setOnLongClickListener true
        }

        goal2.progbar.setOnLongClickListener {
            updateGoalTime(goal2)
            return@setOnLongClickListener true
        }

        goal3.progbar.setOnLongClickListener {
            updateGoalTime(goal3)
            return@setOnLongClickListener true
        }

        goal4.progbar.setOnLongClickListener {
            updateGoalTime(goal4)
            return@setOnLongClickListener true
        }
    }

    override fun onResume() {
        super.onResume()
        curGoal.progbar.progress = ((SystemClock.elapsedRealtime() - curGoal.chrono.base) / 1000).toInt()

    }
}

