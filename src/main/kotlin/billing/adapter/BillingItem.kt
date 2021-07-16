package billing.adapter

import billing.adapter.CruxBillingItem.AMOUNT_KEY
import billing.adapter.CruxBillingItem.CLIENT_KEY
import billing.adapter.CruxBillingItem.DETAILS_KEY
import billing.adapter.CruxBillingItem.TAG_KEY
import billing.adapter.CruxBillingItem.TYPE_BILLING_ITEM
import billing.adapter.CruxBillingSource.Companion.TYPE_KEY
import billing.domain.model.*
import crux.api.CruxDocument

object CruxBillingItem {
    const val TYPE_BILLING_ITEM = "billingItem"

    const val CLIENT_KEY = "client"
    const val AMOUNT_KEY = "amount"
    const val TAG_KEY = "tag"
    const val DETAILS_KEY = "details"
}

fun BillingItem.toCruxDocument(): CruxDocument =
    CruxDocument.build(id.value) { document ->
        document.put(TYPE_KEY, TYPE_BILLING_ITEM)
        document.put(CLIENT_KEY, client.value)
        document.put(AMOUNT_KEY, amount.value)
        document.put(TAG_KEY, tag.value)
        document.put(DETAILS_KEY, details.value)
    }

fun CruxDocument.toBillingItem() =
    BillingItem(
        id = BillingItemId(id as String),
        client = Client(get(CLIENT_KEY) as String),
        amount = BillingAmount(get(AMOUNT_KEY) as Double),
        tag = BillingItemTag(get(TAG_KEY) as String),
        details = BillingItemDetails(get(DETAILS_KEY) as String),
    )