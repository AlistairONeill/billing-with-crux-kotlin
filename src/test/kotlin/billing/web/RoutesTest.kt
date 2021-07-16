package billing.web

import billing.app.BillingApp
import billing.domain.StubBillingSource
import billing.web.routes.BillingRoutes.PING
import billing.web.routes.billingRoutes
import com.natpryce.hamkrest.allOf
import com.natpryce.hamkrest.assertion.assertThat
import org.http4k.core.HttpHandler
import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.core.Status.Companion.OK
import org.http4k.hamkrest.hasBody
import org.http4k.hamkrest.hasStatus
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class RoutesTest {
    private val billingSource = StubBillingSource()

    private val client: HttpHandler =
        billingSource
            .let(::BillingApp)
            .let(::billingRoutes)

    @Nested
    inner class Ping {
        @Test
        fun `ping returns pong`() {
            assertThat(
                client(
                    Request(GET, PING)
                ),
                allOf(
                    hasStatus(OK),
                    hasBody("pong")
                )
            )
        }
    }
}