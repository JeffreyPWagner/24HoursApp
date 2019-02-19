package me.jeffreywagner.a24hours

import android.widget.Button
import android.widget.Chronometer
import android.widget.ProgressBar
import android.widget.TextView

class Goal {
    lateinit var chrono: Chronometer
    lateinit var but: Button
    lateinit var progbar: ProgressBar
    lateinit var goalText: TextView
    var baseTime = 0L
    var goalTime = 3600
}