package billing.app

import billing.domain.StubBillingSource
import billing.domain.model.aNewBillingItem
import billing.domain.model.hasContentsOf
import com.natpryce.hamkrest.allOf
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test

class BillingAppTest {
    private val billingSource = StubBillingSource()
    private val app = BillingApp(billingSource)

    @Test
    fun `can add a billing item`() {
        val newBillingItem = aNewBillingItem()

        val billingItem = app.add(newBillingItem)

        assertThat(
            billingSource.getAll().single { it.id == billingItem.id },
            allOf(
                equalTo(billingItem),
                hasContentsOf(newBillingItem)
            )
        )
    }
}