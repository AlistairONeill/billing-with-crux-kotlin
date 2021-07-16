package billing.json

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.ubertob.kondor.json.JConverter

fun <T: Any> assertRoundTrip(
    converter: JConverter<T>,
    item: T
) = assertThat(
    item.let(converter::toJson)
        .let(converter::fromJson)
        .orThrow(),
    equalTo(item)
)

fun <T: Any> assertRoundTrips(
    converter: JConverter<T>,
    count: Int = 10,
    itemGenerator: () -> T
) = repeat(count) {
    assertRoundTrip(
        converter,
        itemGenerator()
    )
}