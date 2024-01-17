package com.urosjarc.topsongs.app.stream

import com.urosjarc.topsongs.app.radio.RadioRepo
import com.urosjarc.topsongs.shared.Repository
import com.urosjarc.topsongs.shared.startThread
import javafx.application.Platform
import org.koin.core.component.inject

class StreamRepo : Repository<Stream>() {
	val radioRepo by this.inject<RadioRepo>()
	val streamService by this.inject<StreamService>()
	var lastStream: Stream? = null

	init {
		startThread(sleep = 1000, repeat = true) {
			radioRepo.chosen?.let { radio ->
				val stream = streamService.getStream(radio = radio)

				if (this.lastStream == null || (stream.song?.name != lastStream?.song?.name && stream.song != null)) {
					this.lastStream = stream
				}

				this.lastStream!!.info = stream.info

				Platform.runLater {
					chose(this.lastStream!!)
				}
			}
		}
	}
}
