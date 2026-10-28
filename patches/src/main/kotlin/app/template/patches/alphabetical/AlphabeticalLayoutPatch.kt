package app.template.patches.alphabetical

import app.morphe.patcher.patch.ResourcePatchContext
import app.morphe.patcher.patch.resourcePatch
import app.template.patches.shared.Constants.COMPATIBILITY_GBOARD_18_5_4

private const val XML_DIR = "res/xml"
private const val RUSSIAN_IME = "res/xml/APKTOOL_RENAMED_0x7f17036d.xml"
private const val ENGLISH_IME = "res/xml/APKTOOL_RENAMED_0x7f17045e.xml"

private data class Letter(
    val lower: String,
    val lowerId: String,
    val upperId: String,
    val upper: String = "",
    val longPressLower: String? = null,
    val longPressUpper: String? = null,
)

private fun id(hex: String) = "@id/APKTOOL_RENAMED_0x7f0b$hex"

private val english = listOf(
    Letter("a", id("18d4"), id("17f7"), "A"), Letter("b", id("18e4"), id("1806"), "B"),
    Letter("c", id("18e9"), id("1808"), "C"), Letter("d", id("18f7"), id("180f"), "D"),
    Letter("e", id("1905"), id("1813"), "E"), Letter("f", id("191b"), id("1826"), "F"),
    Letter("g", id("191f"), id("1828"), "G"), Letter("h", id("192e"), id("1833"), "H"),
    Letter("i", id("1934"), id("1838"), "I"), Letter("j", id("1945"), id("1849"), "J"),
    Letter("k", id("1949"), id("184b"), "K"), Letter("l", id("1951"), id("184f"), "L"),
    Letter("m", id("195e"), id("1853"), "M"), Letter("n", id("1965"), id("1857"), "N"),
    Letter("o", id("1971"), id("1860"), "O"), Letter("p", id("1987"), id("1875"), "P"),
    Letter("q", id("198e"), id("1877"), "Q"), Letter("r", id("1992"), id("1878"), "R"),
    Letter("s", id("199d"), id("187d"), "S"), Letter("t", id("19c5"), id("1886"), "T"),
    Letter("u", id("19e6"), id("1890"), "U"), Letter("v", id("19fb"), id("18a3"), "V"),
    Letter("w", id("19fe"), id("18a5"), "W"), Letter("x", id("1a03"), id("18a8"), "X"),
    Letter("y", id("1a06"), id("18a9"), "Y"), Letter("z", id("1a0d"), id("18ad"), "Z"),
)

private val russian = listOf(
    Letter("а", id("0d99"), id("0d43"), "А"), Letter("б", id("0da0"), id("0d4a"), "Б"),
    Letter("в", id("0de2"), id("0d8b"), "В"), Letter("г", id("0db5"), id("0d5f"), "Г"),
    Letter("д", id("0da5"), id("0d4f"), "Д"), Letter("е", id("0dc0"), id("0d6a"), "Е", longPressLower = "ё", longPressUpper = "Ё"),
    Letter("ж", id("0de9"), id("0d92"), "Ж"), Letter("з", id("0de7"), id("0d90"), "З"),
    Letter("и", id("0dbd"), id("0d67"), "И", longPressLower = "й", longPressUpper = "Й"), Letter("к", id("0dc5"), id("0d6f"), "К"),
    Letter("л", id("0dac"), id("0d56"), "Л"), Letter("м", id("0dad"), id("0d57"), "М"),
    Letter("н", id("0dae"), id("0d58"), "Н"), Letter("о", id("0dcd"), id("0d77"), "О"),
    Letter("п", id("0dd1"), id("0d7a"), "П"), Letter("р", id("0db2"), id("0d5c"), "Р"),
    Letter("с", id("0db3"), id("0d5d"), "С"), Letter("т", id("0ddb"), id("0d84"), "Т"),
    Letter("у", id("0dde"), id("0d87"), "У"), Letter("ф", id("0dab"), id("0d55"), "Ф"),
    Letter("х", id("0dba"), id("0d64"), "Х"), Letter("ц", id("0ddc"), id("0d85"), "Ц"),
    Letter("ч", id("0da2"), id("0d4c"), "Ч"), Letter("ш", id("0dd3"), id("0d7c"), "Ш"),
    Letter("щ", id("0dd4"), id("0d7d"), "Щ"), Letter("ы", id("0de4"), id("0d8d"), "Ы"),
    Letter("ь", id("0dd8"), id("0d81"), "Ь", longPressLower = "ъ", longPressUpper = "Ъ"), Letter("э", id("0daa"), id("0d54"), "Э"),
    Letter("ю", id("0de6"), id("0d8f"), "Ю"), Letter("я", id("0de3"), id("0d8c"), "Я"),
)

private fun softkeys(letters: List<Letter>): String = buildString {
    appendLine("<?xml version=\"1.0\" encoding=\"utf-8\"?>")
    appendLine("<framework>")
    appendLine("    <include href=\"@xml/APKTOOL_RENAMED_0x7f170ef1\" />")
    appendLine("    <softkeys>")
    appendLine("        <softkey_list splitter=\",\" template_id=\"@id/APKTOOL_RENAMED_0x7f0b2688\">")
    letters.forEach { l ->
        val lowerLong = l.longPressLower?.let { " long_press_data=\"$it\"" } ?: ""
        val upperLong = l.longPressUpper?.let { " long_press_data=\"$it\"" } ?: ""
        appendLine("            <softkey id=\"${l.lowerId}\"$lowerLong press_data=\"${l.lower}\" />")
        appendLine("            <softkey id=\"${l.upperId}\"$upperLong press_data=\"${l.upper}\" />")
    }
    appendLine("        </softkey_list>")
    appendLine("    </softkeys>")
    appendLine("</framework>")
}

