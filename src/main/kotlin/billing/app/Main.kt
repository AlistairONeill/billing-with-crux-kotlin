package billing.app

import billing.adapter.CruxBillingSource
import billing.web.routes.billingRoutes
import crux.api.CruxK
import org.http4k.server.Netty
import org.http4k.server.asServer

fun main() {
    CruxK.startNode()
        .let(::CruxBillingSource)
        .let(::BillingApp)
        .let(::billingRoutes)
        .asServer(Netty(9000))
        .start()
}