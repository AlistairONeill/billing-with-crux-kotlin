package billing.domain

import billing.domain.model.BillingItemId
import billing.domain.model.aBillingItem
import billing.util.assertThatEventually
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test

abstract class AbstractBillingSourceTest {
    abstract val billingSource: BillingSource

    @Test
    fun `can put a billing item`() {
        val item = aBillingItem()

        billingSource.put(item)

        assertThatEventually(
            { billingSource.getAll() },
            equalTo(setOf(item))
        )
    }

    @Test
    fun `can get all billing items`() {
        val items = List(10) { aBillingItem() }.toSet()

        items.forEach(billingSource::put)

        assertThatEventually(
            { billingSource.getAll() },
            equalTo(items)
        )
    }

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