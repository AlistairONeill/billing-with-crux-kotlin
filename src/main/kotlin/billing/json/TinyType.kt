package billing.json

import billing.domain.TinyType
import com.ubertob.kondor.json.JDouble
import com.ubertob.kondor.json.JField
import com.ubertob.kondor.json.JString

fun <PT: Any, T: TinyType<String>> str(binder: PT.() -> T) = JField<String, PT>(
    { binder(it).value },
    JString
)

fun <PT: Any, T: TinyType<Double>> num(binder: PT.() -> T) = JField<Double, PT>(
    { binder(it).value },
    JDouble
)