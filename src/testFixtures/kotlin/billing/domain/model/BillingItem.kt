package billing.domain.model

import billing.domain.model.BillingItem
import billing.util.randomString
import kotlin.random.Random.Default.nextDouble

fun aBillingItem(
    client: String = randomString(),
    amount: Double = nextDouble(),
    tag: String = randomString(),
    details: String = randomString()
) = BillingItem(
    client,
    amount,
    tag,
    details
)