private fun keymap(letters: List<Letter>, rows: List<Int>): String = buildString {
    fun mapping(state: String, ids: List<String>) {
        append("        <key_mapping")
        if (state.isNotEmpty()) append(" state=\"$state\"")
        appendLine(">")
        var offset = 0
        rows.forEachIndexed { row, count ->
            for (column in 1..count) {
                val l = letters[offset++]
                val view = "${listOf('D', 'C', 'B')[row]}${column.toString().padStart(2, '0')}"
                appendLine("            <mapping key_id=\"${ids[letters.indexOf(l)]}\" view_id=\"@id/$view\" />")
            }
        }
        appendLine("        </key_mapping>")
    }
    appendLine("<?xml version=\"1.0\" encoding=\"utf-8\"?>")
    appendLine("<framework>")
    mapping("", letters.map { it.lowerId })
    mapping("SHIFT,CAPS", letters.map { it.upperId })
    appendLine("</framework>")
}

private fun russianMain(): String = """
    <?xml version="1.0" encoding="utf-8"?>
    <framework>
        <include href="@xml/APKTOOL_RENAMED_0x7f170493" />
        <keyboard>
            <if device="tablet" device_size="large" keyboard_mode="normal"><view layout="@layout/APKTOOL_RENAMED_0x7f0e042e" type="body" /></if>
            <else_if keyboard_mode="split"><view layout="@layout/APKTOOL_RENAMED_0x7f0e0418" type="body" /></else_if>
            <else><view layout="@layout/APKTOOL_RENAMED_0x7f0e0416" type="body" /></else>
            <view type="body">
                <include href="@xml/alphabetical_ru_softkeys" />
                <include href="@xml/APKTOOL_RENAMED_0x7f170c04" />
                <include href="@xml/alphabetical_ru_keymap" />
            </view>
        </keyboard>
        <include href="@xml/APKTOOL_RENAMED_0x7f17078b" />
        <if enable_number_row="true"><include href="@xml/APKTOOL_RENAMED_0x7f17076e" /></if>
    </framework>
""".trimIndent()

private fun englishMain(): String = """
    <?xml version="1.0" encoding="utf-8"?>
    <framework>
        <include href="@xml/APKTOOL_RENAMED_0x7f170493" />
        <keyboard>
            <if keyboard_mode="split"><view layout="@layout/APKTOOL_RENAMED_0x7f0e03a4" type="body" /></if>
            <else><view layout="@layout/APKTOOL_RENAMED_0x7f0e03a2" type="body" /></else>
            <view type="body">
                <include href="@xml/alphabetical_en_softkeys" />
                <include href="@xml/APKTOOL_RENAMED_0x7f170c04" />
                <include href="@xml/alphabetical_en_keymap" />
            </view>
        </keyboard>
        <include href="@xml/APKTOOL_RENAMED_0x7f17078b" />
        <if enable_number_row="true"><include href="@xml/APKTOOL_RENAMED_0x7f17075f" /></if>
    </framework>
""".trimIndent()

context(context: ResourcePatchContext)
private fun write(path: String, text: String) = context.get("$XML_DIR/$path", true).writeText(text)

context(context: ResourcePatchContext)
private fun applyPatch() {
    write("alphabetical_en_softkeys.xml", softkeys(english))
    write("alphabetical_en_keymap.xml", keymap(english, listOf(10, 9, 7)))
    write("alphabetical_en_main.xml", englishMain())
    write("alphabetical_ru_softkeys.xml", softkeys(russian))
    write("alphabetical_ru_keymap.xml", keymap(russian, listOf(11, 11, 8)))
    write("alphabetical_ru_main.xml", russianMain())

    val englishIme = context.get(ENGLISH_IME, true)
    var englishText = englishIme.readText()
    if ("@xml/alphabetical_en_main" !in englishText) {
        englishText = englishText.replace(
            "</ime>",
            "        <include href=\"@xml/APKTOOL_RENAMED_0x7f1706bd\" main_def=\"@xml/alphabetical_en_main\" pk_language_tag=\"en-Latn-US\" support_a11y_keyboard=\"true\" variant=\"alphabetical\" variant_label=\"ABCD\" />\n    </ime>"
        )
        englishIme.writeText(englishText)
    }

    val russianIme = context.get(RUSSIAN_IME, true)
    var russianText = russianIme.readText()
    if ("@xml/alphabetical_ru_main" !in russianText) {
        russianText = russianText.replace(
            "</ime>",
            "        <include href=\"@xml/APKTOOL_RENAMED_0x7f1706bd\" main_def=\"@xml/alphabetical_ru_main\" pk_language_tag=\"ru\" support_a11y_keyboard=\"true\" variant=\"alphabetical\" variant_label=\"ABCD\" />\n    </ime>"
        )
        russianIme.writeText(russianText)
    }
}

@Suppress("unused")
val alphabeticalLayoutPatch = resourcePatch(
    name = "Alphabetical Gboard layouts",
    description = "Adds ABCD English and Russian layouts; Russian uses Е→Ё, И→Й and Ь→Ъ on long press.",
    default = true,
) {
    compatibleWith(COMPATIBILITY_GBOARD_18_5_4)
    finalize { applyPatch() }
}
