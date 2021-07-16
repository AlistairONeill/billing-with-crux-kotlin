package billing.domain

import billing.domain.model.aBillingItem
import billing.util.assertThatEventually
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
}