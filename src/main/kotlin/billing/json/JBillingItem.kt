package billing.json

import billing.domain.model.*
import com.ubertob.kondor.json.JAny
import com.ubertob.kondor.json.jsonnode.JsonNodeObject
import com.ubertob.kondor.json.num

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