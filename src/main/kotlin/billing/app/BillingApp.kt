package billing.app

import billing.domain.BillingSource
import billing.domain.model.BillingItem
import billing.domain.model.BillingItemCriteria
import billing.domain.model.BillingItemId
import billing.domain.model.NewBillingItem

class BillingApp(
    private val billingSource: BillingSource
) {
    fun add(newBillingItem: NewBillingItem) =
        BillingItem(
            BillingItemId.mint(),
            newBillingItem.client,
            newBillingItem.amount,
            newBillingItem.tag,
            newBillingItem.details
        ).also(billingSource::put)

    fun getMatching(criteria: BillingItemCriteria) = billingSource.getMatching(criteria)

    fun getStats(criteria: BillingItemCriteria) = billingSource.getStats(criteria)

    fun getBillingItem(id: BillingItemId) = billingSource[id]

    fun update(id: BillingItemId, newBillingItem: NewBillingItem) =
        BillingItem(
            id,
            newBillingItem.client,
            newBillingItem.amount,
            newBillingItem.tag,
            newBillingItem.details
        ).also(billingSource::put)

    fun delete(id: BillingItemId) = billingSource.delete(id)
}