package billing.json

import billing.domain.model.aBillingItem
import org.junit.jupiter.api.Test

class JBillingItemTest {
    @Test
    fun `can round trip`() =
        assertRoundTrips(
            JBillingItem
        ) {
            aBillingItem()
        }
}