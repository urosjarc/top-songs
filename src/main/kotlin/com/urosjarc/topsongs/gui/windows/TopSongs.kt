package com.urosjarc.topsongs.gui.windows

import com.urosjarc.topsongs.app.log.Log
import com.urosjarc.topsongs.app.log.LogRepo
import javafx.fxml.FXML
import javafx.scene.control.Alert
import javafx.scene.control.Alert.AlertType
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


abstract class TopSongsUI : KoinComponent

class TopSongs : TopSongsUI() {
	val logRepo by this.inject<LogRepo>()

	@FXML
	fun initialize() {
		logRepo.onChose { log ->
			when (log.type) {
				Log.Type.WARN -> AlertType.WARNING
				Log.Type.ERROR -> AlertType.ERROR
				Log.Type.FATAL -> AlertType.ERROR
				else -> null
			}?.let { type -> Alert(type, log.data).also { it.showAndWait() } }
		}

	}
}
