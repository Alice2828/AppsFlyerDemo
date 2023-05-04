package com.example.myapplicationAppsFlyerDemo.handler.main

import com.example.myapplicationAppsFlyerDemo.handler.DeepLinkProcessor

class DeepLinkProcessorsUtils {
    companion object {
        val allDeepLinkProcessors =
            mutableListOf<DeepLinkProcessor>().apply {
                add(DeepLinkTopUpDialogProcessor())
            }.toList()
    }
}