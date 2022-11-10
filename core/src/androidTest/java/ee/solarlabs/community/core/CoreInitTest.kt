package ee.solarlabs.community.core

import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Test

class CoreInitTest {

    @Test
    fun testCoreInit() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext
        SolarCommunityCore.init(context, true)
    }
    /*

        @Test
        fun testApi() = testApplication {
            val context = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext
            SolarCommunityCore.init(context, false)
            application {
                configureSockets()
                configureRouting()
                configureSerialization()
                configureMonitoring()
            }
        }
    */

}