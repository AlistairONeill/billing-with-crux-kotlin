package billing.web

import billing.app.BillingApp
import billing.domain.StubBillingSource
import billing.domain.model.*
import billing.json.JBillingItem
import billing.json.JBillingStats
import billing.json.JNewBillingItem
import billing.web.routes.BillingRoutes.API_BILLING_ITEM
import billing.web.routes.BillingRoutes.API_BILLING_STATS
import billing.web.routes.BillingRoutes.PING
import billing.web.routes.billingRoutes
import com.natpryce.hamkrest.Matcher
import com.natpryce.hamkrest.allOf
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.present
import com.ubertob.kondor.json.JSet
import org.http4k.core.HttpHandler
import org.http4k.core.Method.*
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
                billingSource[billingItem.id],
                present(
                    allOf(
                        equalTo(billingItem),
                        hasContentsOf(newBillingItem)
                    )
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
        private fun assertResponse(request: Request, matcher: Matcher<Set<BillingItem>>) {
            val response = client(request)

            assertThat(
                response,
                hasStatus(OK)
            )

            assertThat(
                response.parseJsonBody(JSet(JBillingItem)),
                matcher
            )
        }

        @Test
        fun `can get an empty set of billing items`() =
            assertResponse(
                Request(GET, API_BILLING_ITEM),
                equalTo(emptySet())
            )

        @Test
        fun `can get all billing items`() {
            val items = List(3) { aBillingItem() }.toSet()

            items.forEach(billingSource::put)

            assertResponse(
                Request(GET, API_BILLING_ITEM),
                equalTo(items)
            )
        }

        @Test
        fun `get matching client`() {
            repeat(5) { billingSource.put(aBillingItem(client = "MrBar")) }

            val items = List(5) { aBillingItem(client = "MrFoo") }.toSet()
            items.forEach(billingSource::put)

            assertResponse(
                Request(GET, API_BILLING_ITEM)
                    .query("client", "MrFoo"),
                equalTo(items)
            )
        }

        @Test
        fun `get matching amount`() {
            repeat(5) { billingSource.put(aBillingItem(amount = -1.0)) }

            val items = List(5) { aBillingItem(amount = 118.0) }.toSet()
            items.forEach(billingSource::put)

            assertResponse(
                Request(GET, API_BILLING_ITEM)
                    .query("amount", "118.0"),
                equalTo(items)
            )
        }

        @Test
        fun `get matching tag`() {
            repeat(5) { billingSource.put(aBillingItem(tag = "Import")) }

            val items = List(5) { aBillingItem(tag = "Export") }.toSet()
            items.forEach(billingSource::put)

            assertResponse(
                Request(GET, API_BILLING_ITEM)
                    .query("tag", "Export"),
                equalTo(items)
            )
        }

        @Test
        fun `get matching details`() {
            repeat(5) { billingSource.put(aBillingItem(details = "late")) }

            val items = List(5) { aBillingItem(details = "sent on time") }.toSet()
            items.forEach(billingSource::put)

            assertResponse(
                Request(GET, API_BILLING_ITEM)
                    .query("details", "sent on time"),
                equalTo(items)
            )
        }

        @Test
        fun `can match against multiple criteria`() {
            repeat(5) { billingSource.put(aBillingItem(client = "MrFoo", tag = "Import")) }
            repeat(5) { billingSource.put(aBillingItem(client = "MrBar", tag = "Export")) }

            val items = List(5) { aBillingItem(client = "MrFoo", tag = "Export") }.toSet()
            items.forEach(billingSource::put)

            assertResponse(
                Request(GET, API_BILLING_ITEM)
                    .query("client", "MrFoo")
                    .query("tag", "Export"),
                equalTo(items)
            )
        }
    }

    @Nested
    inner class PutBillingItem {
        @Test
        fun `can put billing item`() {
            val newBillingItem = aNewBillingItem()
            val id = BillingItemId.mint()

            val response = client(
                Request(PUT, "$API_BILLING_ITEM/${id.value}")
                    .bodyAsJson(JNewBillingItem, newBillingItem)
            )

            assertThat(
                response,
                hasStatus(OK)
            )

            val billingItem = response.parseJsonBody(JBillingItem)

            assertThat(
                billingItem,
                allOf(
                    hasId(id),
                    hasContentsOf(newBillingItem)
                )
            )

            assertThat(
                billingSource[id],
                present(
                    equalTo(
                        billingItem
                    )
                )
            )
        }
    }

    @Nested
    inner class GetStats {
        private fun assertResponse(request: Request, matcher: Matcher<BillingStats>) {
            val response = client(request)

            assertThat(
                response,
                hasStatus(OK)
            )

            assertThat(
                response.parseJsonBody(JBillingStats),
                matcher
            )
        }

        @Test
        fun `can get stats`() {
            repeat(5) { billingSource.put(aBillingItem(amount = it.toDouble())) }

            assertResponse(
                Request(GET, API_BILLING_STATS),
                allOf(
                    hasItemCount(5),
                    hasTotal(10.0),
                    hasMean(2.0)
                )
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