package billing.adapter

import billing.domain.AbstractBillingSourceTest
import crux.api.CruxK

class CruxBillingSourceTest: AbstractBillingSourceTest() {
    private val crux = CruxK.startNode()
    override val billingSource = CruxBillingSource(crux)
}