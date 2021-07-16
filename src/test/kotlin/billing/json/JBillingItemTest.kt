package billing.json

import billing.domain.model.aBillingItem
import billing.domain.model.aNewBillingItem
import org.junit.jupiter.api.Test

class JBillingItemTest {
    @Test
    fun `can round trip billing item`() =
        assertRoundTrips(
            JBillingItem
        ) {
            aBillingItem()
        }

    @Test
    fun `can round trip new billing item`() =
        assertRoundTrips(
            JNewBillingItem
        ) {
            aNewBillingItem()
        }
}