package com.urosjarc.topsongs.app.stream

import com.urosjarc.topsongs.app.radio.RadioRepo
import com.urosjarc.topsongs.app.shared.Repository
import com.urosjarc.topsongs.app.shared.startThread
import org.koin.core.component.inject

class StreamRepo : Repository<Stream>() {
	val radioRepo by this.inject<RadioRepo>()
	val streamService by this.inject<StreamService>()

	init {
		startThread(sleep = 1000, repeat = true) {
			radioRepo.chosen?.let {
				val stream = streamService.getStream(radio = it)
				chose(stream)
			}
		}
	}
}
