package com.antv.mock.ui.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.antv.mock.R

val TAG = "AnTVLog"

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.setTitle(R.string.header)


    }



}