package billing.web

import org.http4k.core.Response

fun Response.setBodyString(value: String) =
    header("content-type", "text/plain")
        .body(value)