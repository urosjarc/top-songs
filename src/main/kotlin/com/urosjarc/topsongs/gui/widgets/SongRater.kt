package com.urosjarc.topsongs.gui.widgets

import com.urosjarc.topsongs.app.song.SongRepo
import com.urosjarc.topsongs.app.stream.StreamRepo
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.Label
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

abstract class SongRaterUi : KoinComponent {

	@FXML
	lateinit var songL: Label

	@FXML
	lateinit var saveB: Button
}

class SongRater : SongRaterUi() {
	val streamRepo by this.inject<StreamRepo>()
	val songRepo by this.inject<SongRepo>()

	@FXML
	fun initialize() {
		this.streamRepo.onChose {
			this.songL.text = it.song.name
		}
		this.saveB.setOnAction {
			this.songRepo.save(this.streamRepo.chosen!!.song)
		}
	}

}
