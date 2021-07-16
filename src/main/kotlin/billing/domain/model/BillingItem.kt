package billing.domain.model

data class BillingItem(
    val id: String,
    val client: String,
    val amount: Double,
    val tag: String,
    val details: String
)