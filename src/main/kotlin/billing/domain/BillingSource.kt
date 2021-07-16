package billing.domain

import billing.domain.model.BillingItem
import java.time.Instant

interface BillingSource {
    fun put(item: BillingItem)
    fun getAll(): Set<BillingItem>
}