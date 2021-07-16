package billing.web.routes

import billing.domain.BillingSource
import billing.web.routes.BillingRoutes.API_DESCRIPTION_PATH
import org.http4k.contract.contract
import org.http4k.contract.openapi.ApiInfo
import org.http4k.contract.openapi.v3.OpenApi3
import org.http4k.core.Method.GET
import org.http4k.core.Response
import org.http4k.core.Status.Companion.FOUND
import org.http4k.format.Jackson
import org.http4k.routing.ResourceLoader.Companion.Classpath
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.routing.static

object BillingRoutes {
    const val API_DESCRIPTION_PATH = "/api/swagger.json"
    const val PING = "/ping"
}

fun billingRoutes(
    billingSource: BillingSource
) = routes(
    "docs" bind GET to { Response(FOUND).header("Location", "/docs/index.html?url=$API_DESCRIPTION_PATH") },
    "/docs" bind static(Classpath("META-INF/resources/webjars/swagger-ui/3.25.2")),
    contract {
        renderer = OpenApi3(
            ApiInfo(
                "billing-with-crux-kotlin",
                "v1.0",
                "Have fun and play around!"
            ),
            Jackson
        )
        descriptionPath = API_DESCRIPTION_PATH
        routes += pingRoute()
    }
)