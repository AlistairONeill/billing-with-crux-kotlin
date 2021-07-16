package billing.web

import com.natpryce.hamkrest.allOf
import com.natpryce.hamkrest.assertion.assertThat
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.hamkrest.hasBody
import org.http4k.hamkrest.hasHeader
import org.junit.jupiter.api.Test

class ResponseTest {
    @Test
    fun `sets the content-type correctly`() {
        assertThat(
            Response(OK).setBodyString("foo"),
            allOf(
                hasHeader("content-type", "text/plain"),
                hasBody("foo")
            )
        )
    }
}