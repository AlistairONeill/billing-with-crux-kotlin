package billing.adapter

import billing.domain.model.BillingItem
import billing.domain.model.aBillingItem
import crux.api.CruxDocument
import org.junit.jupiter.api.Test

class BillingItemTest {
    @Test
    fun `can round trip via CruxDocument`() =
        assertRoundTrips(
            BillingItem::toCruxDocument,
            CruxDocument::toBillingItem
        ) { aBillingItem() }
}