package billing.web.lens

import billing.domain.model.*
import org.http4k.core.Request
import org.http4k.lens.Query

val clientLens = Query.map(::Client).optional("client")
val amountLens = Query.map { it.toDouble().let(::BillingAmount) }.optional("amount")
val tagLens = Query.map(::BillingItemTag).optional("tag")
val detailsLens = Query.map(::BillingItemDetails).optional("details")

fun billingItemCriteria(request: Request) =
    BillingItemCriteria(
        client = clientLens(request),
        amount = amountLens(request),
        tag = tagLens(request),
        details = detailsLens(request)
    )