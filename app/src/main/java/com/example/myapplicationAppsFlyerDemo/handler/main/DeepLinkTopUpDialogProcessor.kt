package com.example.myapplicationAppsFlyerDemo.handler.main

import com.example.myapplicationAppsFlyerDemo.handler.DeepLinkProcessor
import com.example.myapplicationAppsFlyerDemo.handler.DeepLinkType

class DeepLinkTopUpDialogProcessor : DeepLinkProcessor {

    override fun matches(path: String, queryMap: HashMap<String, String>) =
        path.containsAll(KEY_FINANCE, KEY_TOP_UP)

    override val isDialog = true

    override val pathToActivity = "com.example.myapplicationAppsFlyerDemo.emptyHostActivity.EmptyHostGlobalActivity"

    override val type = DeepLinkType.DEEP_LINK_TYPE_MFS_TOP_UP
}

fun String.containsAll(vararg list: String): Boolean {
    var containsCounter = 0
    list.forEach {
        if (this.contains(it)) containsCounter++
    }
    return containsCounter == list.size
}

const val KEY_FINANCE = "finance"
const val KEY_TOP_UP = "top-up"
