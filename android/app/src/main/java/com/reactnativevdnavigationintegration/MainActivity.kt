package com.reactnativevdnavigationintegration

import android.content.Intent
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import com.facebook.react.ReactActivity
import com.facebook.react.ReactActivityDelegate
import com.facebook.react.defaults.DefaultNewArchitectureEntryPoint.fabricEnabled
import com.facebook.react.defaults.DefaultReactActivityDelegate
import com.getpoi.android_vd_nav_ui.view.PoiVdNavigationActivity
import com.poilabs.vd.nav.non.ui.jsonclient.VDResponseListener
import com.poilabs.vd.nav.non.ui.models.PoiManager

class MainActivity : ReactActivity() {
    public fun startemre() {
        PoiManager.init(
            context = this,
            appId = "c135bcd1-b23f-4097-941e-65d168b37d66",
            secret = "fc25ab28-4f4a-4cf3-9103-538d7b90b594",
            language = "localeLanguage",
            title = "HAYAL ORTAĞIM",
            vdResponseListener = object : VDResponseListener {
                override fun onSuccess() {
                    Intent(baseContext, PoiVdNavigationActivity::class.java).also {
                        startActivity(it)
                    }
                }

                override fun onFail(throwable: Throwable?) {
                    Toast.makeText(baseContext, throwable?.message, Toast.LENGTH_SHORT)
                        .show()
                }
            })
    }


  /**
   * Returns the name of the main component registered from JavaScript. This is used to schedule
   * rendering of the component.
   */
  override fun getMainComponentName(): String = "reactNativeVdNavigationIntegration"

  /**
   * Returns the instance of the [ReactActivityDelegate]. We use [DefaultReactActivityDelegate]
   * which allows you to enable New Architecture with a single boolean flags [fabricEnabled]
   */
  override fun createReactActivityDelegate(): ReactActivityDelegate =
      DefaultReactActivityDelegate(this, mainComponentName, fabricEnabled)
}
