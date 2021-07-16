package billing.domain

import billing.domain.model.BillingItem
import billing.domain.model.BillingItemCriteria
import billing.domain.model.BillingItemId
import billing.domain.model.BillingStats

interface BillingSource {
    fun put(item: BillingItem)
    fun getMatching(criteria: BillingItemCriteria): Set<BillingItem>
    fun getStats(criteria: BillingItemCriteria): BillingStats
    fun delete(id: BillingItemId)
    operator fun get(id: BillingItemId): BillingItem?
}