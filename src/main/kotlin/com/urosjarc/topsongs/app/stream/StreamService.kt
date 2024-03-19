package com.urosjarc.topsongs.app.stream

import com.urosjarc.topsongs.app.radio.Radio
import com.urosjarc.topsongs.app.song.Song
import kotlinx.datetime.Clock
import java.io.File
import java.net.URL


class StreamService {

	fun getStream(radio: Radio): Stream {
		val stream = Stream(
			radio = radio,
			created = Clock.System.now(),
			info = null, song = null
		)

		try {
			val con = URL(radio.url).openConnection().also {
				it.setRequestProperty("Icy-MetaData", "1")
				it.setRequestProperty("Connection", "close")
				it.setRequestProperty("Accept", null)
				it.connectTimeout = 1000
				it.connect()
			}

			val chunkSize = con.getHeaderField("icy-metaint").toInt()

			val inputStream = con.getInputStream()
			val text = inputStream.readNBytes((chunkSize * 1.25).toInt()).toString(Charsets.US_ASCII)
			File("data.txt").appendText(text)
			inputStream.close()

			stream.song = Song(
				name = this.getValue(key = "StreamTitle", text = text)!!,
				created = Clock.System.now(),
				rated = false
			)

		} catch (e: Throwable) {
			stream.info = "Error: ${e.localizedMessage}"
		}

		return stream
	}

	private fun getValue(key: String, text: String): String? = Regex("$key='(.*?)';").find(text)?.groupValues?.getOrNull(1)
}
