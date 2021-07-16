package billing.json

import com.ubertob.kondor.json.JAny
import com.ubertob.kondor.json.jsonnode.JsonNodeObject
import com.ubertob.kondor.json.num
import com.ubertob.kondor.json.str
import billing.domain.model.BillingItem

object JBillingItem: JAny<BillingItem>() {
    private val client by str(BillingItem::client)
    private val amount by num(BillingItem::amount)
    private val tag by str(BillingItem::tag)
    private val details by str(BillingItem::details)

    override fun JsonNodeObject.deserializeOrThrow() =
        BillingItem(
            +client,
            +amount,
            +tag,
            +details
        )
}