package billing.util

import com.natpryce.hamkrest.Matcher
import com.natpryce.hamkrest.assertion.assertThat
import java.lang.AssertionError
import java.lang.Exception
import java.time.Instant

fun <T> assertThatEventually(
    probe: () -> T,
    criteria: Matcher<T>,
    millis: Long = 10000
) {
    val start = Instant.now().toEpochMilli()
    fun assert() =
        assertThat(
            probe(),
            criteria
        )

    while (Instant.now().toEpochMilli() - start < millis) {
        try {
            assert()
            break
        }
        catch (e: AssertionError) { }
        catch (e: Exception) { }
        Thread.sleep(100)
    }

    assert()
}
