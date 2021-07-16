package billing.domain.model

data class BillingStats(
    val itemCount: Long,
    val total: BillingAmount,
    val mean: BillingAmount
) {
    companion object {
        val Empty = BillingStats(
            0,
            BillingAmount(0.0),
            BillingAmount(0.0)
        )
    }
}