package com.example.myapplicationAppsFlyerDemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplicationAppsFLyerDemo.databinding.ActivityLauncherBinding

class LauncherActivity: AppCompatActivity() {

    private lateinit var binding: ActivityLauncherBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLauncherBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showActivityAndClearBackStack(MainActivity())
    }
}