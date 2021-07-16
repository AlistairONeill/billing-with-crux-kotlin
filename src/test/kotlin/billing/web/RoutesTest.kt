package billing.web

import billing.app.BillingApp
import billing.domain.StubBillingSource
import billing.domain.model.BillingItemId
import billing.domain.model.aBillingItem
import billing.domain.model.aNewBillingItem
import billing.domain.model.hasContentsOf
import billing.json.JBillingItem
import billing.json.JNewBillingItem
import billing.web.routes.BillingRoutes.API_BILLING_ITEM
import billing.web.routes.BillingRoutes.PING
import billing.web.routes.billingRoutes
import com.natpryce.hamkrest.allOf
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.ubertob.kondor.json.JSet
import org.http4k.core.HttpHandler
import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.core.Request
import org.http4k.core.Status.Companion.BAD_REQUEST
import org.http4k.core.Status.Companion.NOT_FOUND
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

    @Nested
    inner class NewBillingItem {
        @Test
        fun `can post a new billing item`() {
            val newBillingItem = aNewBillingItem()

            val response = client(
                Request(POST, API_BILLING_ITEM)
                    .bodyAsJson(JNewBillingItem, newBillingItem)
            )

            assertThat(
                response,
                hasStatus(OK)
            )

            val billingItem = response.parseJsonBody(JBillingItem)

            assertThat(
                billingSource.getAll().single { it.id == billingItem.id },
                allOf(
                    equalTo(billingItem),
                    hasContentsOf(newBillingItem)
                )
            )
        }

        @Test
        fun `a bad request returns a 400`() =
            assertThat(
                client(
                    Request(POST, API_BILLING_ITEM)
                        .header("content-type", "application/json")
                        .body("THIS ISN'T THE JSON YOU ARE LOOKING FOR")
                ),
                hasStatus(BAD_REQUEST)
            )
    }

    @Nested
    inner class GetBillingItems {
        @Test
        fun `can get an empty set of billing items`() {
            val response = client(
                Request(GET, API_BILLING_ITEM)
            )

            assertThat(
                response,
                hasStatus(OK)
            )

            assertThat(
                response.parseJsonBody(JSet(JBillingItem)),
                equalTo(emptySet())
            )
        }

        @Test
        fun `can get billing items`() {
            val billingItems = List(3) { aBillingItem() }.toSet()

            billingItems.forEach(billingSource::put)

            val response = client(
                Request(GET, API_BILLING_ITEM)
            )

            assertThat(
                response,
                hasStatus(OK)
            )

            assertThat(
                response.parseJsonBody(JSet(JBillingItem)),
                equalTo(billingItems)
            )
        }
    }

    @Nested
    inner class GetBillingItem {
        @Test
        fun `can get a billing item`() {
            val billingItem = aBillingItem()

            billingSource.put(billingItem)

            val response = client(
                Request(GET, "$API_BILLING_ITEM/${billingItem.id.value}")
            )

            assertThat(
                response,
                hasStatus(OK)
            )

            assertThat(
                response.parseJsonBody(JBillingItem),
                equalTo(billingItem)
            )
        }

        @Test
        fun `returns 404 if no billing item found`() {
            assertThat(
                client(
                    Request(GET, "$API_BILLING_ITEM/${BillingItemId.mint().value}")
                ),
                hasStatus(NOT_FOUND)
            )
        }
    }
}