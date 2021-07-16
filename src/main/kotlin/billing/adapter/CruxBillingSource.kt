package billing.adapter

import billing.adapter.CruxBillingItem.TYPE_BILLING_ITEM
import billing.domain.BillingSource
import billing.domain.model.BillingItem
import clojure.lang.IPersistentMap
import crux.api.CruxDocument
import crux.api.ICruxAPI
import crux.api.query.conversion.q
import crux.api.tx.submitTx
import crux.api.underware.kw
import crux.api.underware.sym

class CruxBillingSource(private val crux: ICruxAPI): BillingSource {
    companion object {
        const val TYPE_KEY = "type"

        private val item = "item".sym
        private val type = TYPE_KEY.kw
    }

    override fun put(item: BillingItem) {
        crux.submitTx {
            put(item.toCruxDocument())
        }
    }

    override fun getAll(): Set<BillingItem> =
        crux.db().q {
            find {
                pullAll(item)
            }

            where {
                item has type eq TYPE_BILLING_ITEM
            }
        }.map {
            val map = it.single() as IPersistentMap
            map.let(CruxDocument::factory).toBillingItem()
        }.toSet()
}