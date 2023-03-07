package com.example.calc_code.utilits

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import java.math.RoundingMode
import java.text.DecimalFormat

fun AppCompatActivity.resultController(calcResult : Double) : String{
    return if (calcResult % 1 == 0.0) {
        calcResult.toInt().toString()
    } else {
        //return calcResult.toString()
        val df = DecimalFormat("#.##########")
        df.roundingMode = RoundingMode.UP
        df.format(calcResult)
    }
}