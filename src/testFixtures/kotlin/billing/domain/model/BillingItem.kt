package billing.domain.model

import billing.util.randomString
import com.natpryce.hamkrest.MatchResult
import com.natpryce.hamkrest.Matcher
import com.natpryce.hamkrest.describe
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

fun aNewBillingItem(
    client: String = randomString(),
    amount: Double = nextDouble(),
    tag: String = randomString(),
    details: String = randomString()
) = NewBillingItem(
    Client(client),
    BillingAmount(amount),
    BillingItemTag(tag),
    BillingItemDetails(details)
)

fun hasId(expected: BillingItemId): Matcher<BillingItem> =
    object : Matcher<BillingItem> {
        override fun invoke(actual: BillingItem): MatchResult =
            if (actual.id == expected) {
                MatchResult.Match
            } else {
                MatchResult.Mismatch("was: ${describe(actual)}")
            }
        override val description: String get() = "has id equal to ${describe(expected)}"
        override val negatedDescription: String get() = "does not have id equal to ${describe(expected)}"
    }

fun hasContentsOf(expected: NewBillingItem): Matcher<BillingItem> =
    object : Matcher<BillingItem> {
        override fun invoke(actual: BillingItem): MatchResult =
            if (actual.client == expected.client
                && actual.amount == expected.amount
                && actual.details == expected.details
                && actual.tag == expected.tag) {
                MatchResult.Match
            } else {
                MatchResult.Mismatch("was: ${describe(actual)}")
            }
        override val description: String get() = "has contents equal to ${describe(expected)}"
        override val negatedDescription: String get() = "does not have contents equal to ${describe(expected)}"
    }
