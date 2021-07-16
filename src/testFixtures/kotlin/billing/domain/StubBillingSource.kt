package billing.domain

import billing.domain.model.BillingItem
import billing.domain.model.BillingItemId

class StubBillingSource: BillingSource {
    private val items = mutableMapOf<BillingItemId, BillingItem>()

    override fun put(item: BillingItem) {
        items[item.id] = item
    }

    override fun get(id: BillingItemId) = items[id]

    override fun getAll(): Set<BillingItem> = items.values.toSet()
}