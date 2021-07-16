package billing.domain

import billing.domain.model.BillingItem

class StubBillingSource: BillingSource {
    private val items = mutableSetOf<BillingItem>()

    override fun put(item: BillingItem) {
        items.add(item)
    }

    override fun getAll(): Set<BillingItem> = items
}