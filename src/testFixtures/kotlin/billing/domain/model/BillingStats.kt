package billing.domain.model

import com.natpryce.hamkrest.MatchResult
import com.natpryce.hamkrest.MatchResult.Match
import com.natpryce.hamkrest.Matcher
import kotlin.random.Random.Default.nextDouble
import kotlin.random.Random.Default.nextLong

fun aBillingStats(
    itemCount: Long = nextLong(0, 100),
    total: Double = nextDouble(0.0, 1000.0),
    mean: Double = nextDouble(0.0, 1000.0)
) = BillingStats(
    itemCount = itemCount,
    total = BillingAmount(total),
    mean = BillingAmount(mean)
)

fun hasItemCount(expected: Long) = matcher(BillingStats::itemCount, expected, "itemCount")
fun hasTotal(expected: BillingAmount) = matcher(BillingStats::total, expected, "total")
fun hasMean(expected: BillingAmount) = matcher(BillingStats::mean, expected, "mean")

private fun <T> matcher(probe: (BillingStats) -> T, expected: T, propertyName: String) = object : Matcher<BillingStats> {
    override val description = "has $propertyName of $expected"

    override fun invoke(actual: BillingStats) =
        probe(actual).let {
            if (expected == it) {
                Match
            } else {
                MatchResult.Mismatch("Expected $propertyName of $expected but was actually $it")
            }
        }
}