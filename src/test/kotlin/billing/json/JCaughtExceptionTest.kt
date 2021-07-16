package billing.json

import billing.util.randomString
import billing.web.filters.CaughtException
import org.junit.jupiter.api.Test

class JCaughtExceptionTest {
    @Test
    fun `can round trip caught exception`() =
        assertRoundTrips(
            JCaughtException
        ) {
            CaughtException(
                randomString(),
                randomString()
            )
        }
}