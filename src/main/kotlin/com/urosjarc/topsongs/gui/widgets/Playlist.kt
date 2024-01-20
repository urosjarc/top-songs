package com.urosjarc.topsongs.gui.widgets

import javafx.fxml.FXML
import org.koin.core.component.KoinComponent


abstract class PlaylistUi : KoinComponent {
}

class Playlist : PlaylistUi() {

	@FXML
	fun initialize() {
	}
}
