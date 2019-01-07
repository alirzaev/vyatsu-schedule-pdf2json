package rzaevali.routes

import com.google.gson.Gson
import org.apache.logging.log4j.LogManager
import rzaevali.exceptions.Pdf2JsonException
import rzaevali.utils.*
import spark.Spark.get

private val logger = LogManager.getLogger("api/v2/convert")

private val gson = Gson()

fun convertRoute() {
    get("/api/v2/convert") { req, res ->
        res.type("application/json")

        val url = req.queryParams("url") ?: return@get gson.toJson(Response(
                Error(500, "NO_URL"),
                null
        ))

        try {
            logger.info(url)

            val schedule = extractSchedule(url)

            return@get gson.toJson(Response(
                    Success(200, "CONVERTED"),
                    Schedule(schedule)
            ))
        } catch (ex: Pdf2JsonException) {
            logger.error(url)
            logger.throwing(ex)

            val msg = ex.message ?: ""
            return@get gson.toJson(Response(
                    Error(422, msg),
                    null
            ))
        } catch (ex: Exception) {
            logger.error(url)
            logger.throwing(ex)

            return@get gson.toJson(Response(
                    Error(500, "INTERNAL_SERVER_ERROR"),
                    null
            ))
        }
    }
}