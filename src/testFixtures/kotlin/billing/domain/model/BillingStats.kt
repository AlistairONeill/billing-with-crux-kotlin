package billing.domain.model

import kotlin.random.Random.Default.nextDouble
import kotlin.random.Random.Default.nextInt

fun aBillingStats(
    itemCount: Int = nextInt(0, 100),
    total: Double = nextDouble(0.0, 1000.0),
    mean: Double = nextDouble(0.0, 1000.0)
) = BillingStats(
    itemCount = itemCount,
    total = BillingAmount(total),
    mean = BillingAmount(mean)
)