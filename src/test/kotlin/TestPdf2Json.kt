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
        "14518", "14538", "14690"
    ])
    fun testExtraction(groupId: String) {
        val pdfPath = "src/test/resources/pdf/${groupId}_1_01092020_14092020.pdf"
        val jsonPath = "src/test/resources/json/${groupId}_1_01092020_14092020.json"

        val actual = extractSchedule(Files.newInputStream(Paths.get(pdfPath)))
        val data = gson.fromJson(FileReader(jsonPath), Map::class.java)
        val expected = (data["data"] as Map<*, *>) ["schedule"] as List<*>

        assertEquals(expected, actual, "Mismatch for '$groupId'")

    }
}