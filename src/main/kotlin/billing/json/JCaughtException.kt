package billing.json

import billing.web.filters.CaughtException
import com.ubertob.kondor.json.JAny
import com.ubertob.kondor.json.jsonnode.JsonNodeObject
import com.ubertob.kondor.json.str

object JCaughtException: JAny<CaughtException>() {
    private val type by str(CaughtException::type)
    private val message by str(CaughtException::message)

    override fun JsonNodeObject.deserializeOrThrow() =
        CaughtException(
            +type,
            +message
        )
}