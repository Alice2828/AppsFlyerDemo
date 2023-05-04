package com.example.myapplicationAppsFlyerDemo

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.example.myapplicationAppsFLyerDemo.BuildConfig
import com.example.myapplicationAppsFLyerDemo.databinding.FragmentDeepLinkBinding
import com.example.myapplicationAppsFlyerDemo.handler.DeepLinkHandler
import com.example.myapplicationAppsFlyerDemo.handler.DeepLinkNavigator
import com.example.myapplicationAppsFlyerDemo.handler.EMPTY
import com.example.myapplicationAppsFlyerDemo.handler.main.DeepLinkProcessorsUtils
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.lang.ref.WeakReference

class DeepLinkFragment : Fragment(), DeepLinkNavigator {
    private lateinit var binding: FragmentDeepLinkBinding
    private val viewModel by viewModel<DeepLinkViewModel>()
    private val handler by lazy {
        DeepLinkHandler(DeepLinkProcessorsUtils.allDeepLinkProcessors, WeakReference(this))
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDeepLinkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.openDeepLink.observe(viewLifecycleOwner, EventObserver {
            val (link, campaignId) = it
            openDeepLink(link, campaignId)
        })
    }

    fun onNewIntent(intent: Intent?) {
        if (intent?.hasExtra(BuildConfig.DYNAMIC_LINK_ATTRS) == true){
            viewModel.handleAppsFlyerDeepLink(intent.getStringExtra(BuildConfig.DYNAMIC_LINK_ATTRS))
        }
    }

    private fun openDeepLink(deepLinkToOpen: String, campaignId: String) {
        if (handler.isDeepLinkExists(deepLinkToOpen)) {
            handler.process(deepLinkToOpen)
        }
    }

    override fun onDeepLinkNotProcessed(deepLink: String) {
//        val bundle = bundleOf(
//            ARGConstant.ARG_NOTIFICATION_WEB_LINK to deepLink.getLinkWithDomain()
//        )
//        showHomeWebDialog(bundle)
    }

    override fun openPage(activityPath: String, args: Bundle, requiresAuth: Boolean) {
        when {
            activityPath == "com.example.myapplicationAppsFlyerDemo.MainActivity" && activity?.getCurrentActivityPath() != "com.example.myapplicationAppsFlyerDemo.MainActivity" -> {
                activity?.authShowModuleActivity(
                    path = activityPath,
                    args = args,
                    isAuth = requiresAuth
                )
            }
            activityPath == "com.example.myapplicationAppsFlyerDemo.MainActivity" -> {
                //call(EventConstant.UPDATE_MAIN_SCREEN_TAB, args)
            }
            else -> {
                activity?.authShowModuleActivity(
                    path = activityPath,
                    args = args,
                    isAuth = requiresAuth
                )
            }
        }
    }
}

fun Activity.showActivityAndClearBackStack(
    activity: Activity?,
    args: Bundle? = null
) {
    if (activity == null) throw Throwable("Укажите activity")
    startActivity(Intent(this, activity::class.java).apply {
        args?.let {
            putExtras(it)
        }
    })
    ActivityCompat.finishAffinity(this)
}

fun Activity?.getCurrentActivityPath(): String {
    return try {
        val am = this?.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val taskInfo = am.getRunningTasks(1)

        taskInfo.getOrNull(0)?.topActivity?.className.orEmpty()
    } catch (e: Exception) {
        EMPTY
    }
}

fun Activity.authShowModuleActivity(
    path: String,
    args: Bundle? = null,
    isAuth: Boolean = false,
    isClearBackStack: Boolean = false
) {

    try {
        val intent = Intent(this, Class.forName(path))
        args?.let {
            intent.putExtras(it)
        }
        startActivity(intent)
        if (isClearBackStack) {
            ActivityCompat.finishAffinity(this)
        }
    } catch (e: ClassNotFoundException) {
        e.printStackTrace()
    }
}