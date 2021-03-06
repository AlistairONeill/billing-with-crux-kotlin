package billing.json

import billing.domain.model.*
import com.ubertob.kondor.json.JAny
import com.ubertob.kondor.json.jsonnode.JsonNodeObject

object JBillingItem: JAny<BillingItem>() {
    private val id by str(BillingItem::id)
    private val client by str(BillingItem::client)
    private val amount by num(BillingItem::amount)
    private val tag by str(BillingItem::tag)
    private val details by str(BillingItem::details)

    override fun JsonNodeObject.deserializeOrThrow() =
        BillingItem(
            BillingItemId(+id),
            Client(+client),
            BillingAmount(+amount),
            BillingItemTag(+tag),
            BillingItemDetails(+details)
        )
}

object JNewBillingItem: JAny<NewBillingItem>() {
    private val client by str(NewBillingItem::client)
    private val amount by num(NewBillingItem::amount)
    private val tag by str(NewBillingItem::tag)
    private val details by str(NewBillingItem::details)

    override fun JsonNodeObject.deserializeOrThrow() =
        NewBillingItem(
            Client(+client),
            BillingAmount(+amount),
            BillingItemTag(+tag),
            BillingItemDetails(+details)
        )
}