package ee.solarlabs.community.core.util

import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import ee.solarlabs.community.core.model.Error
import java.lang.reflect.Type


class FailureReasonSerializer : JsonSerializer<Error.InnerError.FailureReason> {
    override fun serialize(
        src: Error.InnerError.FailureReason,
        typeOfSrc: Type,
        context: JsonSerializationContext
    ): JsonElement {
        return JsonPrimitive(context.serialize(src, typeOfSrc).toString())
    }
}
