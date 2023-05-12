package com.example.myapplicationAppsFlyerDemo

import android.content.Intent
import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDexApplication
import com.appsflyer.AppsFlyerLib
import com.example.myapplicationAppsFlyerDemo.BuildConfig
import com.example.myapplicationAppsFlyerDemo.di.getAppDIModule
import com.example.myapplicationAppsFlyerDemo.handler.main.DeepLinkActivity
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.module.Module

class App : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        subscribeForDeepLink()
        AppsFlyerLib.getInstance().setDebugLog(true)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        startKoin {
            androidLogger()
            androidContext(applicationContext)
            modules(getDiModules() as ArrayList<Module>)
        }
        AppsFlyerLib.getInstance().start(this, "3uLHgiJvdx27PrYg6RCjRh")
    }

    private fun subscribeForDeepLink() {
        AppsFlyerLib.getInstance().subscribeForDeepLink { deepLinkResult ->
            try {
                val intent = Intent(applicationContext, DeepLinkActivity::class.java)
                if (deepLinkResult.deepLink != null) {
                    val objToStr: String = Gson().toJson(deepLinkResult.deepLink)
                    intent.putExtra(BuildConfig.DYNAMIC_LINK_ATTRS, objToStr)
                }
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
            }
        }
    }
    private fun getDiModules(): List<Module> {
        return getAppDIModule()
    }
}