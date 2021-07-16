package billing.domain.model

import billing.util.randomString
import java.util.*
import kotlin.random.Random.Default.nextDouble

fun aBillingItem(
    id: String = UUID.randomUUID().toString(),
    client: String = randomString(),
    amount: Double = nextDouble(),
    tag: String = randomString(),
    details: String = randomString()
) = BillingItem(
    BillingItemId(id),
    Client(client),
    BillingAmount(amount),
    BillingItemTag(tag),
    BillingItemDetails(details)
)