package com.urosjarc.topsongs.gui.widgets

import com.urosjarc.topsongs.app.song.Song
import com.urosjarc.topsongs.app.song.SongRepo
import com.urosjarc.topsongs.app.stream.StreamRepo
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.ListView
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

abstract class SongListUi : KoinComponent {

	@FXML
	lateinit var songL: Label

	@FXML
	lateinit var saveB: Button

	@FXML
	lateinit var songLV: ListView<Song>
}

class SongList : SongRatingUi() {
	val streamRepo by this.inject<StreamRepo>()
	val songRepo by this.inject<SongRepo>()

	@FXML
	fun initialize() {
		this.streamRepo.onChose { this.songL.text = it.song.name }
		this.songRepo.onData { this.update() }
		this.saveB.setOnAction { this.songRepo.save(this.streamRepo.chosen!!.song) }

		this.update()
	}

	fun update() {
		this.songLV.items.setAll(this.songRepo.data)
	}
}
