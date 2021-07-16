package billing.domain.model

import billing.domain.TinyType

data class BillingItem(
    val id: BillingItemId,
    val client: Client,
    val amount: BillingAmount,
    val tag: BillingItemTag,
    val details: BillingItemDetails
)

data class NewBillingItem(
    val client: Client,
    val amount: BillingAmount,
    val tag: BillingItemTag,
    val details: BillingItemDetails
)

data class BillingItemId(override val value: String): TinyType<String>
data class Client(override val value: String): TinyType<String>
data class BillingAmount(override val value: Double): TinyType<Double>
data class BillingItemTag(override val value: String): TinyType<String>
data class BillingItemDetails(override val value: String): TinyType<String>