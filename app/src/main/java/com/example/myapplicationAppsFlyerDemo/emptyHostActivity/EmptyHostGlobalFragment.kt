package com.example.myapplicationAppsFlyerDemo.emptyHostActivity

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.myapplicationAppsFlyerDemo.R
import com.example.myapplicationAppsFlyerDemo.databinding.FragmentDeepLinkBinding
import com.example.myapplicationAppsFlyerDemo.databinding.FragmentEmptyBinding
import com.example.myapplicationAppsFlyerDemo.databinding.FragmentReloadBinding
import com.example.myapplicationAppsFlyerDemo.authShowModuleActivity
import com.example.myapplicationAppsFlyerDemo.handler.ARG_DEEP_LINK_DATA
import com.example.myapplicationAppsFlyerDemo.handler.DeepLinkNavigator
import com.example.myapplicationAppsFlyerDemo.handler.DeepLinkType
import com.example.myapplicationAppsFlyerDemo.handler.UIDeepLink
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class EmptyHostGlobalFragment : Fragment() {
    private lateinit var binding: FragmentEmptyBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEmptyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /**
         * Скрываем рутовый лаяут для диалога
         */
        if (deepLinkData?.isDialog == true) {
            view?.visibility = View.GONE
        }
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
    override fun getTheme() = R.style.TransparentBottomSheetDialog
    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        if (deepLinkData?.isDialog == true) {
            activity?.finish()
        }
    }

    fun addViewTreeObserverGlobalLayoutListener(block: () -> Unit) {
        view?.viewTreeObserver?.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                block()
                view?.viewTreeObserver?.removeOnGlobalLayoutListener(this)
            }
        })
    }

    override fun onCreateDialog(savedInstanceState: Bundle?) =
        showFullScreen(super.onCreateDialog(savedInstanceState))

    private lateinit var binding: FragmentReloadBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReloadBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDeepLinkNotProcessed(deepLink: String) {
        TODO("Not yet implemented")
    }


    override fun openPage(activityPath: String, args: Bundle, requiresAuth: Boolean) {
        activity?.authShowModuleActivity(activityPath, args, requiresAuth)
    }
}

val Fragment.deepLinkData: UIDeepLink?
    get() = (activity?.intent?.extras?.getSerializable(ARG_DEEP_LINK_DATA) as? UIDeepLink)
        ?: arguments?.getSerializable(ARG_DEEP_LINK_DATA) as? UIDeepLink

fun DialogFragment.show(fm: FragmentManager?): DialogFragment {
    this.show(fm ?: throw NullPointerException("supportFragmentManager null"), tag)
    return this
}

fun BottomSheetDialogFragment.showFullScreen(dialog: Dialog): Dialog {
    dialog.setOnShowListener { dialogInterface ->
        val bottomSheetDialog = dialogInterface as BottomSheetDialog
        val bottomSheet =
            bottomSheetDialog.findViewById<View>(R.id.design_bottom_sheet) as FrameLayout
        val behavior: BottomSheetBehavior<*> = BottomSheetBehavior.from(bottomSheet)
        val layoutParams = bottomSheet.layoutParams
        val displayMetrics = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels
        if (layoutParams != null) {
            layoutParams.height = height
        }
        bottomSheet.layoutParams = layoutParams
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }
    return dialog
}