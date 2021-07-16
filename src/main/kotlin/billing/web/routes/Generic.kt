package billing.web.routes

import billing.web.routes.BillingRoutes.PING
import billing.web.setBodyString
import org.http4k.contract.meta
import org.http4k.core.Method.GET
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK

fun pingRoute() =
    PING meta {
        summary = "ping"
        description = "returns pong"
        returning(OK to "pong")
    } bindContract GET to {
        Response(OK).setBodyString("pong")
    }