package billing.domain.model

data class BillingItemCriteria(
    val client: Client? = null,
    val amount: BillingAmount? = null,
    val tag: BillingItemTag? = null,
    val details: BillingItemDetails? = null
) {
    fun matches(item: BillingItem): Boolean =
        (client == null || client == item.client)
            && (amount == null || amount == item.amount)
            && (tag == null || tag == item.tag)
            && (details == null || details == item.details)
}