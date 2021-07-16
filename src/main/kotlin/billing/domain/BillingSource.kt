package billing.domain

import billing.domain.model.BillingItem
import billing.domain.model.BillingItemId

interface BillingSource {
    fun put(item: BillingItem)
    fun getAll(): Set<BillingItem>
    operator fun get(id: BillingItemId): BillingItem?
}