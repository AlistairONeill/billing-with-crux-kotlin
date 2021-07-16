package billing.web.filters

import billing.json.JBillingItem
import billing.json.JCaughtException
import billing.web.parseJsonBody
import billing.web.setBodyString
import com.natpryce.hamkrest.allOf
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.http4k.core.*
import org.http4k.core.Status.Companion.BAD_REQUEST
import org.http4k.core.Status.Companion.INTERNAL_SERVER_ERROR
import org.http4k.core.Status.Companion.OK
import org.http4k.hamkrest.hasBody
import org.http4k.hamkrest.hasStatus
import org.junit.jupiter.api.Test

class ExceptionHandlingTest {
    private fun HttpHandler.call() = invoke ( Request(Method.GET, "/foo") )

    @Test
    fun `allows a regular response through`() {
        assertThat(
            ExceptionHandling
                .then { Response(OK).setBodyString("Foo") }
                .call(),
            allOf(
                hasStatus(OK),
                hasBody("Foo")
            )
        )
    }

    @Test
    fun `turns generic exception into a 500`() {
        val response = ExceptionHandling
            .then { throw ArrayIndexOutOfBoundsException("Oh no!") }
            .call()

        assertThat(
            response,
            hasStatus(INTERNAL_SERVER_ERROR)
        )

        assertThat(
            response.parseJsonBody(JCaughtException),
            equalTo(
                CaughtException(
                    "java.lang.ArrayIndexOutOfBoundsException",
                    "Oh no!"
                )
            )
        )
    }

    @Test
    fun `a kondor parsing exception is converted to a 400`() {
        val response = ExceptionHandling
            .then { request ->
                request.parseJsonBody(JBillingItem)
                Response(OK)
            }.call()

        assertThat(
            response,
            hasStatus(BAD_REQUEST)
        )

        assertThat(
            response.parseJsonBody(JCaughtException),
            equalTo(
                CaughtException(
                    "JSON Parse Exception",
                    "error on <[root]> at position 0: expected an Object but found unexpected end of file after null - Invalid Json"
                )
            )
        )
    }
}