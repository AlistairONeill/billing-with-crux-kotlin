package billing.adapter

import billing.adapter.CruxBillingItem.AMOUNT_KEY
import billing.adapter.CruxBillingItem.CLIENT_KEY
import billing.adapter.CruxBillingItem.DETAILS_KEY
import billing.adapter.CruxBillingItem.TAG_KEY
import billing.adapter.CruxBillingItem.TYPE_BILLING_ITEM
import billing.adapter.CruxBillingSource.Companion.TYPE_KEY
import billing.domain.model.BillingItem
import crux.api.CruxDocument
import crux.api.query.context.PullSpecContext
import crux.api.query.domain.PullSpec
import crux.api.underware.kw

object CruxBillingItem {
    const val TYPE_BILLING_ITEM = "billingItem"

    const val CLIENT_KEY = "client"
    const val AMOUNT_KEY = "amount"
    const val TAG_KEY = "tag"
    const val DETAILS_KEY = "details"
}

fun BillingItem.toCruxDocument(): CruxDocument =
    CruxDocument.build(id) { document ->
        document.put(TYPE_KEY, TYPE_BILLING_ITEM)
        document.put(CLIENT_KEY, client)
        document.put(AMOUNT_KEY, amount)
        document.put(TAG_KEY, tag)
        document.put(DETAILS_KEY, details)
    }

fun CruxDocument.toBillingItem() =
    BillingItem(
        id = id as String,
        client = get(CLIENT_KEY) as String,
        amount = get(AMOUNT_KEY) as Double,
        tag = get(TAG_KEY) as String,
        details = get(DETAILS_KEY) as String,
    )