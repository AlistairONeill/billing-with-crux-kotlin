package billing.domain

import billing.domain.model.*
import billing.domain.model.BillingStats.Companion.Empty

class StubBillingSource: BillingSource {
    private val items = mutableMapOf<BillingItemId, BillingItem>()

    override fun put(item: BillingItem) {
        items[item.id] = item
    }

    override fun get(id: BillingItemId) = items[id]

    override fun getMatching(criteria: BillingItemCriteria): Set<BillingItem> =
        items.values
            .filter(criteria::matches)
            .toSet()

    override fun getStats(criteria: BillingItemCriteria): BillingStats {
        val matching = getMatching(criteria)
        if (matching.isEmpty()) {
            return Empty
        }

        val itemCount = matching.size.toLong()
        val total = matching.sumOf { it.amount.value }
        val mean = total / itemCount

        return BillingStats(
            itemCount,
            BillingAmount(total),
            BillingAmount(mean)
        )
    }
}