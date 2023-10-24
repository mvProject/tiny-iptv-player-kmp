/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 04.05.23, 10:59
 *
 */

package com.mvproject.tinyiptv.data.parser

object EpgParser {
    private const val TYPE_PROGRAM = "programme"

    private const val FIELD_TITLE = "title"
    private const val FIELD_DESCRIPTION = "desc"
    private const val FIELD_START = "start"
    private const val FIELD_STOP = "stop"
    private const val FIELD_CHANNEL = "channel"

    //  private const val BUFFER_SIZE = 16384
    private const val BUFFER_SIZE = 65536

    /*suspend fun parseTarGzEpg(
        data: ByteReadChannel,
        onProgramParsed: suspend (EpgProgramParseModel) -> Unit
    ) = withContext(Dispatchers.IO) {
        var currentProgram: EpgProgramParseModel? = null

        val inputStream = GZIPInputStream(data.toInputStream(), BUFFER_SIZE)
        val inputStreamReader = InputStreamReader(inputStream, Charsets.UTF_8)

        val parser = XmlPullParserFactory.newInstance()
            .newPullParser()
            .apply {
                setInput(inputStreamReader)
            }

        var eventType = parser.eventType

        while (eventType != XmlPullParser.END_DOCUMENT) {
            when (eventType) {
                XmlPullParser.START_TAG -> {
                    when (parser.name) {
                        TYPE_PROGRAM -> {
                            with(parser) {
                                currentProgram = EpgProgramParseModel(
                                    start = getAttributeValue(null, FIELD_START),
                                    stop = getAttributeValue(null, FIELD_STOP),
                                    channelId = getAttributeValue(null, FIELD_CHANNEL)
                                )
                            }
                        }

                        FIELD_TITLE -> {
                            currentProgram?.title = parser.nextText()
                        }

                        FIELD_DESCRIPTION -> {
                            currentProgram?.description = parser.nextText()
                        }
                    }
                }

                XmlPullParser.END_TAG -> {
                    when (parser.name) {
                        TYPE_PROGRAM -> {
                            // Store the current program in the database
                            currentProgram?.let { onProgramParsed(it) }
                            currentProgram = null
                        }
                    }
                }
            }

            eventType = parser.next()
        }
    }
*/
    /* suspend fun parseTarGzEpg2(
         data: ByteReadChannel,
         onProgramParsed: (EpgProgramParseModel) -> Unit
     ) = withContext(Dispatchers.IO) {

         val inputStream = GZIPInputStream(data.toInputStream(), BUFFER_SIZE)
         val inputStreamReader = InputStreamReader(inputStream, Charsets.UTF_8)

         val factory = SAXParserFactory.newInstance()
         val saxParser = factory.newSAXParser()

         val handler = object : DefaultHandler() {
             var currentProgram: EpgProgramParseModel? = null
             var currentValue: String? = null

             override fun startElement(
                 uri: String?,
                 localName: String?,
                 qName: String?,
                 attributes: Attributes?
             ) {
                 when (qName) {
                     TYPE_PROGRAM -> {
                         currentProgram = EpgProgramParseModel(
                             start = attributes?.getValue(FIELD_START) ?: EMPTY_STRING,
                             stop = attributes?.getValue(FIELD_STOP) ?: EMPTY_STRING,
                             channelId = attributes?.getValue(FIELD_CHANNEL) ?: EMPTY_STRING
                         )
                     }

                     FIELD_TITLE, FIELD_DESCRIPTION -> {
                         currentValue = EMPTY_STRING
                     }
                 }
             }

             override fun characters(ch: CharArray?, start: Int, length: Int) {
                 currentValue += String(ch!!, start, length)
             }

             override fun endElement(uri: String?, localName: String?, qName: String?) {
                 when (qName) {
                     FIELD_TITLE -> {
                         currentProgram?.title = currentValue ?: EMPTY_STRING
                     }

                     FIELD_DESCRIPTION -> {
                         currentProgram?.description = currentValue ?: EMPTY_STRING
                     }

                     TYPE_PROGRAM -> {
                         currentProgram?.let { onProgramParsed(it) }
                         currentProgram = null
                     }
                 }
             }
         }

         saxParser.parse(InputSource(inputStreamReader), handler)
     }*/
}