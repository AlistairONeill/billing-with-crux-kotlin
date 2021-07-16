package billing.web.filters

import billing.json.JCaughtException
import billing.web.toResponse
import com.ubertob.kondor.outcome.OutcomeException
import org.http4k.core.Filter
import org.http4k.core.HttpHandler
import org.http4k.core.Status
import org.http4k.core.Status.Companion.BAD_REQUEST
import org.http4k.core.Status.Companion.INTERNAL_SERVER_ERROR

object ExceptionHandling: Filter {
    override fun invoke(next: HttpHandler): HttpHandler = { request ->
        try {
            next(request)
        }
        catch (exception: OutcomeException) {
            response(
                BAD_REQUEST,
                "JSON Parse Exception",
                exception.localizedMessage
            )
        }
        catch (exception: Exception) {
            response(
                INTERNAL_SERVER_ERROR,
                exception.javaClass.name,
                exception.localizedMessage
            )
        }
    }
}

data class CaughtException(
    val type: String,
    val message: String
)

private fun response(
    status: Status,
    type: String,
    message: String
) = CaughtException(
    type,
    message
).toResponse(status, JCaughtException)