package billing.adapter

import billing.adapter.CruxBillingItem.TYPE_BILLING_ITEM
import billing.adapter.CruxBillingItem.amount
import billing.adapter.CruxBillingItem.client
import billing.adapter.CruxBillingItem.details
import billing.adapter.CruxBillingItem.tag
import billing.domain.BillingSource
import billing.domain.model.BillingItem
import billing.domain.model.BillingItemCriteria
import billing.domain.model.BillingItemId
import clojure.lang.IPersistentMap
import clojure.lang.Symbol
import crux.api.CruxDocument
import crux.api.ICruxAPI
import crux.api.query.context.WhereContext
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

    override fun get(id: BillingItemId): BillingItem? =
        crux.db()
            .entity(id.value)
            ?.toBillingItem()

    override fun getMatching(criteria: BillingItemCriteria): Set<BillingItem> =
        crux.db().q {
            find {
                pullAll(item)
            }

            where {
                item has type eq TYPE_BILLING_ITEM

                applyCriteriaFilter(item, criteria)
            }
        }.map {
            val map = it.single() as IPersistentMap
            map.let(CruxDocument::factory).toBillingItem()
        }.toSet()

    private fun WhereContext.applyCriteriaFilter(item: Symbol, criteria: BillingItemCriteria) {
        criteria.amount?.let { item has amount eq it.value }
        criteria.client?.let { item has client eq it.value }
        criteria.tag?.let { item has tag eq it.value }
        criteria.details?.let { item has details eq it.value }
    }
}