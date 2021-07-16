package billing.web.routes

import billing.app.BillingApp
import billing.json.JBillingStats
import billing.web.lens.*
import billing.web.routes.BillingRoutes.API_BILLING_STATS
import billing.web.toOkResponse
import org.http4k.contract.ContractRoute
import org.http4k.contract.meta
import org.http4k.core.ContentType.Companion.APPLICATION_JSON
import org.http4k.core.Method.GET

fun getBillingStatsRoute(billingApp: BillingApp): ContractRoute =
    API_BILLING_STATS meta {
        summary = "gets billing stats"
        description = "gets the count, total and mean of billing items that match your criteria"
        produces += APPLICATION_JSON
        queries += clientLens
        queries += amountLens
        queries += tagLens
        queries += detailsLens
    } bindContract GET to { request ->
        billingItemCriteria(request)
            .let(billingApp::getStats)
            .toOkResponse(JBillingStats)
    }