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

abstract class SongQueueUi : KoinComponent {

	@FXML
	lateinit var songL: Label

	@FXML
	lateinit var saveB: Button

	@FXML
	lateinit var songLV: ListView<Song>
}

class SongQueue : SongQueueUi() {
	val streamRepo by this.inject<StreamRepo>()
	val songRepo by this.inject<SongRepo>()

	@FXML
	fun initialize() {
		this.streamRepo.onChose { this.songL.text = it.song.name }
		this.songRepo.onData { this.update() }
		this.saveB.setOnAction { this.songRepo.save(this.streamRepo.chosen!!.song) }
		this.songLV.selectionModel.selectedItemProperty().addListener { _, _, song ->
			if (song != null) this.songRepo.chose(song)
		}

		this.update()
	}

	fun update() {
		val unratedSongs = this.songRepo.data.filter { !it.rated }
		this.songLV.items.setAll(unratedSongs)
	}
}
