package billing.domain.model

data class BillingItem(
    val client: String,
    val amount: Double,
    val tag: String,
    val details: String
)