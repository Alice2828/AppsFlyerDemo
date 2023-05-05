package com.example.myapplicationAppsFlyerDemo.emptyHostActivity

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplicationAppsFLyerDemo.R
import com.example.myapplicationAppsFLyerDemo.databinding.ActivityEmptyBinding
import com.example.myapplicationAppsFlyerDemo.handler.ARG_DEEP_LINK_DATA
import com.example.myapplicationAppsFlyerDemo.handler.UIDeepLink

class EmptyHostGlobalActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEmptyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupTheme()
        binding = ActivityEmptyBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setupTheme() {
        /**
         * Устанавливаем прозрачное активити c windowIsFloating параметром в случае если нужно открыть диалог
         * [TransparentActivity] для прозраного активити с диалогами, параметр windowIsFloating включен
         * [TransparentWindowFloatingDisabled] для активити с прозразным бэкгрундом без параметра windowIsFloating
         * [LocalAppTheme] обычная тема
         */
        val isDialog = deepLinkData?.isDialog ?: false
        if (isDialog) {
            setTheme(R.style.TransparentActivity)
        } else {
            setTheme(R.style.LocalAppTheme)
        }
    }
}

val Activity.deepLinkData: UIDeepLink?
    get() = intent.extras?.getSerializable(ARG_DEEP_LINK_DATA) as? UIDeepLink
