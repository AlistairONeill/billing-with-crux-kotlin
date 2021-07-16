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

    fun getBillingItem(id: BillingItemId) = billingSource[id]
}