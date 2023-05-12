package com.example.myapplicationAppsFlyerDemo.handler.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.example.myapplicationAppsFlyerDemo.R
import com.example.myapplicationAppsFlyerDemo.databinding.ActivityDeppLinkBinding
import com.example.myapplicationAppsFlyerDemo.DeepLinkFragment

class DeepLinkActivity: AppCompatActivity() {

    private lateinit var binding: ActivityDeppLinkBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDeppLinkBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleIntent(intent)
    }
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(localIntent: Intent?) {
        val deepLinkFragment = supportFragmentManager.findFragmentById(R.id.navDeepLinkFragment)
        val navigationController = deepLinkFragment?.findNavController()

        if (navigationController?.currentDestination?.id == R.id.deepLinkFragment) {
            (deepLinkFragment.childFragmentManager.fragments[0] as DeepLinkFragment).onNewIntent(localIntent)
        }
    }
}