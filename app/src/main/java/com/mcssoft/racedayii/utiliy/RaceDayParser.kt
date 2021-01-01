package com.mcssoft.racedayii.utiliy

import android.content.Context
import android.util.Log
import okio.Okio
import org.w3c.dom.NodeList
import org.xml.sax.InputSource
import java.io.File
import java.io.InputStream
import javax.xml.xpath.XPathConstants
import javax.xml.xpath.XPathFactory

/**
 * Parse the xml within the downloaded RaceDay.xml file.
 * @param context: Only for system resources.
 */
class RaceDayParser(private val context: Context) {

    private var inStream: InputStream? = null

    /**
     * Set the input stream value used by the XPath InputSource.
     * @param file: A File object (containing relevant path info) to construct the input stream.
     */
    fun setInputStream(file: File) {
        val bufferedSource = Okio.buffer(Okio.source(file))
        inStream = bufferedSource.inputStream()
    }

    /**
     * Parse for all meetings.
     * @return A List<Map<LocalName, NodeValue>>.
     */
    fun parseForMeeting(): ArrayList<MutableMap<String, String>> {
        Log.e("TAG", "[RaceDayParser.parseForMeeting]")
        val expr = "/RaceDay/Meeting"
        return parse(expr)     // only one Meeting is expected.
    }

    /**
     * Parse for a specific <Race></Race> with a <Meeting></Meeting>.
     * @param meetingCode: The Meeting code, e.g. BR.
     * @return A Map<LocalName, NodeValue>.
     */
    fun parseForMeeting(meetingCode: String): MutableMap<String,String> {
        val expr = "/RaceDay/Meeting[@MeetingCode='$meetingCode']"
        return parse(expr)[0]     // only one Meeting is expected.
    }

    /**
     * Generic parse method. Will parse the given XPath expression into an ArrayList of Map.
     * @param xpathExpr: The XPath expression to parse on.
     * @return: An Array of Map<String,String> (Node LocalName and NodeValue).
     */
    private fun parse(xpathExpr: String): ArrayList<MutableMap<String, String>> {
        val lMap = ArrayList<MutableMap<String, String>>()

        try {
            val xpath = XPathFactory.newInstance().newXPath()
            val lNodes = xpath.evaluate(xpathExpr, InputSource(inStream), XPathConstants.NODESET) as NodeList
            val mapGet = mutableMapOf<String, String>()

            if (lNodes.length > 0) {
                val len = lNodes.length
                for (ndx in 0..len) {
                    val node = lNodes.item(ndx)
                    if (node != null) {
                        val lNodeAttrs = node.attributes
                        for (ndx2 in 0 until lNodeAttrs.length) {
                            val attrNode = lNodeAttrs.item(ndx2)
                            mapGet[attrNode.localName] = attrNode.nodeValue
                        }
                        val mapPut = HashMap(mapGet)
                        lMap.add(mapPut)
                        mapGet.clear()
                    }
                }
            }
        } catch(ex: Exception) {
            Log.e("TAG", "[RaceDayParser.parse] Exception: ${ex.message}")
        } finally {
            inStream?.close()
            return lMap
        }
    }
}
