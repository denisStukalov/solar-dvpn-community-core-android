package ee.solarlabs.ui

import android.app.Application
import ee.solarlabs.community.core.SolarCommunityCore

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        SolarCommunityCore.init(this@App, true)
    }
}