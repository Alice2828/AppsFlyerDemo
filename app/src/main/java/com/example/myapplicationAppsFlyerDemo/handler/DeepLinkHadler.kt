package com.example.myapplicationAppsFlyerDemo.handler

import android.os.Bundle
import android.util.Log
import java.io.Serializable
import java.lang.ref.WeakReference
import java.net.URLDecoder

class DeepLinkHandler(
    private val processors: List<DeepLinkProcessor>,
    private val navigator: WeakReference<DeepLinkNavigator>
) {

    /**
     * Принимает ссылку без base url
     * CampaignId от уведомлений для AdTarget
     */
    fun process(deepLinkPathWithQuery: String, campaignId: String = "") {
        val cleanedLangPrefixPath = deepLinkPathWithQuery.deeplinkRemoveLangPrefix()
        var cleanPath = cleanedLangPrefixPath.removePrefix("/")
        val queryMap = cleanPath.queryMap()
        val queryString = queryMap.joinToQueryString("&", "?")
        cleanPath =
            if (cleanPath.contains(KEY_SHOP)) cleanPath else cleanPath.removeSuffix(queryString)

        processors.forEach {
            if (it.matches(cleanPath, queryMap)) {
                val deepLinkData =
                    UIDeepLink(
                        isAuth = it.requiresAuth,
                        isDialog = it.isDialog,
                        type = it.type,
                        tabIndex = it.tabIndex,
                        pathName = cleanPath,
                        queryMap = queryMap,
                        pathMap = it.pathParams,
                        additionalData = campaignId
                    )
                val args = Bundle().apply {
                    putSerializable(ARG_DEEP_LINK_DATA, deepLinkData)
                }
                navigator.get()?.openPage(it.pathToActivity, args, deepLinkData.isAuth)
                Log.d(
                    "Deeplink",
                    "Deeplink processor: ${it.javaClass.name} - $cleanPath - $deepLinkData"
                )
                return
            }
        }

        navigator.get()?.onDeepLinkNotProcessed(deepLinkPathWithQuery)
    }

    /*
    * Проверяем диплинк требует авторизацию или нет
    */
    fun checkDeepLinkNeedsAuth(deepLinkPathWithQuery: String, campaignId: String = ""): Boolean {
        val cleanedLangPrefixPath = deepLinkPathWithQuery.deeplinkRemoveLangPrefix()
        var cleanPath = cleanedLangPrefixPath.removePrefix("/")
        val queryMap = cleanPath.queryMap()
        val queryString = queryMap.joinToQueryString("&", "?")
        cleanPath = cleanPath.removeSuffix(queryString)

        processors.forEach {
            if (it.matches(cleanPath, queryMap)) {
                return it.requiresAuth
            }
        }

        return false
    }

    /*
    * Проверяем есть ли процессор для диплинка
    */
    fun isDeepLinkExists(deepLinkPathWithQuery: String, campaignId: String = ""): Boolean {
        val cleanedLangPrefixPath = deepLinkPathWithQuery.deeplinkRemoveLangPrefix()
        var cleanPath = cleanedLangPrefixPath.removePrefix("/")
        val queryMap = cleanPath.queryMap()
        val queryString = queryMap.joinToQueryString("&", "?")
        cleanPath = cleanPath.removeSuffix(queryString)

        var count = 0
        processors.forEach {
            if (it.matches(cleanPath, queryMap)) {
                count++
            }
        }

        return count != 0
    }

    fun onDestroy() {
        navigator.clear()
    }
}

interface DeepLinkProcessor {

    fun matches(path: String, queryMap: HashMap<String, String>): Boolean

    val isDialog: Boolean
        get() = false

    val type: DeepLinkType
        get() = DeepLinkType.None

    val requiresAuth: Boolean
        get() = false

    val pathToActivity: String

    val tabIndex: Int
        get() = 0

    val pathParams: HashMap<String, String>
        get() = hashMapOf()
}


interface DeepLinkNavigator {

    fun onDeepLinkNotProcessed(deepLink: String)

    fun openPage(activityPath: String, args: Bundle, requiresAuth: Boolean)

}

enum class DeepLinkType {
    None,
    DEEP_LINK_TYPE_MFS_TOP_UP
}

fun String.deeplinkRemoveLangPrefix(): String {
    if (this.containsAny(PREFIX_LANG_EN, PREFIX_LANG_RU, PREFIX_LANG_KZ, PREFIX_LANG_KK)) {
        return this.removeRange(IntRange(0, 2))
    }
    if (this.containsAny(
            PREFIX_LANG_EN_ADDITIONAL,
            PREFIX_LANG_RU_ADDITIONAL,
            PREFIX_LANG_KZ_ADDITIONAL,
            PREFIX_LANG_KK_ADDITIONAL
        )
    ) {
        return this.removeRange(IntRange(0, 1))
    }
    return this
}

fun String.containsAny(vararg list: String): Boolean {
    list.forEach {
        if (this.contains(it)) return true
    }
    return false
}

const val PREFIX_LANG_EN = "/en"
const val PREFIX_LANG_EN_ADDITIONAL = "en/"
const val PREFIX_LANG_RU = "/ru"
const val PREFIX_LANG_RU_ADDITIONAL = "ru/"
const val PREFIX_LANG_KZ = "/kz"
const val PREFIX_LANG_KK = "/kk"
const val PREFIX_LANG_KZ_ADDITIONAL = "kz/"
const val PREFIX_LANG_KK_ADDITIONAL = "kk/"

fun String.queryMap(): HashMap<String, String> {
    val pairs = this.substringAfter("?").split("&".toRegex())
    val resultQueryParamMap = LinkedHashMap<String, String>()
    for (pair in pairs) {
        try {
            val index = pair.indexOf("=")
            val decodedQueryKey = URLDecoder.decode(pair.substring(0, index), "UTF-8")
            val decodedQueryValue = URLDecoder.decode(pair.substring(index + 1), "UTF-8")
            resultQueryParamMap[decodedQueryKey] = decodedQueryValue
        } catch (e: Exception) {
            return hashMapOf()
        }
    }
    return resultQueryParamMap
}

fun HashMap<String, String>.joinToQueryString(separator: CharSequence = ", ", prefix: CharSequence = ""): String {
    return if (!this.isNullOrEmpty()) {
        this.map {
            it.key + "=" + it.value
        }.joinToString(
            separator = separator,
            prefix = prefix
        )
    } else {
        EMPTY
    }
}

const val EMPTY = ""
const val KEY_SHOP = "shop"
const val ARG_DEEP_LINK_DATA = "ARG_DEEP_LINK_DATA"

class UIDeepLink(
    val isAuth : Boolean,
    val isDialog : Boolean,
    val type : DeepLinkType,
    val tabIndex : Int,
    val pathName: String,
    val queryMap : HashMap<String, String>,
    val pathMap : HashMap<String, String>,
    val additionalData: String = EMPTY
) : Serializable

