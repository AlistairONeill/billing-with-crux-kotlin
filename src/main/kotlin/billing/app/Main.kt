package billing.app

import billing.adapter.CruxBillingSource
import billing.web.routes.billingRoutes
import crux.api.CruxK
import org.http4k.server.Netty
import org.http4k.server.asServer

fun main() {
    val crux = CruxK.startNode()
    val billingSource = CruxBillingSource(crux)

    billingRoutes(billingSource)
        .asServer(Netty(9000))
        .start()
}