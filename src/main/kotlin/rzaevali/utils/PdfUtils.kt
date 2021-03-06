package rzaevali.utils

import com.mashape.unirest.http.Unirest
import org.apache.logging.log4j.LogManager
import org.apache.pdfbox.pdmodel.PDDocument
import rzaevali.algo.LeftToRightExtractionAlgorithm
import rzaevali.exceptions.PdfFileFormatException
import rzaevali.exceptions.PdfFileProcessingException
import rzaevali.exceptions.Pdf2JsonException
import technology.tabula.ObjectExtractor
import java.io.InputStream

/**
 * Type alias for three-dimension array of strings for representing schedule.
 * First index - week, second - day, third - lesson
 */
typealias NestedList = List<List<List<String>>>

private val logger = LogManager.getLogger("PdfUtils")

/**
 * Extract table rows representing schedule from pdf document
 * Each table row representing lesson matches to the following regular expression: \d{2}:\d{2}-\d{2}:\d{2}\s*.*
 *
 * @param stream InputStream instance of pdf document with schedule
 * @return list of lessons for two weeks
 * @throws PdfFileProcessingException if some errors occurred during extraction
 */
@Throws(PdfFileProcessingException::class)
private fun extractRows(stream: InputStream): List<String> {
    try {
        PDDocument.load(stream).use { pdfDocument ->
            val pageIterator = ObjectExtractor(pdfDocument).extract()
            val algorithm = LeftToRightExtractionAlgorithm()

            return pageIterator.asSequence()
                    .flatMap { page -> algorithm.extract(page).asSequence() }
                    .flatMap { table -> table.rows.asSequence() }
                    .map { row ->
                        row.asSequence()
                                .map { textContainer -> textContainer.text }
                                .filter { text -> text != "" }
                                .joinToString(" ")
                    }
                    .map { text -> text.replace('\r', ' ') }
                    .filter { text -> text.matches(Regex("\\d{2}:\\d{2}-\\d{2}:\\d{2}\\s*.*")) || text.isNotEmpty() }
                    .map { text -> text.replaceFirst(Regex("\\d{2}:\\d{2}-\\d{2}:\\d{2}\\s*"), "") }
                    .drop(1)
                    .toList()
        }
    } catch (ex: Exception) {
        logger.error("An exception was raised during extracting of rows")
        logger.throwing(ex)

        throw PdfFileProcessingException("PDF_PARSE_ERROR")
    }

}

/**
 * Extract schedule from specified stream of pdf document
 *
 * @param stream InputStream instance of pdf document with schedule
 * @return schedule as three-dimension array
 * @throws Pdf2JsonException if some errors occurred during extraction
 */
@Throws(Pdf2JsonException::class)
fun extractSchedule(stream: InputStream): NestedList {
    val daysCount = 14
    val lessonsPerDay = 7

    val rows = extractRows(stream)
    if (rows.size != daysCount * lessonsPerDay) {
        logger.error("The count of rows doesn't equal to ${daysCount * lessonsPerDay}: ${rows.size}")

        throw PdfFileFormatException("INVALID_ROW_COUNT")
    }

    val days = (0..13).asSequence().map { day ->
        val fromIndex = day * 7
        val toIndex = fromIndex + 7
        rows.subList(fromIndex, toIndex)
    }.toList()

    return listOf(
            days.subList(0, 6),
            days.subList(7, 13)
    )
}

/**
 * Extract schedule from pdf at specified URL
 *
 * @param url URL of the pdf document with schedule
 * @return schedule as three-dimension array
 * @throws Pdf2JsonException if some errors occurred during extraction
 */
@Throws(Pdf2JsonException::class)
fun extractSchedule(url: String): NestedList {
    return extractSchedule(Unirest.get(url).asBinary().body)
}


