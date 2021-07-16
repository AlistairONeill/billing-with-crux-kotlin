package billing.json

import billing.domain.model.aBillingStats
import org.junit.jupiter.api.Test

class JBillingStatsTest {
    @Test
    fun `can round trip`() =
        assertRoundTrips(
            JBillingStats
        ) {
            aBillingStats()
        }
}