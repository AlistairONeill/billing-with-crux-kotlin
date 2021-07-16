package billing.domain

import billing.domain.model.BillingItem
import billing.domain.model.BillingItemCriteria
import billing.domain.model.BillingItemId

interface BillingSource {
    fun put(item: BillingItem)
    fun getMatching(criteria: BillingItemCriteria): Set<BillingItem>
    operator fun get(id: BillingItemId): BillingItem?
}