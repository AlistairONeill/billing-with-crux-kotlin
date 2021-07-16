package billing.web

import com.ubertob.kondor.json.JConverter
import org.http4k.core.Request

fun <T> Request.parseJsonBody(converter: JConverter<T>): T = bodyString().let(converter::fromJson).orThrow()

fun <T> Request.bodyAsJson(converter: JConverter<T>, item: T) =
    header("content-type", "application/json")
        .body(converter.toJson(item))