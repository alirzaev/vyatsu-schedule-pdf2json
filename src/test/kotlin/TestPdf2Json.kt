import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import rzaevali.utils.extractSchedule
import java.io.FileReader
import java.nio.file.Files
import java.nio.file.Paths

class TestPdf2Json {

    private val gson: Gson = GsonBuilder().create()

    @ParameterizedTest
    @ValueSource(strings = [
        "9588", "9592", "9605", "9747", "9748",
        "9902", "10793", "10829", "10879", "11045"
    ])
    fun testExtraction(groupId: String) {
        val pdfPath = "src/test/resources/pdf/${groupId}_1_26112018_09122018.pdf"
        val jsonPath = "src/test/resources/json/${groupId}_1_26112018_09122018.json"

        val actual = extractSchedule(Files.newInputStream(Paths.get(pdfPath)))
        val data = gson.fromJson(FileReader(jsonPath), Map::class.java)
        val expected = (data["data"] as Map<*, *>) ["schedule"] as List<*>

        assertEquals(expected, actual, "Mismatch for '$groupId'")

    }
}