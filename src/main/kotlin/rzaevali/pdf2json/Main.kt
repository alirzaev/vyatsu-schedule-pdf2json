@file:JvmName("Main")

package rzaevali.pdf2json

import rzaevali.routes.convertRoute
import spark.Spark.port

fun main(args: Array<String>) {
    val defaultPort = "80"
    val portVar = System.getenv("PORT") ?: ""
    val port = if (portVar.isEmpty()) {
        defaultPort
    } else {
        portVar
    }

    port(port.toInt())

    convertRoute()
}
