package com.reactnativevdnavigationintegration

import android.content.Intent
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.getpoi.android_vd_nav_ui.view.PoiVdNavigationActivity
import com.poilabs.vd.nav.non.ui.jsonclient.VDResponseListener
import com.poilabs.vd.nav.non.ui.models.PoiManager

class PoilabsNavigationBridge internal constructor(context: ReactApplicationContext?) :
    ReactContextBaseJavaModule(context) {
    override fun getName(): String {
        return "PoilabsNavigationBridge"
    }
    @ReactMethod
    fun showPoilabsVdNavigation() {
        currentActivity?.let { activity ->
            PoiManager.init(
                context = activity.baseContext,
                appId = "APPLICATION_ID",
                secret = "APPLICATION_SECRET_KEY",
                language = "en",
                title = "PLACE_TITLE",
                vdResponseListener = object : VDResponseListener {
                    override fun onSuccess() {
                        Intent(activity.applicationContext, PoiVdNavigationActivity::class.java).also {
                            activity.startActivity(it)
                        }
                    }
                    override fun onFail(throwable: Throwable?) {

                    }
                })
        }
    }
}
