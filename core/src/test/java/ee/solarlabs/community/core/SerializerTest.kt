package ee.solarlabs.community.core

import com.google.gson.GsonBuilder
import ee.solarlabs.community.core.model.Error
import org.junit.Test
import kotlin.test.assertEquals

class DeserializerTest {
    @Test
    fun testFailureSerialization() {
        val innerError = Error.InnerError(true, Error.InnerError.FailureReason("testing", 1))
        val gson = GsonBuilder().create()
        val jsonString = gson.toJson(innerError)
        assertEquals(
            "{\"error\":true,\"reason\":\"{\\\"message\\\":\\\"testing\\\",\\\"code\\\":1}\"}",
            jsonString
        )
    }
}