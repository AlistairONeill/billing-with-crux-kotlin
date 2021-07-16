package billing.domain.model

data class BillingStats(
    val itemCount: Int,
    val total: BillingAmount,
    val mean: BillingAmount
)