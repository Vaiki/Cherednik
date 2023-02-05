package com.vaiki.fintechmvvm.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.vaiki.fintechmvvm.R
import com.vaiki.fintechmvvm.util.makeStatusBarTransparent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        makeStatusBarTransparent()
    }
}