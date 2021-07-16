package billing.web.routes

import billing.app.BillingApp
import billing.domain.model.*
import billing.json.JBillingItem
import billing.json.JNewBillingItem
import billing.web.parseJsonBody
import billing.web.routes.BillingRoutes.API_BILLING_ITEM
import billing.web.toOkResponse
import com.ubertob.kondor.json.JSet
import org.http4k.contract.ContractRoute
import org.http4k.contract.meta
import org.http4k.core.ContentType.Companion.APPLICATION_JSON
import org.http4k.core.Method
import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.core.Status.Companion.BAD_REQUEST
import org.http4k.core.Status.Companion.INTERNAL_SERVER_ERROR
import org.http4k.core.Status.Companion.OK
import org.http4k.format.httpBodyLens

fun addBillingItemRoute(billingApp: BillingApp): ContractRoute =
    API_BILLING_ITEM meta {
        summary = "adds a billing item"
        description = "adds a billing item"
        consumes += APPLICATION_JSON
        produces += APPLICATION_JSON
        receiving(newBillingItemLens to exampleNewBillingItem)
        returning(OK to "The billing item")
        returning(BAD_REQUEST)
        returning(INTERNAL_SERVER_ERROR)
    } bindContract POST to { request ->
        request
            .parseJsonBody(JNewBillingItem)
            .let(billingApp::add)
            .toOkResponse(JBillingItem)
    }

private val exampleNewBillingItem =
    NewBillingItem(
        Client("Tech Co."),
        BillingAmount(100.00),
        BillingItemTag("Infra"),
        BillingItemDetails("Monthly payment for cloud compute")
    ).let(JNewBillingItem::toJson)

private val newBillingItemLens = httpBodyLens("The Billing Item to add", contentType = APPLICATION_JSON).toLens()

fun getBillingItemsRoute(billingApp: BillingApp): ContractRoute =
    API_BILLING_ITEM meta {
        summary = "gets all billing items"
        description = "gets all billing items"
        produces += APPLICATION_JSON
        returning(OK to "The billing items")
    } bindContract GET to {
        billingApp.getAllBillingItems()
            .toOkResponse(JSet(JBillingItem))
    }