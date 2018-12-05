@file:JvmName("Main")

package rzaevali.parser

import rzaevali.routes.parseRoute
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

    parseRoute()
}
