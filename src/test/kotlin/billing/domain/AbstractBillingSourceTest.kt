@file:Suppress("unused")

package billing.domain

import billing.domain.model.*
import billing.util.assertThatEventually
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.present
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

abstract class AbstractBillingSourceTest {
    abstract val billingSource: BillingSource

    @Nested
    inner class Put {
        @Test
        fun `can put a billing item`() {
            val item = aBillingItem()

            billingSource.put(item)

            assertThatEventually(
                { billingSource[item.id] },
                present(
                    equalTo(item)
                )
            )
        }
    }

    @Nested
    inner class GetMatching {
        @Test
        fun `can get all billing items`() {
            val items = List(10) { aBillingItem() }.toSet()

            items.forEach(billingSource::put)

            assertThatEventually(
                { billingSource.getMatching(BillingItemCriteria()) },
                equalTo(items)
            )
        }

        @Test
        fun `get matching client`() {
            repeat(5) { billingSource.put(aBillingItem(client = "MrBar")) }

            val items = List(5) { aBillingItem(client = "MrFoo") }.toSet()
            items.forEach(billingSource::put)

            assertThatEventually(
                {
                    billingSource.getMatching(
                        BillingItemCriteria(
                            client = Client("MrFoo")
                        )
                    )
                },
                equalTo(items)
            )
        }

        @Test
        fun `get matching amount`() {
            repeat(5) { billingSource.put(aBillingItem(amount = -1.0)) }

            val items = List(5) { aBillingItem(amount = 118.0 ) }.toSet()
            items.forEach(billingSource::put)

            assertThatEventually(
                {
                    billingSource.getMatching(
                        BillingItemCriteria(
                            amount = BillingAmount(118.0)
                        )
                    )
                },
                equalTo(items)
            )
        }

        @Test
        fun `get matching tag`() {
            repeat(5) { billingSource.put(aBillingItem(tag = "Import")) }

            val items = List(5) { aBillingItem(tag = "Export") }.toSet()
            items.forEach(billingSource::put)

            assertThatEventually(
                {
                    billingSource.getMatching(
                        BillingItemCriteria(
                            tag = BillingItemTag("Export")
                        )
                    )
                },
                equalTo(items)
            )
        }

        @Test
        fun `get matching details`() {
            repeat(5) { billingSource.put(aBillingItem(details = "late")) }

            val items = List(5) { aBillingItem(details = "sent on time") }.toSet()
            items.forEach(billingSource::put)

            assertThatEventually(
                {
                    billingSource.getMatching(
                        BillingItemCriteria(
                            details = BillingItemDetails("sent on time")
                        )
                    )
                },
                equalTo(items)
            )
        }

        @Test
        fun `can match against multiple criteria`() {
            repeat(5) { billingSource.put(aBillingItem(client = "MrFoo", tag = "Import")) }
            repeat(5) { billingSource.put(aBillingItem(client = "MrBar", tag = "Export")) }

            val items = List(5) { aBillingItem(client = "MrFoo", tag = "Export") }.toSet()
            items.forEach(billingSource::put)

            assertThatEventually(
                {
                    billingSource.getMatching(
                        BillingItemCriteria(
                            client = Client("MrFoo"),
                            tag = BillingItemTag("Export")
                        )
                    )
                },
                equalTo(items)
            )
        }

        @Test
        fun `returns empty set when no matching items`() {
            assertThat(
                billingSource.getMatching(BillingItemCriteria()),
                equalTo(emptySet())
            )
        }
    }

    @Nested
    inner class Statistics {
        @Test
        fun `doesn't fall over when empty`() {
            assertThat(
                billingSource.getStats(BillingItemCriteria()),
                equalTo(
                    BillingStats(
                        0,
                        BillingAmount(0.0),
                        BillingAmount(0.0)
                    )
                )
            )
        }

        @Test
        fun `correctly counts number of entries`() {
            repeat(5) { billingSource.put(aBillingItem()) }

            assertThatEventually(
                { billingSource.getStats(BillingItemCriteria()) },
                hasItemCount(5)
            )
        }

        @Test
        fun `correctly calculates total`() {
            repeat(5) { billingSource.put(aBillingItem(amount = it.toDouble())) }

            assertThatEventually(
                { billingSource.getStats(BillingItemCriteria()) },
                hasTotal(10.0)
            )
        }

        @Test
        fun `correctly calculates mean`() {
            repeat(5) { billingSource.put(aBillingItem(amount = it.toDouble())) }

            assertThatEventually(
                { billingSource.getStats(BillingItemCriteria()) },
                hasMean(2.0)
            )
        }
    }

    @Nested
    inner class Delete {
        @Test
        fun `can delete billing item by id`() {
            val item = aBillingItem()

            billingSource.put(item)

            //Check that the put has definitely been processed!
            assertThatEventually(
                { billingSource[item.id] },
                present(equalTo(item))
            )

            billingSource.delete(item.id)

            assertThatEventually(
                { billingSource[item.id] },
                equalTo(null)
            )
        }
    }

    @Nested
    inner class GetById {
        @Test
        fun `can get billing item by id`() {
            val item = aBillingItem()

            billingSource.put(item)

            assertThatEventually(
                { billingSource[item.id] },
                equalTo(item)
            )
        }

        @Test
        fun `returns null for unknown billing item id`() =
            assertThat(
                billingSource[BillingItemId.mint()],
                equalTo(null)
            )
    }
}