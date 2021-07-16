package billing.domain

class StubBillingSourceTest: AbstractBillingSourceTest() {
    override val billingSource = StubBillingSource()
}