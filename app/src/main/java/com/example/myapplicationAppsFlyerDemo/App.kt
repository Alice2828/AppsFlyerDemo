package com.example.myapplicationAppsFlyerDemo

import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDexApplication
import com.appsflyer.AppsFlyerLib
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        startKoin {
            androidLogger()
            androidContext(applicationContext)
            //modules(getDiModules() as ArrayList<Module>)
        }
      //  AppsFlyerLib.getInstance().start(this, applicationContext.getString(R.string.apps_flyer_key))
      //  subscribeForDeepLink()
    }

//    private fun getDiModules(): List<Module> {
//       // return
//    }
}