package billing.json

import billing.domain.model.BillingAmount
import billing.domain.model.BillingStats
import com.ubertob.kondor.json.JAny
import com.ubertob.kondor.json.jsonnode.JsonNodeObject
import com.ubertob.kondor.json.num

object JBillingStats: JAny<BillingStats>() {
    private val itemCount by num(BillingStats::itemCount)
    private val total by num(BillingStats::total)
    private val mean by num(BillingStats::mean)

    override fun JsonNodeObject.deserializeOrThrow() =
        BillingStats(
            +itemCount,
            BillingAmount(+total),
            BillingAmount(+mean)
        )
}