package com.urosjarc.topsongs.app.stream

import com.urosjarc.topsongs.app.radio.RadioRepo
import com.urosjarc.topsongs.app.shared.Repository
import javafx.application.Platform
import org.koin.core.component.inject
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


class StreamRepo : Repository<Stream>() {
	val radioRepo by this.inject<RadioRepo>()
	val streamService by this.inject<StreamService>()
	val scheduler = Executors.newScheduledThreadPool(1)

	init {
		scheduler.scheduleAtFixedRate({
			Platform.runLater {
				this.radioRepo.chosen?.let {
					val stream = this.streamService.getStream(radio = it)
					this.chose(stream)
				}
			}
		}, 0, 5, TimeUnit.SECONDS)
	}
}
