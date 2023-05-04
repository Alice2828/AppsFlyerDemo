package com.example.myapplicationAppsFlyerDemo.emptyHostActivity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.myapplicationAppsFLyerDemo.R
import com.example.myapplicationAppsFLyerDemo.databinding.FragmentEmptyBinding
import com.example.myapplicationAppsFlyerDemo.handler.ARG_DEEP_LINK_DATA
import com.example.myapplicationAppsFlyerDemo.handler.DeepLinkNavigator
import com.example.myapplicationAppsFlyerDemo.handler.DeepLinkType
import com.example.myapplicationAppsFlyerDemo.handler.UIDeepLink
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class EmptyHostGlobalFragment : Fragment() {
    private lateinit var binding: FragmentEmptyBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleDeepLinkData()
    }

    private fun handleDeepLinkData() {
        deepLinkData?.let {
            when (it.type) {
                DeepLinkType.DEEP_LINK_TYPE_MFS_TOP_UP -> {
                    ReloadBalanceGlobalDialog().show(childFragmentManager)
                }
                else -> {
                }
            }
        }
    }
}


class ReloadBalanceGlobalDialog : BottomSheetDialogFragment(),
    DeepLinkNavigator {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_empty, container, false)
    }

    override fun onDeepLinkNotProcessed(deepLink: String) {
        TODO("Not yet implemented")
    }

    override fun openPage(activityPath: String, args: Bundle, requiresAuth: Boolean) {
        TODO("Not yet implemented")
    }
}

val Fragment.deepLinkData: UIDeepLink?
    get() = (activity?.intent?.extras?.getSerializable(ARG_DEEP_LINK_DATA) as? UIDeepLink)
        ?: arguments?.getSerializable(ARG_DEEP_LINK_DATA) as? UIDeepLink

fun DialogFragment.show(fm : FragmentManager?) : DialogFragment {
    this.show(fm ?: throw NullPointerException("supportFragmentManager null"), tag)
    return this
}