package billing.adapter

import billing.domain.BillingSource
import billing.domain.model.BillingItem
import crux.api.ICruxAPI

class CruxBillingSource(private val crux: ICruxAPI): BillingSource {
    override fun put(item: BillingItem) {
        TODO("Not yet implemented")
    }

    override fun getAll(): Set<BillingItem> {
        TODO("Not yet implemented")
    }
}