package com.example.myapplicationAppsFlyerDemo.emptyHostActivity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplicationAppsFLyerDemo.databinding.ActivityEmptyBinding

class EmptyHostGlobalActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEmptyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEmptyBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}