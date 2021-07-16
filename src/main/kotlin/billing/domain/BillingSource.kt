package billing.domain

import billing.domain.model.BillingItem

interface BillingSource {
    fun put(item: BillingItem)
    fun getAll(): Set<BillingItem>
}