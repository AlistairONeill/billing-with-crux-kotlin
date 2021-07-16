package billing.web

import com.ubertob.kondor.json.JConverter
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.Status.Companion.OK

fun Response.setBodyString(value: String) =
    header("content-type", "text/plain")
        .body(value)

fun <T> T.toOkResponse(converter: JConverter<T>): Response = toResponse(OK, converter)

fun <T> T.toResponse(status: Status, converter: JConverter<T>): Response =
    Response(status)
        .header("content-type", "application/json")
        .body(converter.toJson(this))

fun <T> Response.parseJsonBody(converter: JConverter<T>): T =
    bodyString()
        .let(converter::fromJson)
        .orThrow()
