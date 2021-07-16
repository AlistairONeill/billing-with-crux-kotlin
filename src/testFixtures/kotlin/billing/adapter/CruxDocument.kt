package billing.adapter

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import crux.api.CruxDocument

fun <T: Any> assertRoundTrip(
    toCruxDocument: (T) -> CruxDocument,
    toItem: (CruxDocument) -> T,
    item: T
) = assertThat(
    item.let(toCruxDocument)
        .let(toItem),
    equalTo(item)
)

fun <T: Any> assertRoundTrips(
    toCruxDocument: (T) -> CruxDocument,
    toItem: (CruxDocument) -> T,
    number: Int = 10,
    itemGenerator: () -> T
) = repeat(number) {
    assertRoundTrip(toCruxDocument, toItem, itemGenerator())
}