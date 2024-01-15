package com.urosjarc.topsongs.gui.widgets

import com.urosjarc.topsongs.app.log.LogService
import com.urosjarc.topsongs.app.song.*
import com.urosjarc.topsongs.shared.startThread
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.ComboBox
import javafx.scene.control.TextField
import kotlinx.datetime.Clock
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


abstract class PlaylistUi : KoinComponent {
}

class Playlist : PlaylistUi() {

	@FXML
	fun initialize() {
	}
}
