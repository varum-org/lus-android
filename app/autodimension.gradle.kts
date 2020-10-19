import org.w3c.dom.Element
import org.w3c.dom.Node
import java.io.PrintWriter
import java.util.*
import javax.xml.parsers.DocumentBuilderFactory
import kotlin.collections.HashMap

enum class DimenType {
/**
 * Best for auto generate the dimension by the default values/dimens.xml file.
 */
FROM_DIMEN_FILE,
/**
 * Best for auto generate the dimension as default.
 */
AUTO_CREATE
}

open class DimenFactory : DefaultTask() {
@Input
private val dimens = intArrayOf(
320,
360,
384,
411,
480,
540,
600,
720,
800,
960,
1024,
1080,
1280,
1440,
2560,
3840
)
@Input
private val fromDimen = 360f
@Input
private val positiveMaxDP = 400
@Input
private val positiveMaxSP = 60
@Input
private val negativeMaxDP = 60
@Input
private val type = DimenType.AUTO_CREATE
@Input
private val dimenFileName = "values/dimens.xml"

private val resFolder = project.projectDir.path + "/src/main/res/"

@TaskAction
fun create() {
when (type) {
DimenType.AUTO_CREATE -> autoCreateDimen()
DimenType.FROM_DIMEN_FILE -> createDimenFromDimenFile()
}
}

private fun createDimenFromDimenFile() {
println("Start convert dimen from value file to other screen size")
val path = resFolder + dimenFileName
val pairs = HashMap<String, String>()
try {
val fXmlFile = File(path)
val dbFactory = DocumentBuilderFactory.newInstance()
val dBuilder = dbFactory.newDocumentBuilder()
val doc = dBuilder.parse(fXmlFile)
doc.documentElement.normalize()

val nList = doc.getElementsByTagName("dimen")
for (i in 0 until nList.length) {
val nNode = nList.item(i)
if (nNode.nodeType == Node.ELEMENT_NODE) {
val eElement = nNode as Element
if (eElement.textContent.contains("@dimen")) continue
pairs[eElement.getAttribute("name")] = eElement.textContent
}
}
val dps = HashMap<String, Double>()
val sps = HashMap<String, Double>()

for (entry in pairs) {
val value = entry.value.replace("dp", "").replace("sp", "").toDouble()
if (entry.value.contains("dp")) {
dps[entry.key] = value
} else {
sps[entry.key] = value
}
}
//Sort
val sortedDps = TreeMap(dps)
val sortedSps = TreeMap(sps)
for (dimen in dimens) {
val folder = resFolder + "values-sw" + dimen + "dp"
val fileName = "$folder/auto_dimens.xml"
File(folder).mkdir()
File(fileName).createNewFile()
val printWriter = PrintWriter(fileName)
printWriter.println("<?xml version=\"1.0\" encoding=\"utf-8\"?>")
printWriter.println("<resources>")
for (entry in sortedDps) {
val ratio = dimen / fromDimen
val dp = ratio * entry.value
printWriter.printf(
"\t<dimen name=\"%s\">%.2fdp</dimen>\r\n",
entry.key,
dp
)
}
printWriter.println()
for (entry in sortedSps) {
val ratio = dimen / fromDimen
val sp = ratio * entry.value
printWriter.printf(
"\t<dimen name=\"%s\">%.2fsp</dimen>\r\n",
entry.key,
sp
)
}
printWriter.println("</resources>")
printWriter.close()
}
} catch (e: Exception) {
e.printStackTrace()
}
}

private fun autoCreateDimen() {
// write dimen.xml
val defaultFolder = resFolder + "values"
val defaultDimensFile = "$defaultFolder/dimens.xml"
File(defaultFolder).mkdir()
File(defaultDimensFile).createNewFile()
writeAutoDimen(defaultDimensFile, 360)

// write other auto_dimens.xml
for (dimen in dimens) {
val folder = resFolder + "values-sw" + dimen + "dp"
val fileName = "$folder/auto_dimens.xml"
File(folder).mkdir()
File(fileName).createNewFile()
writeAutoDimen(fileName, dimen)
}
}

private fun writeAutoDimen(fileName: String, dimen: Int) {
println("Auto create dimension file and values $fileName")
val printWriter = PrintWriter(fileName)
printWriter.println("<?xml version=\"1.0\" encoding=\"utf-8\"?>")
printWriter.println("<resources>")
for (i in 1..positiveMaxDP) {
val ratio = dimen / fromDimen
val dp = ratio * i
printWriter.printf("\t<dimen name=\"dp_%d\">%.2fdp</dimen>\r\n", i, dp)
}
printWriter.println()
for (i in 1..negativeMaxDP) {
val ratio = dimen / fromDimen
val dp = ratio * i
printWriter.printf("\t<dimen name=\"dp_minus%d\">%.2fdp</dimen>\r\n", i, -dp)
}
printWriter.println()
for (i in 1..positiveMaxSP) {
val ratio = dimen / dimens[0].toFloat()
val sp = ratio * i
printWriter.printf("\t<dimen name=\"sp_%d\">%.2fsp</dimen>\r\n", i, sp)
}
printWriter.println("</resources>")
printWriter.close()
}
}

tasks.register("createDimen", DimenFactory::class) {
create()
}
